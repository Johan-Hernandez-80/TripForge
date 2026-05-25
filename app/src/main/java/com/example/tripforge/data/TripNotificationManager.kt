package com.example.tripforge.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.tripforge.MainActivity
import com.example.tripforge.R

class TripNotificationManager(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Trip Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications about your trips and activities"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun notifyTripOneWeekAway(tripTitle: String) {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Trip Coming Up!")
            .setContentText("$tripTitle is coming up in 7 days")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getPendingIntent())
            .setAutoCancel(true)
            .build()

        notificationManager.notify(tripTitle.hashCode(), notification)
    }

    fun notifyTripOneDayAway(tripTitle: String) {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Trip Tomorrow!")
            .setContentText("$tripTitle starts tomorrow")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(getPendingIntent())
            .setAutoCancel(true)
            .build()

        notificationManager.notify(tripTitle.hashCode() + 1, notification)
    }

    fun notifyPackingIncomplete(tripTitle: String) {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Packing Incomplete")
            .setContentText("Your packing list for $tripTitle is not complete")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(getPendingIntent())
            .setAutoCancel(true)
            .build()

        notificationManager.notify(tripTitle.hashCode() + 2, notification)
    }

    fun notifyActivityOneDayAway(tripTitle: String, activityTitle: String) {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Activity Tomorrow!")
            .setContentText("$activityTitle tomorrow during $tripTitle")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(getPendingIntent())
            .setAutoCancel(true)
            .build()

        notificationManager.notify((tripTitle + activityTitle).hashCode(), notification)
    }

    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    companion object {
        private const val CHANNEL_ID = "trip_notifications"
    }
}
