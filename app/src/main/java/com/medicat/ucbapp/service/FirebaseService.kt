package com.medicat.ucbapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.medicat.framework.R
import com.medicat.ucbapp.MainActivity

class FirebaseService : FirebaseMessagingService() {
    companion object {
        val TAG = FirebaseService::class.java.simpleName
        private const val CHANNEL_ID = "competition_notifications"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")
        Log.d(TAG, "Message received!")

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
        }

        remoteMessage.notification?.let { notification ->
            Log.d(TAG, "Message Notification Title: ${notification.title}")
            Log.d(TAG, "Message Notification Body: ${notification.body}")
        }

        MainActivity.unreadNotificationsCount.value += 1
        createNotificationChannel()

        showNotification(
            title = remoteMessage.notification?.title ?: "Nueva notificación",
            body = remoteMessage.notification?.body ?: "Tienes una nueva notificación",
            data = remoteMessage.data,
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                val channel =
                    NotificationChannel(
                        CHANNEL_ID,
                        "Notificaciones de Competencias",
                        NotificationManager.IMPORTANCE_HIGH,
                    ).apply {
                        description = "Notificaciones sobre fechas límite de competencias"
                        enableLights(true)
                        enableVibration(true)
                        setShowBadge(true)
                    }
                notificationManager.createNotificationChannel(channel)
                Log.d(TAG, "Notification channel created: $CHANNEL_ID")
            }
        }
    }

    private fun showNotification(
        title: String,
        body: String,
        data: Map<String, String> = emptyMap(),
    ) {
        Log.d(TAG, "Showing notification: $title - $body")

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val intent =
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                data.forEach { (key, value) ->
                    putExtra(key, value)
                }
            }

        val pendingIntent =
            PendingIntent.getActivity(
                this,
                System.currentTimeMillis().toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        val notification =
            NotificationCompat
                .Builder(this, CHANNEL_ID)
                .setSmallIcon(com.medicat.ucbapp.R.drawable.logo)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .build()

        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notification)
        Log.d(TAG, "Notification displayed with ID: $notificationId")
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // Aquí deberías enviar el token a tu servidor
        // sendRegistrationToServer(token)
    }
}
