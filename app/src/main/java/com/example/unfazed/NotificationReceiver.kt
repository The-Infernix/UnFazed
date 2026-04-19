package com.example.unfazed

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Unfazed Update"
        val message = intent.getStringExtra("message") ?: "Check your personalized roadmap!"

        val notificationHelper = NotificationHelper(context)
        notificationHelper.showImmediateNotification(title, message)
    }
}