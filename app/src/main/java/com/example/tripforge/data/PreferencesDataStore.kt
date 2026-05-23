package com.example.tripforge.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Using a private extension property to ensure a single instance of DataStore

class PreferencesDataStore(context: Context) {
    private val appContext = context.applicationContext

    companion object {
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        private val USE_SYSTEM_THEME_KEY = booleanPreferencesKey("use_system_theme")
        private val NOTIFICATIONS_ENABLED_KEY = booleanPreferencesKey("notifications_enabled")
        private val TRAVEL_ALERTS_KEY = booleanPreferencesKey("travel_alerts")
        private val MARKETING_EMAILS_KEY = booleanPreferencesKey("marketing_emails")
    }

    val darkModeFlow: Flow<Boolean> = appContext.dataStore.data
        .map { preferences ->
            preferences[DARK_MODE_KEY] ?: false
        }

    val useSystemThemeFlow: Flow<Boolean> = appContext.dataStore.data
        .map { preferences ->
            preferences[USE_SYSTEM_THEME_KEY] ?: true
        }

    val notificationsEnabledFlow: Flow<Boolean> = appContext.dataStore.data
        .map { preferences ->
            preferences[NOTIFICATIONS_ENABLED_KEY] ?: true
        }

    val travelAlertsFlow: Flow<Boolean> = appContext.dataStore.data
        .map { preferences ->
            preferences[TRAVEL_ALERTS_KEY] ?: true
        }

    val marketingEmailsFlow: Flow<Boolean> = appContext.dataStore.data
        .map { preferences ->
            preferences[MARKETING_EMAILS_KEY] ?: false
        }

    suspend fun setDarkMode(enabled: Boolean) {
        appContext.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
            // If the user manually toggles dark mode, we disable following the system theme
            preferences[USE_SYSTEM_THEME_KEY] = false
        }
    }

    suspend fun setUseSystemTheme(enabled: Boolean) {
        appContext.dataStore.edit { preferences ->
            preferences[USE_SYSTEM_THEME_KEY] = enabled
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        appContext.dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED_KEY] = enabled
        }
    }

    suspend fun setTravelAlerts(enabled: Boolean) {
        appContext.dataStore.edit { preferences ->
            preferences[TRAVEL_ALERTS_KEY] = enabled
        }
    }

    suspend fun setMarketingEmails(enabled: Boolean) {
        appContext.dataStore.edit { preferences ->
            preferences[MARKETING_EMAILS_KEY] = enabled
        }
    }
}
