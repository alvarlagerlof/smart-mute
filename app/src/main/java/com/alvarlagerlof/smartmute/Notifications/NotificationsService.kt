package com.alvarlagerlof.smartmute.Notifications

import android.app.IntentService
import android.content.Intent
import com.alvarlagerlof.smartmute.MuteHandler

/**
 * Created by alvar on 2017-08-19.
 */

class NotificationsService : IntentService("NotificationService") {

    public override fun onHandleIntent(intent: Intent?) {
        MuteHandler(this).remove(notify = false)
    }
}