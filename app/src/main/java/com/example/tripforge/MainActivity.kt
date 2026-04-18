package com.example.tripforge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tripforge.ui.theme.TripForgeTheme
import com.example.tripforge.ui.components.FeaturedTripCard
import com.example.tripforge.ui.components.TripCardBig
import com.example.tripforge.ui.components.TripCardLong

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripForgeTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    HomeScreen()
                }
            }
        }
    }
}

// --- 1. HOME SCREEN PREVIEW ---
@Preview(showBackground = true, name = "Home Screen", showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    TripForgeTheme {
        HomeScreen()
    }
}

// --- 2. TRIP DETAILS PREVIEW ---
@Preview(showBackground = true, name = "Trip Details", showSystemUi = true)
@Composable
fun TripDetailsPreview() {
    TripForgeTheme {
        TripDetailsScreen()
    }
}

// --- 3. ITINERARY PREVIEW ---
@Preview(showBackground = true, name = "Itinerary Screen", showSystemUi = true)
@Composable
fun ItineraryPreview() {
    TripForgeTheme {
        ItineraryScreen()
    }
}

// --- 4. BUDGET PREVIEW ---
@Preview(showBackground = true, name = "Budget Screen", showSystemUi = true)
@Composable
fun BudgetPreview() {
    TripForgeTheme {
        BudgetScreen()
    }
}

// --- 5. ADD ACTIVITY PREVIEW ---
@Preview(showBackground = true, name = "Add Activity Screen", showSystemUi = true)
@Composable
fun AddActivityPreview() {
    TripForgeTheme {
        AddActivityScreen()
    }
}

// --- 6. MAP PREVIEW ---
@Preview(showBackground = true, name = "Map Screen", showSystemUi = true)
@Composable
fun MapScreenPreview() {
    TripForgeTheme {
        MapScreen()
    }
}

// --- 6. PACKING LIST SCREEN PREVIEW ---
@Preview(showBackground = true, name = "Packing List Screen", showSystemUi = true)
@Composable
fun PackingListScreenPreview() {
    TripForgeTheme {
        PackingListScreen()
    }
}

// --- 7. TRIPS SCREEN PREVIEW ---
@Preview(showBackground = true, name = "Trips Screen", showSystemUi = true)
@Composable
fun TripsScreenPreview() {
    TripForgeTheme {
        TripsScreen()
    }
}