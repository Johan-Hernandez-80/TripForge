package com.example.tripforge

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.tripforge.ui.components.BottomNav

@Composable
fun TripsScreen() {

    var selectedTab by remember { mutableStateOf("trips") }
    val tabs = listOf("All", "Upcoming", "Ongoing", "Complete")
    var activeTab by remember { mutableStateOf("All") }

    Scaffold(
        bottomBar = {
            BottomNav(selected = selectedTab, onSelect = { selectedTab = it })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {

            Spacer(Modifier.height(16.dp))

            Text(
                "My Trips",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 24.dp)
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
                        color = if (tab == activeTab) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            TripCard("Tokyo Adventure", "Tokyo, Japan")
            TripCard("Paris Romance", "Paris, France")
        }
    }
}

@Composable
fun TripCard(title: String, location: String) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(Color.Gray)
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(location, style = MaterialTheme.typography.bodySmall)
        }
    }
}