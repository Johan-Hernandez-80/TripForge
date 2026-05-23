package com.example.tripforge.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.tripforge.data.PreferencesDataStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettingsScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val preferencesDataStore = remember { PreferencesDataStore(context) }
    val isDarkMode by preferencesDataStore.darkModeFlow.collectAsState(initial = false)
    val scope = rememberCoroutineScope()

    var enableAnimations by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("App Settings") },
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
            Text("Display", style = MaterialTheme.typography.titleMedium)

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
                        Text("Dark Mode", style = MaterialTheme.typography.titleMedium)
                        Text("Switch to dark theme", style = MaterialTheme.typography.bodySmall)
                    }
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { newValue ->
                            scope.launch {
                                preferencesDataStore.setDarkMode(newValue)
                            }
                        }
                    )
                }
            }

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
                        Text("Enable Animations", style = MaterialTheme.typography.titleMedium)
                        Text("Smooth UI transitions", style = MaterialTheme.typography.bodySmall)
                    }
                    Switch(
                        checked = enableAnimations,
                        onCheckedChange = { enableAnimations = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Storage", style = MaterialTheme.typography.titleMedium)

            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("App Cache", style = MaterialTheme.typography.titleMedium)
                            Text("2.3 MB", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Clear Cache")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("About", style = MaterialTheme.typography.titleMedium)

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
                        Text("App Version", style = MaterialTheme.typography.titleMedium)
                        Text("1.0.0", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
