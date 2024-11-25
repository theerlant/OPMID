package org.scahyana.opmid.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BrightnessAuto
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.scahyana.opmid.R
import org.scahyana.opmid.services.HapticFeedbackHelper
import org.scahyana.opmid.services.LocaleManager
import org.scahyana.opmid.services.SettingsManager
import org.scahyana.opmid.services.ThemeState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ThemeSwitcher(
) {
    val context = LocalContext.current

    val themeState by SettingsManager.themeSettingData.observeAsState(ThemeState.SYSTEM_DEFAULT.ordinal)
    val elementKey = "selected"

    val loc: LocaleManager = viewModel()

    val fillColorFirst by animateColorAsState(
        if (themeState == ThemeState.SYSTEM_DEFAULT.ordinal)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surface.copy(alpha = 0f)
    )
    val fillColorSecond by animateColorAsState(
        if (themeState == ThemeState.LIGHT.ordinal)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surface.copy(alpha = 0f)
    )
    val fillColorThird by animateColorAsState(
        if (themeState == ThemeState.DARK.ordinal)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surface.copy(alpha = 0f)
    )

    SharedTransitionLayout {
        Row {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        fillColorFirst
                    )
                    .clickable(
                        enabled = (themeState != ThemeState.SYSTEM_DEFAULT.ordinal)
                    ) {
                        HapticFeedbackHelper.performClickFeedback(context)
                        SettingsManager.themeSetting = ThemeState.SYSTEM_DEFAULT.ordinal
                    }
                    .padding(8.dp)

            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.BrightnessAuto, contentDescription = null,
                    )
                }
                Text(text = loc.getString(R.string.theme_auto))
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        fillColorSecond
                    )
                    .clickable(
                        enabled = (themeState != ThemeState.LIGHT.ordinal)
                    ) {
                        HapticFeedbackHelper.performClickFeedback(context)
                        SettingsManager.themeSetting = ThemeState.LIGHT.ordinal
                    }
                    .padding(8.dp)

            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LightMode, contentDescription = null,
                    )
                }
                Text(text = loc.getString(R.string.theme_bright))
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        fillColorThird
                    )
                    .clickable(
                        enabled = (themeState != ThemeState.DARK.ordinal)
                    ) {
                        HapticFeedbackHelper.performClickFeedback(context)
                        SettingsManager.themeSetting = ThemeState.DARK.ordinal
                    }
                    .padding(8.dp)

            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.DarkMode, contentDescription = null,
                    )
                }
                Text(text = loc.getString(R.string.theme_dark))
            }
        }
    }

}

@Composable
fun LanguageSwitcher() {
    val context = LocalContext.current

    val loc: LocaleManager = viewModel()

    val languageState by SettingsManager.languageSettingData.observeAsState("in")

    val leftPadWeight by animateFloatAsState(
        targetValue = if (languageState == "id") 0.01f else 1f, label = ""
    )
    val rightPadWeight by animateFloatAsState(
        targetValue = if (languageState == "id") 1f else 0.01f, label = ""
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Box(modifier = Modifier.weight(leftPadWeight))
            Box(
                modifier = Modifier
                    .animateContentSize()
                    .width(if (languageState == "id") 0.dp else 16.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp + 16.dp)
                    .clip(RoundedCornerShape(24.dp - 8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            )
            Box(
                modifier = Modifier
                    .animateContentSize()
                    .width(if (languageState == "id") 16.dp else 0.dp)
            )
            Box(modifier = Modifier.weight(rightPadWeight))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp + 16.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp - 8.dp))
                    .clickable(
                        enabled = (languageState != "id"),
                    ) {
                        HapticFeedbackHelper.performClickFeedback(context)
                        SettingsManager.languageSetting = "id"
                    }
                    .padding(20.dp)
            ) {
                Text(text = loc.getString(R.string.lang_in))
            }
            Box(
                modifier = Modifier
                    .width(16.dp)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp + 16.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp - 8.dp))
                    .clickable(
                        enabled = (languageState != "en"),
                    ) {
                        HapticFeedbackHelper.performClickFeedback(context)
                        SettingsManager.languageSetting = "en"
                    }
                    .padding(20.dp)
            ) {
                Text(text = loc.getString(R.string.lang_en))
            }
        }
    }
}