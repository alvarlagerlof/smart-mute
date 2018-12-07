package com.alvarlagerlof.smartmute.Geofence

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import com.alvarlagerlof.smartmute.MuteHandler
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent


/**
 * Created by alvar on 2017-08-18.
 */

class GeofenceTransitionsIntentService : IntentService("GeofenceTransitionsIs") {

    lateinit var event: GeofencingEvent

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onHandleIntent(intent: Intent?) {

        event = GeofencingEvent.fromIntent(intent)
        if (!event.hasError()) {

            if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                MuteHandler(this).remove(notify = true)
            }
        }
    }

}