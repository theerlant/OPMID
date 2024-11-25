package org.scahyana.opmid.ui.theme

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.scahyana.opmid.services.SettingsManager
import org.scahyana.opmid.services.ThemeState

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun OPMIDTheme(
    lifecycleOwner: LifecycleOwner,
    content: @Composable () -> Unit
) {
    val systemTheme = isSystemInDarkTheme()
    var darkTheme by remember { mutableStateOf(systemTheme) }

    // Observe theme setting changes
    DisposableEffect(lifecycleOwner) {
        val observer = Observer<Int> { value ->
             darkTheme = when (value) {
                ThemeState.SYSTEM_DEFAULT.ordinal -> systemTheme
                ThemeState.DARK.ordinal -> true
                ThemeState.LIGHT.ordinal -> false
                 else -> systemTheme
             }
        }

        SettingsManager.themeSettingData.observe(lifecycleOwner, observer)

        onDispose {
            SettingsManager.themeSettingData.removeObservers(lifecycleOwner)
        }
    }


    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }



    val view = LocalView.current
    val systemUiController = rememberSystemUiController()
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
//
//            window.apply {
//                statusBarColor = Color.Transparent.toArgb()
//                navigationBarColor = Color.Transparent.toArgb()
//                WindowCompat.setDecorFitsSystemWindows(this, false)
//
//
//            }

            systemUiController.setSystemBarsColor(Color.Transparent, isNavigationBarContrastEnforced = false)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

fun updateSystemBarsAppearance(
    systemUiController: SystemUiController,
    configuration: Configuration,
    window: Window,
    navbarColor: Color? = null,
    statusbarColor: Color? = null,
    isDarkColor: Boolean? = null
) {
    if (statusbarColor != null) {
        systemUiController.setStatusBarColor(statusbarColor)
    }
    if (navbarColor != null) {
        systemUiController.setNavigationBarColor(navbarColor)
    }

    val isDarkTheme = isDarkColor ?: (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = !isDarkTheme
    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightNavigationBars = !isDarkTheme
}