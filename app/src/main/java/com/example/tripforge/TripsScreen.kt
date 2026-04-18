package com.example.tripforge

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.tripforge.ui.components.BottomNav

@Composable
fun TripsScreen(
    onTripClick: () -> Unit,
    selectedTab: String? = null,
    onSelectTab: ((String) -> Unit)? = null
) {
    val tabs = listOf("All", "Upcoming", "Ongoing", "Complete")
    var activeTab by remember { mutableStateOf("All") }

    Scaffold(
        bottomBar = {
            if (selectedTab != null && onSelectTab != null) {
                Surface(
                    tonalElevation = 0.dp,
                    shadowElevation = 16.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    BottomNav(selected = selectedTab, onSelect = onSelectTab)
                }
            }
        }
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

            TripCard("Tokyo Adventure", "Tokyo, Japan", onClick = onTripClick)
            TripCard("Paris Romance", "Paris, France", onClick = onTripClick)
        }
    }
}

@Composable
fun TripCard(title: String, location: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(MaterialTheme.colorScheme.outline)
        )

        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                Text(location, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            
            Row {
                IconButton(onClick = { /* TODO: Edit trip */ }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = { /* TODO: Delete trip */ }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
