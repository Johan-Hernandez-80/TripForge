package com.example.tripforge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.tripforge.ui.theme.TripForgeTheme
import android.content.Intent

class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripForgeTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MapScreen(
                        selectedTab = "map",
                        onSelectTab = { tab ->
                            when (tab) {
                                "home" -> {
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                }
                                "trips" -> {
                                    startActivity(Intent(this, TripsActivity::class.java))
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
