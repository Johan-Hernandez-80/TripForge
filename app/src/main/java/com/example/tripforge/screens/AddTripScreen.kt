package com.example.tripforge.screens

import android.icu.text.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.tripforge.data.ImageService
import com.example.tripforge.data.TripRepository
import com.example.tripforge.model.TripStatus
import com.example.tripforge.model.TripSummary
import com.example.tripforge.ui.components.*
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import android.util.Log


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTripScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {},
    initialCountry: String? = null,
    initialCity: String? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { TripRepository(context) }
    val currentUser by repository.getCurrentUserFlow().collectAsState(initial = null)
    val dateFormatter = remember { DateFormat.getDateInstance() }

    var tripName by rememberSaveable { mutableStateOf("") }
    var budget by rememberSaveable { mutableStateOf("") }
    var selectedCountry by rememberSaveable { mutableStateOf(initialCountry ?: "") }
    var selectedCity by rememberSaveable { mutableStateOf(initialCity ?: "") }
    var startDate by rememberSaveable { mutableStateOf("") }
    var endDate by rememberSaveable { mutableStateOf("") }

    if (currentUser == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Login Required",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "You need to be logged in to create trips",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go Back")
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        ScreenHeader(
            title = "Plan New Trip",
            subtitle = "Create your adventure",
            onBack = onBack
        )

        LabeledField(
            label = "Trip Name",
            value = tripName,
            onValueChange = { tripName = it },
            placeholder = "e.g., Summer in Europe",
            leadingIcon = Icons.Default.Description,
            modifier = Modifier.fillMaxWidth()
        )

        LocationPickerFields(
            selectedCountry = selectedCountry,
            selectedCity = selectedCity,
            onCountryChange = { selectedCountry = it },
            onCityChange = { selectedCity = it },
            modifier = Modifier.fillMaxWidth()
        )

        DatePickerField(
            value = startDate,
            label = "Start Date",
            onDateSelected = { startDate = it },
            modifier = Modifier.fillMaxWidth()
        )

        DatePickerField(
            value = endDate,
            label = "End Date",
            onDateSelected = { endDate = it },
            modifier = Modifier.fillMaxWidth()
        )

        MoneyField(
            label = "Total Budget",
            value = budget,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) budget = newValue
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            onClick = {
                if (tripName.isNotBlank() && selectedCountry.isNotBlank() && selectedCity.isNotBlank()) {
                    scope.launch {
                        val location = "$selectedCity, $selectedCountry"
                        
                        // Fetch image from Pixabay in background
                        val imageUrl = try {
                            val imageService = ImageService()
                            val url = imageService.fetchImageUrl(selectedCountry) ?: ""
                            Log.d("TripForge", "Fetched image URL: '$url'") // ADD THIS
                            url
                        } catch (e: Exception) {
                            Log.e("TripForge", "Image fetch failed", e) // AND THIS
                            ""
                        }
                        
                        val newTrip = TripSummary(
                            id = UUID.randomUUID().toString(),
                            title = tripName,
                            location = location,
                            startDate = startDate,
                            endDate = endDate,
                            budgetTotal = budget.toIntOrNull() ?: 0,
                            imageUrl = imageUrl,
                            status = TripStatus.UPCOMING
                        )
                        repository.saveTrip(newTrip)
                        onSave()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Create Trip", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}