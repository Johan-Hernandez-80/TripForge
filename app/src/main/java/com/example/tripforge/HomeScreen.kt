package com.example.tripforge

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.example.tripforge.ui.components.*

@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()
    var selectedTab by remember { mutableStateOf("home") }

    Scaffold(
        bottomBar = {
            Surface(
                tonalElevation = 0.dp, // Elevación 0 para mantener el blanco limpio
                shadowElevation = 16.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                BottomNav(selected = selectedTab, onSelect = { selectedTab = it })
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // #F8FAFC
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                HeaderSection() // Ahora usa colores del tema internamente

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    SectionHeader(title = "Featured Trip")
                    FeaturedTripCard()

                    Spacer(modifier = Modifier.height(24.dp))

                    SectionHeader(title = "Recent Trips", hasSeeAll = true)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        TripCardBig("Tokyo Adventure", "Tokyo, Japan", R.drawable.tokyo, Modifier.weight(1f))
                        TripCardBig("Paris Romance", "Paris, France", R.drawable.paris, Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Explore Destinations",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground // #1E293B
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
}

@Composable
fun HeaderSection() {
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
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("New Trip", color = primaryColor, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                ) {
                    Text("View All", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, hasSeeAll: Boolean = false) {
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
            TextButton(onClick = { }) {
                Text("See all", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}