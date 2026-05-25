package com.example.tripforge.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.tripforge.data.PreferencesDataStore
import com.example.tripforge.data.NotificationWorker
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val preferencesDataStore = remember { PreferencesDataStore(context) }
    val scope = rememberCoroutineScope()

    val notificationsEnabled by preferencesDataStore.notificationsEnabledFlow.collectAsState(initial = true)
    val travelAlerts by preferencesDataStore.travelAlertsFlow.collectAsState(initial = true)
    val marketingEmails by preferencesDataStore.marketingEmailsFlow.collectAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Notifications") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Master Switch
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Enable Notifications", style = MaterialTheme.typography.titleMedium)
                        Text("Receive all trip notifications", style = MaterialTheme.typography.bodySmall)
                    }
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { newValue ->
                            scope.launch {
                                preferencesDataStore.setNotificationsEnabled(newValue)
                                if (newValue) {
                                    NotificationWorker.scheduleNotifications(context)
                                } else {
                                    NotificationWorker.cancelNotifications(context)
                                }
                            }
                        }
                    )
                }
            }

            if (notificationsEnabled) {
                Text(
                    "Trip Notifications",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                )

                NotificationTypeCard(
                    icon = Icons.Default.Check,
                    title = "Trip One Week Away",
                    description = "Get notified 7 days before your trip starts"
                )

                NotificationTypeCard(
                    icon = Icons.Default.Check,
                    title = "Trip Tomorrow",
                    description = "Reminder when your trip is starting tomorrow"
                )

                NotificationTypeCard(
                    icon = Icons.Default.Check,
                    title = "Packing Incomplete",
                    description = "Alert if packing list isn't complete before trip"
                )

                NotificationTypeCard(
                    icon = Icons.Default.Check,
                    title = "Activity Tomorrow",
                    description = "Reminder when a planned activity is tomorrow"
                )

                Spacer(modifier = Modifier.height(8.dp))
            }


            Button(
                onClick = {
                    val testWork = OneTimeWorkRequestBuilder<NotificationWorker>().build()
                    WorkManager.getInstance(context).enqueue(testWork)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Run notification check now")
            }

            // Travel Alerts
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Travel Alerts", style = MaterialTheme.typography.titleMedium)
                        Text("Flight delays, weather alerts", style = MaterialTheme.typography.bodySmall)
                    }
                    Switch(
                        checked = travelAlerts,
                        onCheckedChange = { newValue ->
                            scope.launch {
                                preferencesDataStore.setTravelAlerts(newValue)
                            }
                        },
                        enabled = notificationsEnabled
                    )
                }
            }

            // Marketing Emails
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Marketing Emails", style = MaterialTheme.typography.titleMedium)
                        Text("Travel tips, special offers", style = MaterialTheme.typography.bodySmall)
                    }
                    Switch(
                        checked = marketingEmails,
                        onCheckedChange = { newValue ->
                            scope.launch {
                                preferencesDataStore.setMarketingEmails(newValue)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationTypeCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
