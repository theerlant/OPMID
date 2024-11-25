package org.scahyana.opmid.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.content.ContextCompat

object HapticFeedbackHelper {
    private lateinit var vibrator: Vibrator
    private var enableFeedback = true

    fun initialize(
        context: Context,
    ) {
        vibrator = context.getSystemService(Vibrator::class.java)

        SettingsManager.feedbackSettingData.observeForever {
            enableFeedback = it
        }
    }

    fun performClickFeedback(context: Context) {
        if (!this::vibrator.isInitialized || !enableFeedback) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For API 29 and above
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For API 26 to 28
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            // For older versions
            vibrator.vibrate(50)
        }
    }
}