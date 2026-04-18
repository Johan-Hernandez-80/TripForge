package com.example.tripforge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.example.tripforge.ui.components.BottomNav

data class Marker(
    val id: Int,
    val type: String,
    val x: Float,
    val y: Float
)

@Composable
fun MapScreen(
    selectedTab: String? = null,
    onSelectTab: ((String) -> Unit)? = null
) {
    val markers = listOf(
        Marker(1, "trip", 0.2f, 0.15f),
        Marker(2, "trip", 0.35f, 0.35f),
        Marker(3, "trip", 0.5f, 0.55f),
        Marker(4, "activity", 0.65f, 0.25f),
        Marker(5, "activity", 0.8f, 0.45f)
    )

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

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Search locations...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(24.dp)
                    )
            ) {

                markers.forEach {
                    Box(
                        modifier = Modifier
                            .offset(
                                x = (it.x * 300).dp,
                                y = (it.y * 500).dp
                            )
                            .size(20.dp)
                            .background(
                                if (it.type == "trip")
                                    MaterialTheme.colorScheme.primary
                                else
                                    Color(0xFFFF9800),
                                RoundedCornerShape(50)
                            )
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(12.dp)
                ) {
                    Text("Legend", style = MaterialTheme.typography.labelMedium)

                    Spacer(Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(12.dp)
                                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Trip")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(12.dp)
                                .background(Color(0xFFFF9800), RoundedCornerShape(50))
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Activity")
                    }
                }
            }
        }
    }
}
