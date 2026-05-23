package com.example.tripforge.screens

import android.Manifest
import android.content.Context
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.content.pm.PackageManager
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.text.font.FontWeight

@Composable
fun MapScreen(
    selectedTab: String? = null,
    onSelectTab: ((String) -> Unit)? = null,
    navController: NavController? = null
) {
    val context = LocalContext.current
    val mapViewState = remember { mutableStateOf<MapView?>(null) }
    val userLocationState = remember { mutableStateOf<LatLng?>(null) }
    val selectedLocationState = remember { mutableStateOf<LatLng?>(null) }
    val selectedAddressState = remember { mutableStateOf<Pair<String, String>?>(null) }
    val showDialogState = remember { mutableStateOf(false) }
    val geocoder = remember { Geocoder(context) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val isOnline = remember { mutableStateOf(isInternetAvailable(context)) }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    userLocationState.value = LatLng(location.latitude, location.longitude)
                }
            }
        }
    }

    Scaffold(
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Search locations...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxSize()
                    .padding(bottom = 24.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(24.dp)
                    )
            ) {
                if (isOnline.value) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { context ->
                            MapView(context).apply {
                                mapViewState.value = this
                                onCreate(null)
                                getMapAsync { googleMap ->
                                    val initialLocation =
                                        userLocationState.value ?: LatLng(-33.852, 151.211)
                                    googleMap.moveCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                            initialLocation,
                                            12f
                                        )
                                    )

                                    googleMap.setOnMapClickListener { latLng ->
                                        selectedLocationState.value = latLng
                                        try {
                                            val addresses = geocoder.getFromLocation(
                                                latLng.latitude,
                                                latLng.longitude,
                                                1
                                            )
                                            if (!addresses.isNullOrEmpty()) {
                                                val address = addresses[0]
                                                val country = address.countryName ?: "Unknown"
                                                val city =
                                                    address.adminArea ?: address.locality
                                                    ?: "Unknown"
                                                selectedAddressState.value = Pair(country, city)
                                                showDialogState.value = true
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                }
                            }
                        },
                        update = { mapView ->
                            mapView.onResume()
                        }
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                            .background(
                                MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(16.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Text("Legend", style = MaterialTheme.typography.labelMedium)

                        Spacer(Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                Modifier
                                    .size(12.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(50)
                                    )
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Trip")
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                Modifier
                                    .size(12.dp)
                                    .background(Color(0xFFFF9800), RoundedCornerShape(50))
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Activity")
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WifiOff,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "connect to the internet to view map",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = { isOnline.value = isInternetAvailable(context) }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }

    if (showDialogState.value && selectedAddressState.value != null) {
        val (country, city) = selectedAddressState.value!!
        AlertDialog(
            onDismissRequest = { showDialogState.value = false },
            title = { Text("Create Trip") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Plan a trip to:")
                    Text(
                        "$city, $country",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        navController?.navigate("add_trip?country=$country&city=$city")
                        showDialogState.value = false
                    }
                ) {
                    Text("Plan Trip Here")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDialogState.value = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            mapViewState.value?.onDestroy()
        }
    }
}

private fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}
