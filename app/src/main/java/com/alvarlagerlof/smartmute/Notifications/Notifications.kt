package com.alvarlagerlof.smartmute.Notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.alvarlagerlof.smartmute.R


/**
 * Created by alvar on 2017-08-18.
 */

class Notifications(var context: Context) {

    fun sendDebug(id: Int, title: String, description: String) {
        val notificationBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setColor(ContextCompat.getColor(context, Color.RED))
                .setContentText(description)

        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("debug", "Debug", NotificationManager.IMPORTANCE_HIGH)
            channel.description = "Debug notifications"
            channel.enableLights(true)
            mNotificationManager.createNotificationChannel(channel)
            notificationBuilder.setChannelId("debug")
        }

        mNotificationManager.notify(id+1, notificationBuilder.build())
    }


    fun sendOngoing() {

        val turnOffPendingIntent = PendingIntent.getService(
                context,
                0,
                Intent(context, NotificationsService::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Device muted")
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setColorized(true)
                .setContentText("Smartmute turns off when device moves 25 meters")
                .addAction(android.R.drawable.ic_menu_delete, "Turn off now", turnOffPendingIntent)
                .setAutoCancel(true)
                .setOngoing(true)

        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("ongoing", "Currently muted", NotificationManager.IMPORTANCE_HIGH)
            channel.description = "Persistent notification while muted"
            channel.enableLights(true)
            mNotificationManager.createNotificationChannel(channel)
            notificationBuilder.setChannelId("ongoing")
        }

        mNotificationManager.notify(0, notificationBuilder.build())
    }


    fun sendTurnedOff() {
        val notificationBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Device unmuted")
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setTimeoutAfter(1000L*60*5)
                .setContentText("You moved 25 meters")

        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("turnedOff", "Mute turned off", NotificationManager.IMPORTANCE_HIGH)
            channel.description = "Notification when mute is turned off"
            channel.enableLights(true)
            mNotificationManager.createNotificationChannel(channel)
            notificationBuilder.setChannelId("turnedOff")
        }

        mNotificationManager.notify(0, notificationBuilder.build())
    }


}
