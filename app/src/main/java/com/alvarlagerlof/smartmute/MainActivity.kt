package com.alvarlagerlof.smartmute

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    internal val PERMISSION_REQUEST_USE_GPS = 0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions(this)

        toggle.setOnClickListener {
            val prefs = applicationContext.getSharedPreferences("prefs", 0)

            if (prefs.getBoolean("on", false)) {
                MuteHandler(this).remove(notify = false)
            } else {
                MuteHandler(this).add()
            }
        }

    }


    fun checkPermissions(context: Context) {

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !notificationManager.isNotificationPolicyAccessGranted) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            context.startActivity(intent)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                    AlertDialog.Builder(this@MainActivity)
                            .setTitle("Smartmute")
                            .setMessage("Behöver gps")
                            .setPositiveButton("Ge tillstånd") { dialog, _ ->
                                dialog.dismiss()
                                ActivityCompat.requestPermissions(this@MainActivity,
                                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                                        PERMISSION_REQUEST_USE_GPS)
                            }
                            .setNegativeButton("avbryt") { dialog, _ -> dialog.dismiss() }
                            .show()


                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this@MainActivity,
                            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                            PERMISSION_REQUEST_USE_GPS)
                }
            } else {
                // TODO() GRANTED
            }
        } else {
            // TODO() GRANTED
        }

    }





    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_USE_GPS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO() GRANTED
                }
                return
            }
        }
    }



}
