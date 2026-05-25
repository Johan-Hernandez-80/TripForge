package com.example.tripforge.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.tripforge.data.TripRepository
import com.example.tripforge.model.ActivityItem
import com.example.tripforge.ui.components.ScreenHeader
import com.example.tripforge.ui.components.ActivityCardCompact
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date

@Composable
fun ItineraryScreen(
    tripId: String,
    onBack: () -> Unit = {},
    onAddActivity: () -> Unit = {},
    onEditActivity: (ActivityItem) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { TripRepository(context) }
    
    val trips by repository.trips.collectAsState(initial = emptyList())
    val trip = trips.find { it.id == tripId }

    if (trip == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    ScreenHeader(
                        title = "Itinerary",
                        subtitle = trip.title,
                        onBack = onBack
                    )
                }
            }

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val dateFormatter = remember { DateFormat.getDateInstance() }
                
                val allActivities = trip.itinerary.flatMap { it.activities }
                    .sortedBy { activity ->
                        try {
                            dateFormatter.parse(activity.date)?.time ?: Long.MAX_VALUE
                        } catch (e: Exception) {
                            Long.MAX_VALUE
                        }
                    }

                if (allActivities.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No activities planned yet.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    allActivities.forEach { activity ->
                        ActivityCardCompact(
                            activity = activity,
                            onToggleCompletion = {
                                scope.launch {
                                    repository.toggleActivityCompletion(tripId, activity.id)
                                }
                            },
                            onEdit = { onEditActivity(activity) },
                            onDelete = {
                                scope.launch {
                                    repository.deleteActivity(tripId, activity.id)
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        FloatingActionButton(
            onClick = onAddActivity,
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add activity", tint = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
