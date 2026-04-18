package com.example.tripforge

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tripforge.ui.components.*

@Composable
fun HomeScreen(navController: NavController? = null) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            HeaderSection(
                onNewTrip = { navController?.navigate("add_trip") },
                onViewAll = { navController?.navigate("trips") }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                SectionHeader(title = "Featured Trip")
                Box(modifier = Modifier.clickable { navController?.navigate("trip_details") }) {
                    FeaturedTripCard()
                }

                Spacer(modifier = Modifier.height(24.dp))

                SectionHeader(
                    title = "Recent Trips",
                    hasSeeAll = true,
                    onSeeAll = { navController?.navigate("trips") }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    TripCardBig(
                        "Tokyo Adventure",
                        "Tokyo, Japan",
                        R.drawable.tokyo,
                        Modifier.weight(1f).clickable { navController?.navigate("trip_details") }
                    )
                    TripCardBig(
                        "Paris Romance",
                        "Paris, France",
                        R.drawable.paris,
                        Modifier.weight(1f).clickable { navController?.navigate("trip_details") }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Explore Destinations",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))
                TripCardLong("Santorini, Greece", "Sunsets and white architecture", R.drawable.tokyo)
                Spacer(modifier = Modifier.height(12.dp))
                TripCardLong("Rome, Italy", "History and amazing food", R.drawable.paris)

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun HeaderSection(onNewTrip: () -> Unit, onViewAll: () -> Unit) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(primaryColor, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 32.dp)
    ) {
        Column {
            Text(
                text = "TripForge",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onNewTrip,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Text("New Trip", color = primaryColor, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onViewAll,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f))
                ) {
                    Text("View All", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, hasSeeAll: Boolean = false, onSeeAll: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        if (hasSeeAll) {
            TextButton(onClick = onSeeAll) {
                Text("See all", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
