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
import com.example.tripforge.ui.components.ScreenHeader
import com.example.tripforge.ui.components.TripDaySection
import kotlinx.coroutines.launch

@Composable
fun ItineraryScreen(
    tripId: String,
    onBack: () -> Unit = {},
    onAddActivity: () -> Unit = {}
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
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                if (trip.itinerary.isEmpty()) {
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
                    trip.itinerary.sortedBy { it.day }.forEach { dayPlan ->
                        TripDaySection(
                            day = dayPlan.day,
                            dateLabel = dayPlan.dateLabel,
                            activities = dayPlan.activities,
                            onDeleteActivity = { activityId ->
                                scope.launch {
                                    repository.deleteActivity(tripId, activityId)
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
