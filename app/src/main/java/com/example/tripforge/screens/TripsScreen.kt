package com.example.tripforge.screens

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.example.tripforge.data.TripRepository
import com.example.tripforge.model.TripStatus
import com.example.tripforge.model.TripSummary
import com.example.tripforge.ui.components.TripCard
import kotlinx.coroutines.launch

@Composable
fun TripsScreen(
    onTripClick: (TripSummary) -> Unit = {},
    onEditTrip: (TripSummary) -> Unit = {},
    selectedTab: String? = null,
    onSelectTab: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    val repository = remember { TripRepository(context) }
    val trips by repository.trips.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    val tabs = listOf("All", "Upcoming", "Ongoing", "Complete")
    var activeTab by remember { mutableStateOf("All") }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var tripToDelete by remember { mutableStateOf<TripSummary?>(null) }

    if (showDeleteDialog && tripToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                tripToDelete = null
            },
            title = { Text("Delete Trip") },
            text = { Text("Are you sure you want to delete '${tripToDelete?.title}'? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        tripToDelete?.let { trip ->
                            scope.launch {
                                repository.deleteTrip(trip.id)
                            }
                        }
                        showDeleteDialog = false
                        tripToDelete = null
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    tripToDelete = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {

            Spacer(Modifier.height(16.dp))

            Text(
                "My Trips",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 24.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.padding(horizontal = 24.dp)) {
                tabs.forEach { tab ->
                    Text(
                        tab,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable { activeTab = tab }
                            .background(
                                if (tab == activeTab)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        color = if (tab == activeTab) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            if (trips.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "there are no trips yet",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                val filteredTrips = when (activeTab) {
                    "Upcoming" -> trips.filter { it.status == TripStatus.UPCOMING }
                    "Ongoing" -> trips.filter { it.status == TripStatus.ONGOING }
                    "Complete" -> trips.filter { it.status == TripStatus.COMPLETE }
                    else -> trips
                }

                if (filteredTrips.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "no $activeTab trips",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(filteredTrips) { trip ->

                            val cardColor = when (trip.status) {
                                TripStatus.COMPLETE -> MaterialTheme.colorScheme.secondary
                                else -> MaterialTheme.colorScheme.surface
                            }

                            TripCard(
                                trip = trip,
                                cardColor = cardColor,
                                onClick = { onTripClick(trip) },
                                onEdit = { onEditTrip(trip) },
                                onDelete = {
                                    tripToDelete = trip
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

