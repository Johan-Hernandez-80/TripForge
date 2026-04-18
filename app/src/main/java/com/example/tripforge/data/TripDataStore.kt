package com.example.tripforge.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.tripforge.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "trip_forge_prefs")

class TripDataStore(private val context: Context) {

    companion object {
        val TRIPS_KEY = stringPreferencesKey("trips_list")
    }

    private val json = Json { 
        ignoreUnknownKeys = true 
        encodeDefaults = true
    }

    val tripsFlow: Flow<List<TripSummary>> = context.dataStore.data
        .map { preferences ->
            val tripsJson = preferences[TRIPS_KEY] ?: "[]"
            try {
                json.decodeFromString<List<TripSummary>>(tripsJson)
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
            preferences[TRIPS_KEY] = json.encodeToString(currentTrips)
        }
    }

    suspend fun addExpense(tripId: String, expense: ExpenseItem) {
        updateTrip(tripId) { it.copy(expenses = it.expenses + expense) }
    }

    suspend fun addActivity(tripId: String, day: Int, activity: ActivityItem) {
        updateTrip(tripId) { trip ->
            val newItinerary = trip.itinerary.toMutableList()
            val dayIndex = newItinerary.indexOfFirst { it.day == day }
            if (dayIndex != -1) {
                val currentDay = newItinerary[dayIndex]
                newItinerary[dayIndex] = currentDay.copy(activities = currentDay.activities + activity)
            } else {
                // Should ideally handle creating new day if it doesn't exist
                newItinerary.add(DayPlan(day, "Day $day", listOf(activity)))
            }
            trip.copy(itinerary = newItinerary)
        }
    }

    suspend fun addPackingItem(tripId: String, item: PackingItem) {
        updateTrip(tripId) { it.copy(packingList = it.packingList + item) }
    }
    
    suspend fun togglePackingItem(tripId: String, itemId: Int) {
        updateTrip(tripId) { trip ->
            trip.copy(packingList = trip.packingList.map { 
                if (it.id == itemId) it.copy(checked = !it.checked) else it 
            })
        }
    }

    private suspend fun updateTrip(tripId: String, update: (TripSummary) -> TripSummary) {
        context.dataStore.edit { preferences ->
            val currentTrips = getAllTrips().toMutableList()
            val index = currentTrips.indexOfFirst { it.id == tripId }
            if (index != -1) {
                currentTrips[index] = update(currentTrips[index])
                preferences[TRIPS_KEY] = json.encodeToString(currentTrips)
            }
        }
    }

    private suspend fun getAllTrips(): List<TripSummary> {
        val preferences = context.dataStore.data.first()
        val tripsJson = preferences[TRIPS_KEY] ?: "[]"
        return try {
            json.decodeFromString<List<TripSummary>>(tripsJson)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun deleteTrip(tripId: String) {
        context.dataStore.edit { preferences ->
            val currentTrips = getAllTrips().toMutableList()
            currentTrips.removeAll { it.id == tripId }
            preferences[TRIPS_KEY] = json.encodeToString(currentTrips)
        }
    }
}
