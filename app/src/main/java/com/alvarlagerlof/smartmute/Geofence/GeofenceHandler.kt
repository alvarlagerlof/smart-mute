package com.alvarlagerlof.smartmute.Geofence

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import com.alvarlagerlof.smartmute.Notifications.Notifications
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallbacks
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

/**
 * Created by alvar on 2017-08-18.
 */
class GeofenceHandler(var context: Context, var action: Int) : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {



    var googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()


    init {
        googleApiClient.connect()
    }


    fun add() {

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE

        locationManager.requestSingleUpdate(criteria, object : LocationListener {
            override fun onLocationChanged(location: Location) {

                // Remove oldfirst
                remove()


                // Create geofence
                val geofence = Geofence.Builder()
                        .setRequestId("fence_id")
                        .setCircularRegion(location.latitude, location.longitude, 25f) // defining fence region
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build()

                val geofenceRequest = GeofencingRequest.Builder()
                        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_EXIT)
                        .addGeofence(geofence)
                        .build()

                val intent = Intent(context, GeofenceTransitionsIntentService::class.java)
                val pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


                // Add
                LocationServices.GeofencingApi.addGeofences(googleApiClient, geofenceRequest, pendingIntent).setResultCallback(object : ResultCallbacks<Status>() {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onSuccess(status: Status) {
                        if (DEBUG) Notifications(context).sendDebug(1, "Debug", "Successfully added fence")
                    }

                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onFailure(status: Status) {
                        if (DEBUG) Notifications(context).sendDebug(1, "Debug", "Failed to add fence")
                    }
                })

            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }, null)
    }


    fun remove() {
        val geofenceIds = ArrayList<String>()
        geofenceIds.add(FENCE_ID)
        LocationServices.GeofencingApi.removeGeofences(googleApiClient, geofenceIds).setResultCallback(object : ResultCallbacks<Status>() {
            override fun onSuccess(status: Status) {
                if (DEBUG) Notifications(context).sendDebug(100, "Debug", "Successfully Removed fence")
            }

            override fun onFailure(status: Status) {
                if (DEBUG) Notifications(context).sendDebug(100, "Debug", "Failed to remove fence")
            }
        })
    }








    // Stuff
    override fun onConnectionFailed(p0: ConnectionResult) { googleApiClient.connect() }
    override fun onConnectionSuspended(p0: Int) { googleApiClient.connect() }

    override fun onConnected(p0: Bundle?) {
        if (!googleApiClient.isConnected) {
            if (DEBUG) Notifications(context).sendDebug(30, "Debug", "Google apis not connected")
        } else {
            when (action) {
                ACTION_ADD -> add()
                ACTION_REMOVE -> remove()
            }
        }
    }


    // Objects
    companion object {
        val DEBUG = false
        val FENCE_ID = "fence_id"
        val ACTION_ADD = 0
        val ACTION_REMOVE = 1
    }


}