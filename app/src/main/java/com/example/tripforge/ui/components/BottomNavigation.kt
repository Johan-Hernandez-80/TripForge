package com.example.tripforge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@Composable
fun BottomNav(
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        NavItem("Home", Icons.Default.Home, selected.lowercase() == "home", Modifier.weight(1f)) {
            onSelect("home")
        }

        NavItem("Trips", Icons.Default.Luggage, selected.lowercase() == "trips", Modifier.weight(1f)) {
            onSelect("trips")
        }

        NavItem("Map", Icons.Default.LocationOn, selected.lowercase() == "map", Modifier.weight(1f)) {
            onSelect("map")
        }

        NavItem("Profile", Icons.Default.AccountCircle, selected.lowercase() == "profile", Modifier.weight(1f)) {
            onSelect("profile")
        }
    }
}

@Composable
fun NavItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isActive: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    val color = if (isActive) Color(0xFF2563EB) else Color.Gray

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color
        )

        Text(
            text = label,
            fontSize = 12.sp,
            color = color
        )
    }
}
