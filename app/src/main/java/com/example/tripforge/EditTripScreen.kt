package com.example.tripforge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.tripforge.data.TripRepository
import com.example.tripforge.model.TripSummary
import com.example.tripforge.ui.components.LabeledField
import com.example.tripforge.ui.components.ScreenHeader
import kotlinx.coroutines.launch

@Composable
fun EditTripScreen(
    trip: TripSummary,
    onBack: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { TripRepository(context) }
    
    var tripName by rememberSaveable { mutableStateOf(trip.title) }
    var location by rememberSaveable { mutableStateOf(trip.location) }
    var startDate by rememberSaveable { mutableStateOf(trip.startDate) }
    var endDate by rememberSaveable { mutableStateOf(trip.endDate) }
    var budget by rememberSaveable { mutableStateOf(trip.budgetTotal.toString()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        ScreenHeader(
            title = "Edit Trip",
            subtitle = trip.title,
            onBack = onBack
        )

        LabeledField(
            label = "Trip Name",
            value = tripName,
            onValueChange = { tripName = it },
            placeholder = "e.g., Summer in Europe",
            leadingIcon = Icons.Default.Description
        )

        LabeledField(
            label = "Destination",
            value = location,
            onValueChange = { location = it },
            placeholder = "e.g., Paris, France",
            leadingIcon = Icons.Default.LocationOn
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            LabeledField(
                label = "Start Date",
                value = startDate,
                onValueChange = { startDate = it },
                placeholder = "dd/mm/yyyy",
                leadingIcon = Icons.Default.CalendarToday,
                modifier = Modifier.weight(1f)
            )

            LabeledField(
                label = "End Date",
                value = endDate,
                onValueChange = { endDate = it },
                placeholder = "dd/mm/yyyy",
                leadingIcon = Icons.Default.CalendarToday,
                modifier = Modifier.weight(1f)
            )
        }

        LabeledField(
            label = "Total Budget",
            value = budget,
            onValueChange = { budget = it },
            placeholder = "e.g., 2000",
            leadingIcon = Icons.Default.AttachMoney
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (tripName.isNotBlank() && location.isNotBlank()) {
                    scope.launch {
                        val updatedTrip = trip.copy(
                            title = tripName,
                            location = location,
                            startDate = startDate,
                            endDate = endDate,
                            budgetTotal = budget.toIntOrNull() ?: 0
                        )
                        repository.saveTrip(updatedTrip, isEdit = true)
                        onSave()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Save Changes", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
