package com.example.tripforge

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import android.Manifest
import android.os.Build
import com.example.tripforge.data.ImageService
import com.example.tripforge.data.PreferencesDataStore
import com.example.tripforge.data.TripRepository
import com.example.tripforge.data.AuthenticationDataStore
import com.example.tripforge.data.NotificationWorker
import com.example.tripforge.model.ActivityItem
import com.example.tripforge.model.TripSummary
import com.example.tripforge.screens.AddActivityScreen
import com.example.tripforge.screens.AddTripScreen
import com.example.tripforge.screens.AppSettingsScreen
import com.example.tripforge.screens.BudgetScreen
import com.example.tripforge.screens.EditActivityScreen
import com.example.tripforge.screens.EditTripScreen
import com.example.tripforge.screens.HomeScreen
import com.example.tripforge.screens.ItineraryScreen
import com.example.tripforge.screens.MapScreen
import com.example.tripforge.screens.NotificationsScreen
import com.example.tripforge.screens.PackingListScreen
import com.example.tripforge.screens.PrivacyScreen
import com.example.tripforge.screens.ProfileScreen
import com.example.tripforge.screens.TravelPreferencesScreen
import com.example.tripforge.screens.TripDetailsScreen
import com.example.tripforge.screens.LoginRegisterScreen
import com.example.tripforge.screens.TripsScreen
import com.example.tripforge.ui.components.BottomNav
import com.example.tripforge.ui.theme.TripForgeTheme
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val preferencesDataStore = remember { PreferencesDataStore(context) }
            val authStore = remember { AuthenticationDataStore(context) }
            val isDarkMode by preferencesDataStore.darkModeFlow.collectAsState(initial = false)
            val currentUser by authStore.currentUserFlow.collectAsState(initial = null)
            val scope = rememberCoroutineScope()

            val imageService = remember { ImageService() }
            val tripRepository = remember { TripRepository(context) }
            val trips by tripRepository.trips.collectAsState(initial = emptyList())

            val notificationPermissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { _ ->
                scope.launch {
                    NotificationWorker.scheduleNotifications(context)
                }
            }

            LaunchedEffect(Unit) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    NotificationWorker.scheduleNotifications(context)
                }
            }

            LaunchedEffect(trips.isNotEmpty()) {
                if (trips.isNotEmpty()) {
                    imageService.preloadTripImages(context, trips) { updatedTrip ->
                        tripRepository.saveTrip(updatedTrip, isEdit = true)
                    }
                }
            }

            TripForgeTheme(darkTheme = isDarkMode) {
                if (currentUser == null) {
                    LoginRegisterScreen(onAuthSuccess = {})
                } else {
                    MainScreen(
                        isDarkMode = isDarkMode,
                        onDarkModeChange = { newValue ->
                            scope.launch {
                                preferencesDataStore.setDarkMode(newValue)
                            }
                        },
                        tripRepository = tripRepository,
                        onLogout = {
                            scope.launch {
                                authStore.logout()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    tripRepository: TripRepository? = null,
    onLogout: () -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val gson = remember { Gson() }

    Scaffold(
        bottomBar = {
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
            composable("home") {
                HomeScreen(
                    onNewTrip = { navController.navigate("add_trip") },
                    onViewAll = { navController.navigate("trips") },
                    onOpenDetails = { navController.navigate("trips") },
                    onOpenTrip = { trip ->                                // ← NUEVO
                        val tripJson = Uri.encode(gson.toJson(trip))
                        navController.navigate("trip_details/$tripJson")
                    },
                    selectedTab = "home",
                    onSelectTab = { route ->
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

            composable("trips") {
                TripsScreen(
                    onTripClick = { trip ->
                        val tripJson = Uri.encode(gson.toJson(trip))
                        navController.navigate("trip_details/$tripJson")
                    },
                    onEditTrip = { trip ->
                        val tripJson = Uri.encode(gson.toJson(trip))
                        navController.navigate("edit_trip/$tripJson")
                    },
                    selectedTab = "trips",
                    onSelectTab = { route ->
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
            composable("map") { MapScreen(navController = navController) }
            composable("profile") {
                ProfileScreen(
                    isDarkMode = isDarkMode,
                    onDarkModeChange = onDarkModeChange,
                    onNavigateToTravelPreferences = { navController.navigate("travel_preferences") },
                    onNavigateToNotifications = { navController.navigate("notifications") },
                    onNavigateToPrivacy = { navController.navigate("privacy") },
                    onNavigateToAppSettings = { navController.navigate("app_settings") },
                    onLogout = onLogout
                )
            }

            composable("add_trip") {
                AddTripScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() }
                )
            }

            composable(
                route = "add_trip?country={country}&city={city}",
                arguments = listOf(
                    navArgument("country") { type = NavType.StringType; defaultValue = "" },
                    navArgument("city") { type = NavType.StringType; defaultValue = "" }
                )
            ) { backStackEntry ->
                val country = backStackEntry.arguments?.getString("country") ?: ""
                val city = backStackEntry.arguments?.getString("city") ?: ""
                AddTripScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() },
                    initialCountry = if (country.isNotBlank()) country else null,
                    initialCity = if (city.isNotBlank()) city else null
                )
            }

            composable(
                route = "edit_trip/{tripJson}",
                arguments = listOf(navArgument("tripJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val tripJson = backStackEntry.arguments?.getString("tripJson")
                val trip = tripJson?.let { gson.fromJson(Uri.decode(it), TripSummary::class.java) }
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
                val trip = tripJson?.let { gson.fromJson(Uri.decode(it), TripSummary::class.java) }
                if (trip != null) {
                    TripDetailsScreen(
                        trip = trip,
                        onBack = { navController.popBackStack() },
                        onEdit = {
                            val encoded = Uri.encode(gson.toJson(trip))
                            navController.navigate("edit_trip/$encoded")
                        },
                        onOpenBudget = {
                            val encoded = Uri.encode(gson.toJson(trip))
                            navController.navigate("budget/$encoded")
                        },
                        onOpenItinerary = {
                            val encoded = Uri.encode(gson.toJson(trip))
                            navController.navigate("itinerary/$encoded")
                        },
                        onOpenPacking = {
                            val encoded = Uri.encode(gson.toJson(trip))
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
                val trip = tripJson?.let { gson.fromJson(Uri.decode(it), TripSummary::class.java) }
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
                val trip = tripJson?.let { gson.fromJson(Uri.decode(it), TripSummary::class.java) }
                if (trip != null) {
                    ItineraryScreen(
                        tripId = trip.id,
                        onBack = { navController.popBackStack() },
                        onAddActivity = {
                            navController.navigate("add_activity/${trip.id}")
                        },
                        onEditActivity = { activity ->
                            val activityJson = Uri.encode(gson.toJson(activity))
                            navController.navigate("edit_activity/${trip.id}/$activityJson")
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
                route = "edit_activity/{tripId}/{activityJson}",
                arguments = listOf(
                    navArgument("tripId") { type = NavType.StringType },
                    navArgument("activityJson") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                val activityJson = backStackEntry.arguments?.getString("activityJson")
                val activity = activityJson?.let { gson.fromJson(Uri.decode(it), ActivityItem::class.java) }
                if (activity != null) {
                    EditActivityScreen(
                        tripId = tripId,
                        activity = activity,
                        onBack = { navController.popBackStack() },
                        onSave = { navController.popBackStack() }
                    )
                }
            }

            composable(
                route = "packing_list/{tripJson}",
                arguments = listOf(navArgument("tripJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val tripJson = backStackEntry.arguments?.getString("tripJson")
                val trip = tripJson?.let { gson.fromJson(Uri.decode(it), TripSummary::class.java) }
                if (trip != null) {
                    PackingListScreen(
                        tripId = trip.id,
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            composable("travel_preferences") {
                TravelPreferencesScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable("notifications") {
                NotificationsScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable("privacy") {
                PrivacyScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable("app_settings") {
                AppSettingsScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
