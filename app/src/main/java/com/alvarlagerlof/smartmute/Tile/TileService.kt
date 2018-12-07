package com.alvarlagerlof.smartmute.Tile

import android.graphics.drawable.Icon
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.support.annotation.RequiresApi
import com.alvarlagerlof.smartmute.MuteHandler
import com.alvarlagerlof.smartmute.R
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode



/**
 * Created by alvar on 2017-08-18.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
class TileService : TileService() {
    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onTileAdded() {
        super.onTileAdded()
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        EventBus.getDefault().unregister(this)
        MuteHandler(this).remove(notify = false)
    }

    override fun onStartListening() {
        super.onStartListening()
        EventBus.getDefault().register(this)
        updateTileState()
    }

    override fun onStopListening() {
        super.onStopListening()
        EventBus.getDefault().unregister(this)
    }

    override fun onClick() {
        super.onClick()

        // Get
        val prefs = applicationContext.getSharedPreferences("prefs", 0)

        // Logic
        if (prefs.getBoolean("on", false)) {
            MuteHandler(this).remove(notify = false)
        } else {
            MuteHandler(this).add()
        }
    }

    fun updateTileState() {

        val tile = qsTile
        val prefs = applicationContext.getSharedPreferences("prefs", 0)

        if (tile != null) {
            if (prefs.getBoolean("on", false)) {
                tile.label = "Smartmute"
                tile.state = Tile.STATE_ACTIVE
                tile.icon = Icon.createWithResource(this, R.drawable.ic_notification)
                tile.updateTile()
            } else {
                tile.label = "Smartmute"
                tile.state = Tile.STATE_INACTIVE
                tile.icon = Icon.createWithResource(this, R.drawable.ic_notification)
                tile.updateTile()
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: TileUpdateEvent) {
        updateTileState()
    }

}