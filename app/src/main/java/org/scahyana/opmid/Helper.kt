package org.scahyana.opmid

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Check if gesture navigation mode is active.
 * In gesture navigation mode, the standard navigation bar is hidden and replaced with a gesture area with a handle.
 *
 * To detect if the device is in gesture navigation mode, both conditions should be met:
 * 1. At least one system gesture horizontal inset is greater than 0.
 * 2. Navigation bar horizontal insets are equal to 0.
 */
fun View.isInGestureNavigationMode(): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        return false
    }
    val windowInsets = ViewCompat.getRootWindowInsets(this) ?: return false
    val systemGesturesInsets =  windowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemGestures())
    val navigationBarsInsets = windowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.navigationBars())

    val hasSystemGestureHorizontalInset = systemGesturesInsets.left > 0 || systemGesturesInsets.right > 0
    val hasNavigationBarHorizontalInset = navigationBarsInsets.left > 0 || navigationBarsInsets.right > 0

    return hasSystemGestureHorizontalInset && !hasNavigationBarHorizontalInset
}