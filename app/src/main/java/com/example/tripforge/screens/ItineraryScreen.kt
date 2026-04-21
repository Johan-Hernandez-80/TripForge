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
import com.example.tripforge.data.TripDataStore
import com.example.tripforge.data.sampleDayPlans
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
    val dataStore = remember { TripDataStore(context) }
    val tripId = "sample_trip_id"

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
                        subtitle = "Tokyo Adventure",
                        onBack = onBack
                    )
                }
            }

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                sampleDayPlans.forEach { dayPlan ->
                    TripDaySection(
                        day = dayPlan.day,
                        dateLabel = dayPlan.dateLabel,
                        activities = dayPlan.activities,
                        onDeleteActivity = { activityId ->
                            scope.launch {
                                dataStore.deleteActivity(tripId, activityId)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(72.dp))
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
