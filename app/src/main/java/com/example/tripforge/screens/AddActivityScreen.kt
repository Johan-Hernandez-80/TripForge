package com.example.tripforge.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.tripforge.data.TripRepository
import com.example.tripforge.model.ActivityItem
import com.example.tripforge.ui.components.DatePickerField
import com.example.tripforge.ui.components.LabeledField
import com.example.tripforge.ui.components.MoneyField
import com.example.tripforge.ui.components.ScreenHeader
import com.example.tripforge.ui.components.TimeField
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun AddActivityScreen(
    tripId: String,
    onBack: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { TripRepository(context) }

    var activityName by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }
    var time by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var cost by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        ScreenHeader(
            title = "Add Activity",
            subtitle = "Plan your next adventure",
            onBack = onBack
        )

        LabeledField(
            label = "Activity Name",
            value = activityName,
            onValueChange = { activityName = it },
            placeholder = "e.g., Visit Eiffel Tower",
            leadingIcon = Icons.Default.Description
        )

        LabeledField(
            label = "Location",
            value = location,
            onValueChange = { location = it },
            placeholder = "e.g., Champ de Mars, Paris",
            leadingIcon = Icons.Default.LocationOn
        )

        DatePickerField(
            value = date,
            label = "Date",
            onDateSelected = { date = it },
            modifier = Modifier.fillMaxWidth()
        )

        TimeField(
            value = time,
            onTimeSelected = { time = it },
            label = "Time",
            modifier = Modifier.fillMaxWidth()
        )

        LabeledField(
            label = "Description (optional)",
            value = description,
            onValueChange = { description = it },
            placeholder = "Add any details about this activity...",
            leadingIcon = null,
            singleLine = false
        )

        MoneyField(
            value = cost,
            onValueChange = { cost = it },
            modifier = Modifier.fillMaxWidth(),
            label = "Estimated Cost (optional)"
        )

        Button(
            onClick = {
                if (activityName.isNotBlank()) {
                    scope.launch {
                        val newActivity = ActivityItem(
                            id = UUID.randomUUID().toString(),
                            title = activityName,
                            location = location,
                            time = time,
                            description = description,
                            cost = cost.toIntOrNull()
                        )
                        // Using tripId from parameters instead of hardcoded value
                        repository.addActivity(tripId, 1, newActivity)
                        onSave()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Save Activity", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
