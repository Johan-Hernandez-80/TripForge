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

@Composable
fun EditActivityScreen(
    tripId: String,
    activity: ActivityItem,
    onBack: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { TripRepository(context) }

    var activityName by rememberSaveable { mutableStateOf(activity.title) }
    var location by rememberSaveable { mutableStateOf(activity.location) }
    var date by rememberSaveable { mutableStateOf(activity.date) }
    var time by rememberSaveable { mutableStateOf(activity.time) }
    var description by rememberSaveable { mutableStateOf(activity.description) }
    var cost by rememberSaveable { mutableStateOf(activity.cost?.toString() ?: "") }
    var isCompleted by rememberSaveable { mutableStateOf(activity.completed) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        ScreenHeader(
            title = "Edit Activity",
            subtitle = "Update your activity details",
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

        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Mark as Completed",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Switch(
                    checked = isCompleted,
                    onCheckedChange = { isCompleted = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    if (activityName.isNotBlank() && date.isNotBlank()) {
                        scope.launch {
                            val updatedActivity = activity.copy(
                                title = activityName,
                                location = location,
                                time = time,
                                description = description,
                                cost = cost.toIntOrNull(),
                                date = date,
                                completed = isCompleted
                            )
                            repository.editActivity(tripId, updatedActivity)
                            onSave()
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save Changes", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
