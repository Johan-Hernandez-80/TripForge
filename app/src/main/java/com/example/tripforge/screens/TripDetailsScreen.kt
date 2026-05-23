package com.example.tripforge.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.tripforge.data.TripRepository
import com.example.tripforge.model.TripSummary
import com.example.tripforge.model.TripStatus
import com.example.tripforge.ui.components.MetricCard
import com.example.tripforge.ui.components.SectionHeader
import com.example.tripforge.ui.components.TripBulletItem
import kotlinx.coroutines.launch

@Composable
fun TripDetailsScreen(
    trip: TripSummary,
    onBack: () -> Unit = {},
    onEdit: () -> Unit = {},
    onOpenBudget: () -> Unit = {},
    onOpenItinerary: () -> Unit = {},
    onOpenPacking: () -> Unit = {}
) {
    val context = LocalContext.current
    val repository = remember { TripRepository(context) }
    val scope = rememberCoroutineScope()
    var currentTrip by remember { mutableStateOf(trip) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Text(
                    text = currentTrip.title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = currentTrip.location,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = currentTrip.startDate,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (currentTrip.status != TripStatus.COMPLETE) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    val updatedTrip = currentTrip.copy(status = TripStatus.COMPLETE)
                                    repository.saveTrip(updatedTrip, isEdit = true)
                                    currentTrip = updatedTrip
                                }
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    Color.Black.copy(alpha = 0.30f),
                                    shape = MaterialTheme.shapes.large
                                )
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Mark Complete", tint = Color.White)
                        }
                    }
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                Color.Black.copy(alpha = 0.30f),
                                shape = MaterialTheme.shapes.large
                            )
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                    }
                    IconButton(
                        onClick = {
                            scope.launch {
                                repository.deleteTrip(currentTrip.id)
                                onBack()
                            }
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                Color.Black.copy(alpha = 0.30f),
                                shape = MaterialTheme.shapes.large
                            )
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                shape = MaterialTheme.shapes.medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "Travel Dates",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            "${currentTrip.startDate} - ${currentTrip.endDate}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard(
                    title = "Budget",
                    value = "$${currentTrip.budgetSpent}",
                    subtitle = "of $${currentTrip.budgetTotal}",
                    icon = Icons.Default.AttachMoney,
                    iconTint = MaterialTheme.colorScheme.secondary,
                    backgroundTint = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.weight(1f),
                    onClick = onOpenBudget
                )

                MetricCard(
                    title = "Packing",
                    value = "${currentTrip.packingList.count { it.checked }}/${currentTrip.packingList.size}",
                    subtitle = "items packed",
                    icon = Icons.Default.Inventory,
                    iconTint = MaterialTheme.colorScheme.tertiary,
                    backgroundTint = MaterialTheme.colorScheme.tertiaryContainer,
                    modifier = Modifier.weight(1f),
                    onClick = onOpenPacking
                )
            }

            Card(
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionHeader(
                        title = "Itinerary",
                        actionText = "View all",
                        onAction = onOpenItinerary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (currentTrip.itinerary.isEmpty()) {
                        Text(
                            "No activities planned yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        currentTrip.itinerary.firstOrNull()?.activities?.take(2)?.forEach { activity ->
                            TripBulletItem(
                                title = activity.title,
                                location = activity.location,
                                time = activity.time,
                                dateLabel = currentTrip.itinerary.firstOrNull()?.dateLabel
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }
                }
            }
        }
    }
}
