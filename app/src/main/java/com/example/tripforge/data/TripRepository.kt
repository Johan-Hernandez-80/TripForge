package com.example.tripforge.data

import android.content.Context
import android.widget.Toast
import com.example.tripforge.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class TripRepository(private val context: Context) {
    private val dataStore = TripDataStore(context)
    private val authStore = AuthenticationDataStore(context)
    private val dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)

    private fun calculateTripStatus(trip: TripSummary): TripSummary {
        return try {
            val startDate = dateFormatter.parse(trip.startDate)
            val endDate = dateFormatter.parse(trip.endDate)
            val today = Date()

            val status = when {
                today.before(startDate) -> TripStatus.UPCOMING
                !today.after(endDate) -> TripStatus.ONGOING
                else -> TripStatus.COMPLETE
            }

            trip.copy(status = status)
        } catch (e: Exception) {
            trip
        }
    }

    val trips: Flow<List<TripSummary>> = combine(
        dataStore.tripsFlow,
        authStore.currentUserFlow
    ) { allTrips, currentUser ->
        if (currentUser != null) {
            allTrips
                .filter { it.userId == currentUser.id }
                .map { calculateTripStatus(it) }
                .sortedByDescending { it.createdAt }
        } else {
            emptyList()
        }
    }

    suspend fun saveTrip(trip: TripSummary, isEdit: Boolean = false) {
        try {
            val currentUser = authStore.getCurrentUser()
            if (currentUser != null) {
                val tripWithUser = trip.copy(userId = currentUser.id)
                dataStore.saveTrip(tripWithUser)
                showToast(if (isEdit) "Trip updated successfully" else "Trip created successfully")
            } else {
                showToast("You must be logged in to save trips")
            }
        } catch (e: Exception) {
            showToast("Error saving trip: ${e.message}")
        }
    }

    suspend fun deleteTrip(tripId: String) {
        try {
            dataStore.deleteTrip(tripId)
            showToast("Trip deleted")
        } catch (e: Exception) {
            showToast("Error deleting trip")
        }
    }

    suspend fun addExpense(tripId: String, expense: ExpenseItem) {
        try {
            dataStore.addExpense(tripId, expense)
            showToast("Expense added")
        } catch (e: Exception) {
            showToast("Error adding expense")
        }
    }

    suspend fun deleteExpense(tripId: String, expenseId: String) {
        try {
            dataStore.deleteExpense(tripId, expenseId)
            showToast("Expense deleted")
        } catch (e: Exception) {
            showToast("Error deleting expense")
        }
    }

    suspend fun addActivity(tripId: String, day: Int, activity: ActivityItem) {
        try {
            dataStore.addActivity(tripId, day, activity)
            showToast("Activity added")
        } catch (e: Exception) {
            showToast("Error adding activity")
        }
    }

    suspend fun editActivity(tripId: String, activity: ActivityItem) {
        try {
            dataStore.updateActivity(tripId, activity)
            showToast("Activity updated")
        } catch (e: Exception) {
            showToast("Error updating activity")
        }
    }

    suspend fun toggleActivityCompletion(tripId: String, activityId: String) {
        try {
            dataStore.toggleActivityCompletion(tripId, activityId)
        } catch (e: Exception) {
            showToast("Error updating activity")
        }
    }

    suspend fun deleteActivity(tripId: String, activityId: String) {
        try {
            dataStore.deleteActivity(tripId, activityId)
            showToast("Activity removed")
        } catch (e: Exception) {
            showToast("Error removing activity")
        }
    }

    suspend fun addPackingItem(tripId: String, item: PackingItem) {
        try {
            dataStore.addPackingItem(tripId, item)
            showToast("Item added to packing list")
        } catch (e: Exception) {
            showToast("Error adding item")
        }
    }

    suspend fun togglePackingItem(tripId: String, itemId: Int) {
        try {
            dataStore.togglePackingItem(tripId, itemId)
        } catch (e: Exception) {
            showToast("Error updating item")
        }
    }

    suspend fun deletePackingItem(tripId: String, itemId: Int) {
        try {
            dataStore.deletePackingItem(tripId, itemId)
            showToast("Item removed from packing list")
        } catch (e: Exception) {
            showToast("Error removing item")
        }
    }

    suspend fun getCurrentUser(): User? = authStore.getCurrentUser()

    suspend fun logout() = authStore.logout()

    fun getCurrentUserFlow(): Flow<User?> = authStore.currentUserFlow

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}