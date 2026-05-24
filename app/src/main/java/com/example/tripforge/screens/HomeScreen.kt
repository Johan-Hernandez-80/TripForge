package com.example.tripforge.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tripforge.data.TripRepository
import com.example.tripforge.model.BudgetCategory
import com.example.tripforge.model.TripStatus
import com.example.tripforge.model.TripSummary
import com.example.tripforge.model.User
import com.example.tripforge.ui.theme.*
import java.text.DateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

@Composable
fun HomeScreen(
    onNewTrip: () -> Unit,
    onViewAll: () -> Unit,
    onOpenDetails: () -> Unit,                         // kept for backward compat
    onOpenTrip: (TripSummary) -> Unit = {},            // new: open a specific trip
    selectedTab: String,
    onSelectTab: (String) -> Unit
) {
    val context = LocalContext.current
    val repository = remember { TripRepository(context) }
    val trips by repository.trips.collectAsState(initial = emptyList())
    val currentUser by repository.getCurrentUserFlow().collectAsState(initial = null)
    val scrollState = rememberScrollState()

    // Next upcoming or currently ongoing trip
    val nextTrip = trips
        .filter { it.status == TripStatus.UPCOMING || it.status == TripStatus.ONGOING }
        .minByOrNull { it.startDate }

    // Global stats derived from all real trip data
    val totalTrips = trips.size
    val completedTrips = trips.count { it.status == TripStatus.COMPLETE }
    val totalSpent = trips.sumOf { trip -> trip.expenses.sumOf { it.amount } }
    val activitiesDone = trips.sumOf { trip ->
        trip.itinerary.sumOf { day -> day.activities.count { it.completed } }
    }
    val favoriteCategory = trips
        .flatMap { it.expenses }
        .groupBy { it.category }
        .maxByOrNull { entry -> entry.value.sumOf { it.amount } }
        ?.key

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
        ) {
            HomeHeader(
                user = currentUser,
                onNewTrip = onNewTrip,
                onViewAll = onViewAll
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // ── Section 1: Next / ongoing trip ──────────────────────
                Text(
                    text = if (nextTrip?.status == TripStatus.ONGOING) "Ongoing trip" else "Next trip",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))

                if (nextTrip != null) {
                    NextTripCard(
                        trip = nextTrip,
                        onClick = { onOpenTrip(nextTrip) }
                    )
                } else {
                    EmptyNextTripCard(onNewTrip = onNewTrip)
                }

                // ── Section 2: Travel stats (only when there is data) ───
                if (totalTrips > 0) {
                    Spacer(modifier = Modifier.height(28.dp))
                    SectionHeader(title = "Your travel stats")
                    Spacer(modifier = Modifier.height(12.dp))
                    StatsGrid(
                        totalTrips = totalTrips,
                        completedTrips = completedTrips,
                        totalSpent = totalSpent,
                        activitiesDone = activitiesDone,
                        favoriteCategory = favoriteCategory
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun HomeHeader(user: User?, onNewTrip: () -> Unit, onViewAll: () -> Unit) {
    val firstName = user?.name?.split(" ")?.firstOrNull() ?: "traveler"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 32.dp)
    ) {
        Column {
            Text(
                text = "Hello, $firstName ✈️",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ready for your next adventure?",
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onNewTrip,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "New Trip",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                OutlinedButton(
                    onClick = onViewAll,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                    )
                ) {
                    Text(
                        "View All",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun NextTripCard(trip: TripSummary, onClick: () -> Unit) {
    val dateFormatter = remember { DateFormat.getDateInstance() }

    val daysUntil = remember(trip.startDate, trip.status) {
        if (trip.status == TripStatus.ONGOING) return@remember 0L
        try {
            val start = dateFormatter.parse(trip.startDate)!!
            val diff = start.time - Date().time
            TimeUnit.MILLISECONDS.toDays(diff).coerceAtLeast(0)
        } catch (e: Exception) {
            0L
        }
    }

    val isOngoing = trip.status == TripStatus.ONGOING
    val totalSpentOnTrip = trip.expenses.sumOf { it.amount }
    val budgetProgress =
        if (trip.budgetTotal > 0) totalSpentOnTrip.toFloat() / trip.budgetTotal else 0f
    val packedItems = trip.packingList.count { it.checked }
    val totalPackItems = trip.packingList.size
    val completedActivities = trip.itinerary.sumOf { d -> d.activities.count { it.completed } }
    val totalActivities = trip.itinerary.sumOf { it.activities.size }
    val nextPendingActivity =
        trip.itinerary.flatMap { it.activities }.firstOrNull { !it.completed }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // Title row + countdown badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isOngoing)
                            SuccessGreen.copy(alpha = 0.12f)
                        else
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                    ) {
                        Text(
                            text = if (isOngoing) "● Ongoing" else "Upcoming",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isOngoing) SuccessGreen else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = trip.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = trip.location,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                // Countdown badge (only for upcoming)
                if (!isOngoing) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$daysUntil",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = if (daysUntil == 1L) "day" else "days",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            HorizontalDivider(color = GrayBorder)
            Spacer(Modifier.height(16.dp))

            // Mini stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (trip.budgetTotal > 0) {
                    TripMiniStat(
                        label = "Budget",
                        value = "${(budgetProgress * 100).toInt()}%",
                        icon = Icons.Default.AccountBalanceWallet,
                        color = when {
                            budgetProgress >= 1f -> ErrorRed
                            budgetProgress >= 0.8f -> WarningAmber
                            else -> SuccessGreen
                        }
                    )
                }
                if (totalPackItems > 0) {
                    TripMiniStat(
                        label = "Packing",
                        value = "$packedItems/$totalPackItems",
                        icon = Icons.Default.Work,
                        color = TealSecondary
                    )
                }
                if (totalActivities > 0) {
                    TripMiniStat(
                        label = "Activities",
                        value = "$completedActivities/$totalActivities",
                        icon = Icons.Default.CheckCircle,
                        color = OrangeAccent
                    )
                }
            }

            // Next pending activity hint
            if (nextPendingActivity != null) {
                Spacer(Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = LightGrayBackground,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            Icons.Default.Event,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Column {
                            Text(
                                "Next pending activity",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                            Text(
                                nextPendingActivity.title,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TripMiniStat(label: String, value: String, icon: ImageVector, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}

@Composable
fun EmptyNextTripCard(onNewTrip: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.FlightTakeoff,
                contentDescription = null,
                tint = GrayBorder,
                modifier = Modifier.size(48.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "No upcoming trips",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Start planning your next adventure",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = onNewTrip,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Create a trip")
            }
        }
    }
}

@Composable
fun StatsGrid(
    totalTrips: Int,
    completedTrips: Int,
    totalSpent: Int,
    activitiesDone: Int,
    favoriteCategory: BudgetCategory?
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Total trips",
                value = "$totalTrips",
                icon = Icons.Default.FlightTakeoff,
                color = BluePrimary
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Completed",
                value = "$completedTrips",
                icon = Icons.Default.CheckCircle,
                color = SuccessGreen
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Total spent",
                value = "$$totalSpent",
                icon = Icons.Default.AccountBalanceWallet,
                color = OrangeAccent
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Activities done",
                value = "$activitiesDone",
                icon = Icons.Default.Star,
                color = TealSecondary
            )
        }
        if (favoriteCategory != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = WarningAmber.copy(alpha = 0.14f)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = WarningAmber,
                            modifier = Modifier
                                .padding(10.dp)
                                .size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            "Top expense category",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Text(
                            favoriteCategory.label,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = color.copy(alpha = 0.12f)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(18.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun SectionHeader(title: String, hasSeeAll: Boolean = false, onSeeAll: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        if (hasSeeAll) {
            TextButton(onClick = onSeeAll) {
                Text("See all", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}