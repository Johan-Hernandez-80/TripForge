package com.example.tripforge.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.tripforge.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "trip_forge_prefs")

class TripDataStore(private val context: Context) {

    companion object {
        val TRIPS_KEY = stringPreferencesKey("trips_list")
    }

    private val gson = Gson()

    val tripsFlow: Flow<List<TripSummary>> = context.dataStore.data
        .map { preferences ->
            val tripsJson = preferences[TRIPS_KEY] ?: "[]"
            try {
                val type = object : TypeToken<List<TripSummary>>() {}.type
                gson.fromJson(tripsJson, type)
            } catch (e: Exception) {
                emptyList()
            }
        }

    suspend fun saveTrip(trip: TripSummary) {
        context.dataStore.edit { preferences ->
            val currentTrips = getAllTrips().toMutableList()
            val index = currentTrips.indexOfFirst { it.id == trip.id }
            if (index != -1) {
                currentTrips[index] = trip
            } else {
                currentTrips.add(trip)
            }
            preferences[TRIPS_KEY] = gson.toJson(currentTrips)
        }
    }

    suspend fun addExpense(tripId: String, expense: ExpenseItem) {
        updateTrip(tripId) { it.copy(expenses = it.expenses + expense) }
    }

    suspend fun updateExpense(tripId: String, expense: ExpenseItem) {
        updateTrip(tripId) { trip ->
            trip.copy(expenses = trip.expenses.map { 
                if (it.id == expense.id) expense else it 
            })
        }
    }

    suspend fun deleteExpense(tripId: String, expenseId: String) {
        updateTrip(tripId) { trip ->
            trip.copy(expenses = trip.expenses.filter { it.id != expenseId })
        }
    }

    suspend fun addActivity(tripId: String, day: Int, activity: ActivityItem) {
        updateTrip(tripId) { trip ->
            val newItinerary = trip.itinerary.toMutableList()
            val dayIndex = newItinerary.indexOfFirst { it.day == day }
            if (dayIndex != -1) {
                val currentDay = newItinerary[dayIndex]
                newItinerary[dayIndex] = currentDay.copy(activities = currentDay.activities + activity)
            } else {
                newItinerary.add(DayPlan(day, "Day $day", listOf(activity)))
            }
            trip.copy(itinerary = newItinerary)
        }
    }

    suspend fun updateActivity(tripId: String, activity: ActivityItem) {
        updateTrip(tripId) { trip ->
            trip.copy(itinerary = trip.itinerary.map { day ->
                day.copy(activities = day.activities.map { 
                    if (it.id == activity.id) activity else it 
                })
            })
        }
    }

    suspend fun deleteActivity(tripId: String, activityId: String) {
        updateTrip(tripId) { trip ->
            trip.copy(itinerary = trip.itinerary.map { day ->
                day.copy(activities = day.activities.filter { it.id != activityId })
            })
        }
    }

    suspend fun addPackingItem(tripId: String, item: PackingItem) {
        updateTrip(tripId) { it.copy(packingList = it.packingList + item) }
    }
    
    suspend fun updatePackingItem(tripId: String, item: PackingItem) {
        updateTrip(tripId) { trip ->
            trip.copy(packingList = trip.packingList.map { 
                if (it.id == item.id) item else it 
            })
        }
    }

    suspend fun togglePackingItem(tripId: String, itemId: Int) {
        updateTrip(tripId) { trip ->
            trip.copy(packingList = trip.packingList.map { 
                if (it.id == itemId) it.copy(checked = !it.checked) else it 
            })
        }
    }

    suspend fun deletePackingItem(tripId: String, itemId: Int) {
        updateTrip(tripId) { trip ->
            trip.copy(packingList = trip.packingList.filter { it.id != itemId })
        }
    }

    private suspend fun updateTrip(tripId: String, update: (TripSummary) -> TripSummary) {
        context.dataStore.edit { preferences ->
            val currentTrips = getAllTrips().toMutableList()
            val index = currentTrips.indexOfFirst { it.id == tripId }
            if (index != -1) {
                currentTrips[index] = update(currentTrips[index])
                preferences[TRIPS_KEY] = gson.toJson(currentTrips)
            }
        }
    }

    private suspend fun getAllTrips(): List<TripSummary> {
        val preferences = context.dataStore.data.first()
        val tripsJson = preferences[TRIPS_KEY] ?: "[]"
        return try {
            val type = object : TypeToken<List<TripSummary>>() {}.type
            gson.fromJson(tripsJson, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun deleteTrip(tripId: String) {
        context.dataStore.edit { preferences ->
            val currentTrips = getAllTrips().toMutableList()
            currentTrips.removeAll { it.id == tripId }
            preferences[TRIPS_KEY] = gson.toJson(currentTrips)
        }
    }
}
