package com.example.tripforge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.tripforge.ui.theme.TripForgeTheme
import android.content.Intent

class TripsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripForgeTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    TripsScreen(
                        onTripClick = {
                            // In this Activity-based navigation, we might not have a details activity yet
                        },
                        selectedTab = "trips",
                        onSelectTab = { tab ->
                            when (tab) {
                                "home" -> {
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                }
                                "map" -> {
                                    startActivity(Intent(this, MapActivity::class.java))
                                    finish()
                                }
                                "profile" -> {
                                    startActivity(Intent(this, ProfileActivity::class.java))
                                    finish()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
