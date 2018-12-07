package com.alvarlagerlof.smartmute

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat

/**
 * Created by alvar on 2017-08-18.
 */


class CheckPermissions(var context: Context) {

    fun check(): Boolean {

        // Ok var
        var ok = true


        // Gps
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ok = false
            }
        }


        // Change sound setings
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !notificationManager.isNotificationPolicyAccessGranted) {
            ok = false
        }


        return ok
    }


    fun request() {

        val intent2 = Intent(context, MainActivity::class.java)
        context.startActivity(intent2)
    }

}