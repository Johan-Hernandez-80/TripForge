package com.example.tripforge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tripforge.data.sampleTripDetails
import com.example.tripforge.ui.components.MetricCard
import com.example.tripforge.ui.components.SectionHeader
import com.example.tripforge.ui.components.TripBulletItem

@Composable
fun TripDetailsScreen(
    onBack: () -> Unit = {},
    onOpenBudget: () -> Unit = {},
    onOpenItinerary: () -> Unit = {},
    onOpenPacking: () -> Unit = {}
) {
    val trip = sampleTripDetails

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
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Text(trip.title, color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(trip.location, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f), style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("11 days", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f), style = MaterialTheme.typography.bodyMedium)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Black.copy(alpha = 0.30f), shape = MaterialTheme.shapes.large)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                
                Row {
                    IconButton(
                        onClick = { /* TODO: Edit */ },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.Black.copy(alpha = 0.30f), shape = MaterialTheme.shapes.large)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                    }
                    Spacer(Modifier.width(8.dp))
                    IconButton(
                        onClick = { /* TODO: Delete */ },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.Black.copy(alpha = 0.30f), shape = MaterialTheme.shapes.large)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                    }
                }
            }
        }

        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
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
                            .background(MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.medium),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Travel Dates", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("April 14, 2026 - April 24, 2026", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard(
                    title = "Budget",
                    value = "$1,200",
                    subtitle = "of $3,500",
                    icon = Icons.Default.AttachMoney,
                    iconTint = MaterialTheme.colorScheme.secondary,
                    backgroundTint = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.weight(1f),
                    onClick = onOpenBudget
                )

                MetricCard(
                    title = "Packing",
                    value = "5/12",
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

                    TripBulletItem(
                        title = "Visit Senso-ji Temple",
                        location = "Asakusa, Tokyo",
                        time = "09:00",
                        dateLabel = "Apr 15"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    TripBulletItem(
                        title = "Shibuya Crossing Experience",
                        location = "Shibuya, Tokyo",
                        time = "14:00",
                        dateLabel = "Apr 15"
                    )
                }
            }
        }
    }
}
