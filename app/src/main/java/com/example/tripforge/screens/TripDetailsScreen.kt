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
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
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
    
    // Collect trips from the repository flow to ensure live updates from other screens
    val trips by repository.trips.collectAsState(initial = emptyList())
    val currentTrip = trips.find { it.id == trip.id } ?: trip

    val totalSpent = currentTrip.expenses.sumOf { it.amount }
    val budgetTotal = currentTrip.budgetTotal
    val packedCount = currentTrip.packingList.count { it.checked }
    val totalPacking = currentTrip.packingList.size
    val remainingBudget = budgetTotal - totalSpent
    
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    // Strings for display using local variables to simplify interpolation
    val spentDisplay = "$remainingBudget"
    val budgetSubtitle = "of $budgetTotal"
    val packingDisplay = "$packedCount/$totalPacking"

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Trip") },
            text = { Text("Are you sure you want to delete '${currentTrip.title}'? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            repository.deleteTrip(currentTrip.id)
                            onBack()
                        }
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

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
            if (currentTrip.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = currentTrip.imageUrl,
                    contentDescription = currentTrip.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                )
            }
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
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = currentTrip.location,
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = currentTrip.startDate,
                        color = Color.White.copy(alpha = 0.9f),
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
                        onClick = { showDeleteDialog = true },
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
                    value = spentDisplay,
                    subtitle = budgetSubtitle,
                    icon = Icons.Default.AttachMoney,
                    iconTint = MaterialTheme.colorScheme.secondary,
                    backgroundTint = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.weight(1f),
                    onClick = onOpenBudget
                )

                MetricCard(
                    title = "Packing",
                    value = packingDisplay,
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
                        val upcoming = currentTrip.itinerary
                            .flatMap { day -> day.activities.map { it to day.dateLabel } }
                            .take(2)

                        upcoming.forEach { (activity, dateLabel) ->
                            TripBulletItem(
                                title = activity.title,
                                location = activity.location,
                                time = activity.time,
                                dateLabel = dateLabel
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }
                }
            }
        }
    }
}
