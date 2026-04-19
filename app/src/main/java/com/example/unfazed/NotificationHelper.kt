package com.example.unfazed

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import java.util.*

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "unfazed_notifications"
        const val CHANNEL_NAME = "Unfazed Alerts"
        const val NOTIFICATION_ID = 1001
    }

    init {
        createNotificationChannel()
        scheduleNotifications()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Important academic updates and reminders"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleNotifications() {
        scheduleSemesterNotification()
        scheduleRegistrationNotification()
        scheduleExamNotification()
        scheduleOpportunityNotification()
    }

    private fun scheduleSemesterNotification() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, Calendar.JANUARY)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
        }

        scheduleNotification(
            title = "📚 New Semester Starting Soon!",
            message = "Prepare for the upcoming semester. Check your roadmap and set goals!",
            timeInMillis = calendar.timeInMillis
        )
    }

    private fun scheduleRegistrationNotification() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, Calendar.FEBRUARY)
            set(Calendar.DAY_OF_MONTH, 15)
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
        }

        scheduleNotification(
            title = "✅ Registration Deadline Approaching",
            message = "Don't forget to register for next semester courses! Deadline in 2 weeks.",
            timeInMillis = calendar.timeInMillis
        )
    }

    private fun scheduleExamNotification() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, Calendar.APRIL)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
        }

        scheduleNotification(
            title = "📝 Semester Exams Starting Soon!",
            message = "Start your preparation. Check your personalized study plan!",
            timeInMillis = calendar.timeInMillis
        )
    }

    private fun scheduleOpportunityNotification() {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 3)
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 0)
        }

        scheduleNotification(
            title = "🎯 New Opportunities Available!",
            message = "Check out new internships and hackathons matching your profile.",
            timeInMillis = calendar.timeInMillis
        )
    }

    private fun scheduleNotification(title: String, message: String, timeInMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        }
    }

    fun showImmediateNotification(title: String, message: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify((System.currentTimeMillis() % 10000).toInt(), notification)
    }
}