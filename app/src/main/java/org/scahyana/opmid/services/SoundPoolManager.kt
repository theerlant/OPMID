package org.scahyana.opmid.services

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log

object SoundPoolManager {
    private var soundPool: SoundPool? = null
    private var loadedSounds: MutableMap<Any, Int> = mutableMapOf()

    fun initialize() {
        val audioAttributes = AudioAttributes
            .Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool
            .Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(12)
            .build()
    }

    fun loadSound(key: Any, context: Context, resId: Int, priority: Int) {
        if (soundPool == null) {
            Log.e(null, "SoundPool is not initialized or missing")
            return
        }

        val soundId = soundPool!!.load(context, resId, priority)

        if (loadedSounds.containsKey(key)) {
            Log.e(null, "Key is already used for SoundID = ${loadedSounds[key]}")
            return
        }

        loadedSounds[key] = soundId
        Log.d(null, "Sound loaded with key $key successfully created as soundID: ${loadedSounds[key]}")
    }

    fun playSound(key: Any) {
        if (soundPool == null) {
            Log.e(null, "SoundPool is not initialized or missing")
            return
        }

        if (!loadedSounds.containsKey(key)) {
            Log.e(null, "SoundID for the specified key is not found. Load it first")
            return
        }

        val soundId = loadedSounds[key]

        val streamId = soundPool!!.play(
            soundId!!,
            1f,
            1f,
            1,
            0,
            1f
        )

        if (streamId == 0) {
            Log.e(null, "Playback of SoundID (${loadedSounds[key]}) is failed")
        } else {
            Log.d(null, "Playback of SoundID (${loadedSounds[key]}) is successful with StreamID: $streamId")
        }
    }

    fun release() {
        if (soundPool != null) {
            soundPool!!.release()

            soundPool = null
        }
    }
}