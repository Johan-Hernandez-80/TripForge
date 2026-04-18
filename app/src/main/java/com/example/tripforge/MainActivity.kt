package com.example.tripforge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tripforge.ui.components.BottomNav
import com.example.tripforge.ui.theme.TripForgeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkMode by rememberSaveable { mutableStateOf(false) }
            
            TripForgeTheme(darkTheme = isDarkMode) {
                MainScreen(
                    isDarkMode = isDarkMode,
                    onDarkModeChange = { isDarkMode = it }
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // Only show bottom bar on main tabs
            val mainTabs = listOf("home", "trips", "map", "profile")
            if (currentDestination in mainTabs) {
                Surface(
                    tonalElevation = 0.dp,
                    shadowElevation = 16.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    BottomNav(
                        selected = currentDestination ?: "home",
                        onSelect = { route ->
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(navController) }
            composable("trips") {
                TripsScreen(
                    onTripClick = { navController.navigate("trip_details") }
                )
            }
            composable("map") { MapScreen() }
            composable("profile") { 
                ProfileScreen(
                    isDarkMode = isDarkMode,
                    onDarkModeChange = onDarkModeChange
                ) 
            }
            
            composable("add_trip") { 
                AddTripScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() }
                ) 
            }
            
            composable("trip_details") {
                TripDetailsScreen(
                    onBack = { navController.popBackStack() },
                    onOpenBudget = { navController.navigate("budget") },
                    onOpenItinerary = { navController.navigate("itinerary") },
                    onOpenPacking = { navController.navigate("packing_list") }
                )
            }
            
            composable("budget") {
                BudgetScreen(onBack = { navController.popBackStack() })
            }
            
            composable("itinerary") {
                ItineraryScreen(
                    onBack = { navController.popBackStack() },
                    onAddActivity = { navController.navigate("add_activity") }
                )
            }
            
            composable("add_activity") {
                AddActivityScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() }
                )
            }

            composable("packing_list") {
                PackingListScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
