package com.example.tripforge.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripforge.data.LocationData
import java.text.DateFormat
import java.util.Date

@Composable
fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector? = null,
    singleLine: Boolean = true,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (leadingIcon != null) {
                Icon(
                    leadingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
    }
}
@Composable
fun MoneyField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Budget",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.all { it.isDigit() }) {
                onValueChange(newValue)
            }
        },
        label = { Text(label) },
        placeholder = { Text("0") },
        leadingIcon = {
            Icon(Icons.Default.AttachMoney, contentDescription = null)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * Two linked dropdown fields: Country → City.
 * Fetches countries from CountriesNow API on first load; falls back to hardcoded data on error.
 * When a country is selected, cities are fetched automatically (same fallback logic).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerFields(
    selectedCountry: String,
    selectedCity: String,
    onCountryChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var countries by remember { mutableStateOf<List<String>>(emptyList()) }
    var cities by remember { mutableStateOf<List<String>>(emptyList()) }
    var loadingCountries by remember { mutableStateOf(true) }
    var loadingCities by remember { mutableStateOf(false) }

    // Load countries once on first composition
    androidx.compose.runtime.LaunchedEffect(Unit) {
        countries = LocationData.fetchCountries()
        loadingCountries = false
    }

    // Reload cities whenever the selected country changes
    androidx.compose.runtime.LaunchedEffect(selectedCountry) {
        if (selectedCountry.isNotBlank()) {
            loadingCities = true
            cities = LocationData.fetchCitiesForCountry(selectedCountry)
            loadingCities = false
        } else {
            cities = emptyList()
        }
    }

    var countryExpanded by remember { mutableStateOf(false) }
    var cityExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier, verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(18.dp)) {
        // ── Country ──────────────────────────────────────────────────────────
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Flag, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Country", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = countryExpanded && !loadingCountries,
                onExpandedChange = { if (!loadingCountries) countryExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedCountry,
                    onValueChange = {},
                    readOnly = true,
                    enabled = !loadingCountries,
                    placeholder = { Text(if (loadingCountries) "Loading countries..." else "Select a country") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryExpanded && !loadingCountries) },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = countryExpanded,
                    onDismissRequest = { countryExpanded = false }
                ) {
                    countries.forEach { country ->
                        DropdownMenuItem(
                            text = { Text(country) },
                            onClick = {
                                onCountryChange(country)
                                onCityChange("")   // reset city when country changes
                                countryExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // ── City ─────────────────────────────────────────────────────────────
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationCity, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.width(8.dp))
                Text("City", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = cityExpanded && cities.isNotEmpty() && !loadingCities,
                onExpandedChange = { if (cities.isNotEmpty() && !loadingCities) cityExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedCity,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text(
                        when {
                            selectedCountry.isEmpty() -> "Select a country first"
                            loadingCities -> "Loading cities..."
                            else -> "Select a city"
                        }
                    ) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityExpanded && cities.isNotEmpty()) },
                    enabled = cities.isNotEmpty() && !loadingCities,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = cityExpanded && cities.isNotEmpty(),
                    onDismissRequest = { cityExpanded = false }
                ) {
                    cities.forEach { city ->
                        DropdownMenuItem(
                            text = { Text(city) },
                            onClick = {
                                onCityChange(city)
                                cityExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DateField(
    value: String,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        placeholder = { Text("dd/mm/yyyy") },
        readOnly = true,
        leadingIcon = {
            Icon(Icons.Default.CalendarToday, contentDescription = null)
        },
        trailingIcon = {
            IconButton(onClick = onClick) {
                Icon(Icons.Default.EditCalendar, contentDescription = null)
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeField(
    value: String,
    onTimeSelected: (String) -> Unit,
    label: String = "Time",
    modifier: Modifier = Modifier
) {
    val timePickerState = rememberTimePickerState()
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        TimePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val hour = timePickerState.hour
                        val minute = timePickerState.minute
                        val formatted = String.format("%02d:%02d", hour, minute)
                        onTimeSelected(formatted)
                        showDialog = false
                    }
                ) {
                    Text("OK")
                }
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        placeholder = { Text("--:--") },
        readOnly = true,
        leadingIcon = {
            Icon(Icons.Default.AccessTime, contentDescription = null)
        },
        trailingIcon = {
            IconButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Schedule, contentDescription = null)
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}