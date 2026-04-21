package com.example.tripforge.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.tripforge.ui.theme.TripForgeTheme
import com.example.tripforge.model.TripSummary
import com.example.tripforge.screens.EditTripScreen
import com.google.gson.Gson

class EditTripActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val tripJson = intent.getStringExtra("trip_json")
        val trip = tripJson?.let { Gson().fromJson(it, TripSummary::class.java) }
        
        if (trip == null) {
            finish()
            return
        }

        setContent {
            TripForgeTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    EditTripScreen(
                        trip = trip,
                        onBack = { finish() },
                        onSave = { finish() }
                    )
                }
            }
        }
    }
}
