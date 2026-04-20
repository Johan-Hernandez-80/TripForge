package com.example.tripforge.data

import android.content.Context
import android.widget.Toast
import com.example.tripforge.model.*
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class TripRepository(private val context: Context) {
    private val dataStore = TripDataStore(context)

    val trips: Flow<List<TripSummary>> = dataStore.tripsFlow

    suspend fun saveTrip(trip: TripSummary, isEdit: Boolean = false) {
        try {
            dataStore.saveTrip(trip)
            showToast(if (isEdit) "Trip updated successfully" else "Trip created successfully")
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

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
