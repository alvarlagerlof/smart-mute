package com.alvarlagerlof.smartmute

import android.content.Context
import android.media.AudioManager

/**
 * Created by alvar on 2017-08-18.
 */

class SoundMode {

    fun setMode(context: Context, mode: Int) {
        val am: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.ringerMode = mode
    }


}
