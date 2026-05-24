package com.example.tripforge.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.first

class NotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val tripRepository = TripRepository(context)
            val preferencesDataStore = PreferencesDataStore(context)
            val notificationManager = TripNotificationManager(context)
            val dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)

            val notificationsEnabled = preferencesDataStore.notificationsEnabledFlow.first()
            if (!notificationsEnabled) {
                return Result.success()
            }

            val trips = tripRepository.trips.first()
            val today = Date()

            trips.forEach { trip ->
                try {
                    val startDate = dateFormatter.parse(trip.startDate)
                    val daysBetween = ((startDate.time - today.time) / (1000 * 60 * 60 * 24)).toInt()

                    when {
                        daysBetween == 7 -> {
                            notificationManager.notifyTripOneWeekAway(trip.title)
                        }
                        daysBetween == 1 -> {
                            notificationManager.notifyTripOneDayAway(trip.title)
                            val incompletePacking = trip.packingList.any { !it.checked }
                            if (incompletePacking) {
                                notificationManager.notifyPackingIncomplete(trip.title)
                            }
                        }
                    }

                    trip.itinerary.forEach { dayPlan ->
                        dayPlan.activities.forEach { activity ->
                            try {
                                if (activity.date.isNotEmpty()) {
                                    val activityDate = dateFormatter.parse(activity.date)
                                    val activityDaysBetween = ((activityDate.time - today.time) / (1000 * 60 * 60 * 24)).toInt()

                                    if (activityDaysBetween == 1) {
                                        notificationManager.notifyActivityOneDayAway(trip.title, activity.title)
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    tripRepository.saveTrip(trip, isEdit = true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    companion object {
        private const val NOTIFICATION_WORK_NAME = "tripforge_notification_work"

        fun scheduleNotifications(context: Context) {
            val notificationWork = PeriodicWorkRequestBuilder<NotificationWorker>(
                1, TimeUnit.HOURS
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                NOTIFICATION_WORK_NAME,
                androidx.work.ExistingPeriodicWorkPolicy.KEEP,
                notificationWork
            )
        }

        fun cancelNotifications(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(NOTIFICATION_WORK_NAME)
        }
    }
}