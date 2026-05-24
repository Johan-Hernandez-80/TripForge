package com.example.tripforge.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.tripforge.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID

val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "trip_forge_auth")

class AuthenticationDataStore(private val context: Context) {

    companion object {
        val USERS_KEY = stringPreferencesKey("users_list")
        val CURRENT_USER_ID_KEY = stringPreferencesKey("current_user_id")
    }

    private val gson = Gson()

    val currentUserFlow: Flow<User?> = context.authDataStore.data
        .map { preferences ->
            val userId = preferences[CURRENT_USER_ID_KEY]?.takeIf { it.isNotEmpty() }
            if (userId != null) {
                getUserById(userId)
            } else {
                null
            }
        }

    private suspend fun getAllUsers(): List<User> {
        return try {
            val data = context.authDataStore.data.first()
            val usersJson = data[USERS_KEY] ?: "[]"
            val type = object : TypeToken<List<User>>() {}.type
            gson.fromJson(usersJson, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun getUserById(userId: String): User? {
        val users = getAllUsers()
        return users.find { it.id == userId }
    }

    suspend fun register(email: String, password: String, name: String): Result<User> {
        return try {
            val users = getAllUsers()
            
            if (users.any { it.email == email }) {
                Result.failure(Exception("Email already registered"))
            } else if (password.length < 6) {
                Result.failure(Exception("Password must be at least 6 characters"))
            } else if (name.isBlank()) {
                Result.failure(Exception("Name cannot be empty"))
            } else {
                val newUser = User(
                    id = UUID.randomUUID().toString(),
                    email = email,
                    password = password,
                    name = name,
                    createdAt = System.currentTimeMillis()
                )
                
                val updatedUsers = users + newUser
                context.authDataStore.edit { preferences ->
                    preferences[USERS_KEY] = gson.toJson(updatedUsers)
                    preferences[CURRENT_USER_ID_KEY] = newUser.id
                }
                
                Result.success(newUser)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val users = getAllUsers()
            val user = users.find { it.email == email && it.password == password }
            
            if (user != null) {
                context.authDataStore.edit { preferences ->
                    preferences[CURRENT_USER_ID_KEY] = user.id
                }
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid email or password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        try {
            context.authDataStore.edit { preferences ->
                preferences[CURRENT_USER_ID_KEY] = ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getCurrentUser(): User? {
        return try {
            val data = context.authDataStore.data.first()
            val userId = data[CURRENT_USER_ID_KEY]?.takeIf { it.isNotEmpty() }
            if (userId != null) {
                getUserById(userId)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun isUserLoggedIn(): Boolean {
        return getCurrentUser() != null
    }
}
