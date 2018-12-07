package com.alvarlagerlof.smartmute

import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.os.Build
import com.alvarlagerlof.smartmute.Geofence.GeofenceHandler
import com.alvarlagerlof.smartmute.Notifications.Notifications
import com.alvarlagerlof.smartmute.Tile.TileUpdateEvent
import org.greenrobot.eventbus.EventBus


/**
 * Created by alvar on 2017-08-19.
 */

class MuteHandler(var context: Context) {

    fun add() {

        // Prefs
        val prefs = context.getSharedPreferences("prefs", 0)
        val editor = prefs.edit()
        editor.putBoolean("on", true)
        editor.commit()

        // Update tile
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            EventBus.getDefault().post(TileUpdateEvent())
        }

        // Sound
        SoundMode().setMode(context, AudioManager.RINGER_MODE_SILENT)

        // Notification
        Notifications(context).sendOngoing()

        // Geofence
        GeofenceHandler(context, GeofenceHandler.ACTION_ADD)

    }

    fun remove(notify: Boolean) {

        // Prefs
        val prefs = context.getSharedPreferences("prefs", 0)
        val editor = prefs.edit()
        editor.putBoolean("on", false)
        editor.commit()

        // Update tile
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            EventBus.getDefault().post(TileUpdateEvent())
        }

        // Sound
        SoundMode().setMode(context, AudioManager.RINGER_MODE_NORMAL)

        // Notification
        if (notify) {
            Notifications(context).sendTurnedOff()
        } else {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(0)
        }

        // Geofence
        GeofenceHandler(context, GeofenceHandler.ACTION_REMOVE)

    }


}