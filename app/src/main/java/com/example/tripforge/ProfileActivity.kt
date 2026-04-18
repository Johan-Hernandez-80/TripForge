package com.example.tripforge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.tripforge.ui.theme.TripForgeTheme
import android.content.Intent

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripForgeTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ProfileScreen(
                        selectedTab = "profile",
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
                                "map" -> {
                                    startActivity(Intent(this, MapActivity::class.java))
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
