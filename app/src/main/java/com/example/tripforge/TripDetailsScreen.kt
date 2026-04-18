package com.example.tripforge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tripforge.data.sampleDayPlans
import com.example.tripforge.data.sampleTripDetails
import com.example.tripforge.ui.components.MetricCard
import com.example.tripforge.ui.components.ProgressBar
import com.example.tripforge.ui.components.ScreenHeader
import com.example.tripforge.ui.components.SectionHeader
import com.example.tripforge.ui.components.TripBulletItem

@Composable
fun TripDetailsScreen(
    tripId: Int = sampleTripDetails.id,
    onBack: () -> Unit = { /* TODO placeholder */ },
    onOpenBudget: () -> Unit = { /* TODO placeholder */ },
    onOpenItinerary: () -> Unit = { /* TODO placeholder */ }
) {
    val trip = sampleTripDetails

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(Color(0xFF1D4ED8))
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Text(trip.title, color = Color.White, fontSize = MaterialTheme.typography.headlineMedium.fontSize)
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(trip.location, color = Color.White.copy(alpha = 0.9f), fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("11 days", color = Color.White.copy(alpha = 0.9f), fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                }
            }

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(24.dp)
                    .size(40.dp)
                    .background(Color.Black.copy(alpha = 0.30f), shape = MaterialTheme.shapes.large)
            ) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.White)
            }
        }

        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(shape = MaterialTheme.shapes.large) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFFEFF6FF), shape = MaterialTheme.shapes.medium),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color(0xFF2563EB))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Travel Dates", fontSize = MaterialTheme.typography.bodySmall.fontSize, color = Color(0xFF6B7280))
                        Text("April 14, 2026 - April 24, 2026", fontSize = MaterialTheme.typography.bodyMedium.fontSize, color = Color(0xFF111827))
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard(
                    title = "Budget",
                    value = "$1,200",
                    subtitle = "of $3,500",
                    icon = Icons.Default.AttachMoney,
                    iconTint = Color(0xFF0F766E),
                    backgroundTint = Color(0xFFE6FFFB),
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = "Packing",
                    value = "5/12",
                    subtitle = "items packed",
                    icon = Icons.Default.Folder,
                    iconTint = Color(0xFFEA580C),
                    backgroundTint = Color(0xFFFFF7ED),
                    modifier = Modifier.weight(1f)
                )
            }

            Card(shape = MaterialTheme.shapes.large) {
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