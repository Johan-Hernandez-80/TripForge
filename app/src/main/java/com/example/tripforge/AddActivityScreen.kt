package com.example.tripforge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tripforge.ui.components.LabeledField
import com.example.tripforge.ui.components.ScreenHeader

@Composable
fun AddActivityScreen(
    onBack: () -> Unit = { /* TODO placeholder */ },
    onSave: () -> Unit = { /* TODO placeholder */ }
) {
    var activityName by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }
    var time by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var cost by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        ScreenHeader(
            title = "Add Activity",
            subtitle = "Tokyo Adventure",
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

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            LabeledField(
                label = "Date",
                value = date,
                onValueChange = { date = it },
                placeholder = "dd/mm/yyyy",
                leadingIcon = Icons.Default.CalendarToday,
                modifier = Modifier.weight(1f)
            )

            LabeledField(
                label = "Time",
                value = time,
                onValueChange = { time = it },
                placeholder = "--:--",
                leadingIcon = Icons.Default.AccessTime,
                modifier = Modifier.weight(1f)
            )
        }

        LabeledField(
            label = "Description (optional)",
            value = description,
            onValueChange = { description = it },
            placeholder = "Add any details about this activity...",
            leadingIcon = null,
            singleLine = false
        )

        LabeledField(
            label = "Estimated Cost (optional)",
            value = cost,
            onValueChange = { cost = it },
            placeholder = "e.g., 50",
            leadingIcon = Icons.Default.AttachMoney
        )

        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
        ) {
            Text("Save Activity", color = Color.White)
        }
    }
}