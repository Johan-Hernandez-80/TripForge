package com.example.tripforge.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.example.tripforge.data.TripRepository
import com.example.tripforge.model.TripStatus
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    isDarkMode: Boolean = false,
    onDarkModeChange: (Boolean) -> Unit = {},
    onNavigateToTravelPreferences: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToPrivacy: () -> Unit = {},
    onNavigateToAppSettings: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val repository = remember { TripRepository(context) }
    val trips by repository.trips.collectAsState(initial = emptyList())
    
    val totalTrips = trips.size
    val completedTrips = trips.count { it.status == TripStatus.COMPLETE }
    val upcomingTrips = trips.count { it.status == TripStatus.UPCOMING }
    val totalSpending = trips.sumOf { it.budgetTotal }
    
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
        ) {

            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .padding(24.dp)
            ) {
                Column {

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "Profile",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(24.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "?",
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }

                        Spacer(Modifier.width(16.dp))

                        Button(
                            onClick = { },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Text(
                                "Login",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.width(8.dp))

                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                            )
                        ) {
                            Text(
                                "Sign up",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.height(32.dp))
                }
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .offset(y = (-40).dp)
            ) {

                // Stats Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(totalTrips.toString(), "Total Trips", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                        StatItem(completedTrips.toString(), "Completed", MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
                        StatItem(upcomingTrips.toString(), "Upcoming", MaterialTheme.colorScheme.tertiary, Modifier.weight(1f))
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Spending Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column {
                            Text(
                                "Total Travel Spending",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "$$totalSpending",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Flight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Dark Mode
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    if (isDarkMode) Icons.Default.DarkMode else Icons.Default.WbSunny,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Column {
                                Text("Dark Mode")
                                Text(
                                    "Switch to dark theme",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = onDarkModeChange
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Settings Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {

                        SettingsItem(
                            icon = Icons.Default.Public,
                            iconTint = MaterialTheme.colorScheme.primary,
                            title = "Travel Preferences",
                            subtitle = "Currencies, units, languages",
                            onClick = onNavigateToTravelPreferences
                        )

                        HorizontalDivider()

                        SettingsItem(
                            icon = Icons.Default.Notifications,
                            iconTint = MaterialTheme.colorScheme.primary,
                            title = "Notifications",
                            subtitle = "Reminders, alerts, updates",
                            onClick = onNavigateToNotifications
                        )

                        HorizontalDivider()

                        SettingsItem(
                            icon = Icons.Default.Security,
                            iconTint = MaterialTheme.colorScheme.primary,
                            title = "Privacy & Security",
                            subtitle = "Password, 2FA, data",
                            onClick = onNavigateToPrivacy
                        )

                        HorizontalDivider()

                        SettingsItem(
                            icon = Icons.Default.Settings,
                            iconTint = MaterialTheme.colorScheme.primary,
                            title = "App Settings",
                            subtitle = "Theme, cache, about",
                            onClick = onNavigateToAppSettings
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Logout
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )

                        Spacer(Modifier.width(12.dp))

                        Text(
                            "Log Out",
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(Modifier.height(48.dp)) // Added more bottom spacing
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconTint.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint)
            }

            Spacer(Modifier.width(12.dp))

            Column {
                Text(title)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Icon(
            Icons.AutoMirrored.Filled.ArrowRight,
            contentDescription = null
        )
    }
}

@Composable
fun StatItem(value: String, label: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            value,
            color = color,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
