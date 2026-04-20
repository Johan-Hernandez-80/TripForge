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
import com.example.tripforge.model.TripSummary
import com.example.tripforge.ui.components.BottomNav
import com.example.tripforge.ui.theme.TripForgeTheme
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import android.net.Uri
import androidx.navigation.NavType
import androidx.navigation.navArgument

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
                    onTripClick = { trip ->
                        val tripJson = Uri.encode(Json.encodeToString(trip))
                        navController.navigate("trip_details/$tripJson")
                    },
                    onEditTrip = { trip ->
                        val tripJson = Uri.encode(Json.encodeToString(trip))
                        navController.navigate("edit_trip/$tripJson")
                    }
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

            composable(
                route = "edit_trip/{tripJson}",
                arguments = listOf(navArgument("tripJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val tripJson = backStackEntry.arguments?.getString("tripJson")
                val trip = tripJson?.let { Json.decodeFromString<TripSummary>(Uri.decode(it)) }
                if (trip != null) {
                    EditTripScreen(
                        trip = trip,
                        onBack = { navController.popBackStack() },
                        onSave = { navController.popBackStack() }
                    )
                }
            }
            
            composable(
                route = "trip_details/{tripJson}",
                arguments = listOf(navArgument("tripJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val tripJson = backStackEntry.arguments?.getString("tripJson")
                val trip = tripJson?.let { Json.decodeFromString<TripSummary>(Uri.decode(it)) }
                if (trip != null) {
                    TripDetailsScreen(
                        trip = trip,
                        onBack = { navController.popBackStack() },
                        onEdit = {
                            val encoded = Uri.encode(Json.encodeToString(trip))
                            navController.navigate("edit_trip/$encoded")
                        },
                        onOpenBudget = { 
                            val encoded = Uri.encode(Json.encodeToString(trip))
                            navController.navigate("budget/$encoded") 
                        },
                        onOpenItinerary = { 
                            val encoded = Uri.encode(Json.encodeToString(trip))
                            navController.navigate("itinerary/$encoded") 
                        },
                        onOpenPacking = { 
                            val encoded = Uri.encode(Json.encodeToString(trip))
                            navController.navigate("packing_list/$encoded") 
                        }
                    )
                }
            }
            
            composable(
                route = "budget/{tripJson}",
                arguments = listOf(navArgument("tripJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val tripJson = backStackEntry.arguments?.getString("tripJson")
                val trip = tripJson?.let { Json.decodeFromString<TripSummary>(Uri.decode(it)) }
                if (trip != null) {
                    BudgetScreen(
                        tripId = trip.id,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
            
            composable(
                route = "itinerary/{tripJson}",
                arguments = listOf(navArgument("tripJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val tripJson = backStackEntry.arguments?.getString("tripJson")
                val trip = tripJson?.let { Json.decodeFromString<TripSummary>(Uri.decode(it)) }
                if (trip != null) {
                    ItineraryScreen(
                        tripId = trip.id,
                        onBack = { navController.popBackStack() },
                        onAddActivity = { 
                            navController.navigate("add_activity/${trip.id}") 
                        }
                    )
                }
            }
            
            composable(
                route = "add_activity/{tripId}",
                arguments = listOf(navArgument("tripId") { type = NavType.StringType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                AddActivityScreen(
                    tripId = tripId,
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() }
                )
            }

            composable(
                route = "packing_list/{tripJson}",
                arguments = listOf(navArgument("tripJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val tripJson = backStackEntry.arguments?.getString("tripJson")
                val trip = tripJson?.let { Json.decodeFromString<TripSummary>(Uri.decode(it)) }
                if (trip != null) {
                    PackingListScreen(
                        tripId = trip.id,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
