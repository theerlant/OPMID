package org.scahyana.opmid.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.VolumeMute
import androidx.compose.material.icons.automirrored.rounded.VolumeOff
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Contrast
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.EdgesensorHigh
import androidx.compose.material.icons.rounded.EdgesensorLow
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Smartphone
import androidx.compose.material.icons.rounded.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.scahyana.opmid.LocalNavigation
import org.scahyana.opmid.R
import org.scahyana.opmid.components.IconPlaceholder
import org.scahyana.opmid.components.LanguageSwitcher
import org.scahyana.opmid.components.ThemeSwitcher
import org.scahyana.opmid.isInGestureNavigationMode
import org.scahyana.opmid.services.HapticFeedbackHelper
import org.scahyana.opmid.services.LocaleManager
import org.scahyana.opmid.services.SettingsManager
import org.scahyana.opmid.services.SoundPoolManager
import org.scahyana.opmid.services.ThemeState
import org.scahyana.opmid.values.TransitionKey

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SettingOverlay(
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    val navController = LocalNavigation.current

    val loc: LocaleManager = viewModel()

    val currentLocale by loc.currentLocale.observeAsState()

    var enableContent by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (enableContent) 1f else 0f,
        label = "",
        animationSpec = tween(800, 100)
    )

    val soundState by SettingsManager.soundSettingData.observeAsState(true)
    val feedbackState by SettingsManager.feedbackSettingData.observeAsState(true)

    DisposableEffect(Unit) {
        // Set the value to true when the composable is first composed
        enableContent = true
        // Cleanup action to reset the value when the composable is removed
        onDispose {
            enableContent = false
        }
    }

    val context = LocalContext.current
    val vibrateRequestLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission())
        { granted ->
            // If the vibrate request in not granted. Turn off the vibrator
            SettingsManager.feedbackSetting = granted
        }

    val statusBarGradient = remember { Animatable(0f) }
    var navBarDivider by remember { mutableStateOf(true) }

    val systemBarInsets = WindowInsets.systemBars.asPaddingValues()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }
            .distinctUntilChanged()
            .collect { scrollPosition ->
                if (scrollPosition.dp > systemBarInsets.calculateTopPadding() + 96.dp) {
                    coroutineScope.launch {
                        statusBarGradient.animateTo(
                            1f,
                            tween(100, 0, EaseInOutSine)
                        )
                    }
                } else {
                    coroutineScope.launch {
                        statusBarGradient.animateTo(
                            0f,
                            tween(100, 0, EaseInOutSine)
                        )
                    }
                }

                navBarDivider =
                    scrollPosition.dp <= scrollState.maxValue.dp - 12.dp
            }
    }


    with(sharedTransitionScope) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .sharedElement(
                    rememberSharedContentState(key = TransitionKey.SettingContainer),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .border(0.dp, Color.Black.copy(alpha = 0.2f), RectangleShape)
                .clip(RoundedCornerShape(0.dp))
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .height(WindowInsets.statusBars.getTop(LocalDensity.current).dp)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = statusBarGradient.value * 0.2f),
                                Color.Transparent
                            )
                        )
                    )
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .systemBarsPadding()
                    .padding(16.dp)
                    .clip(CircleShape)
                    .zIndex(1f)
                    .clickable(enabled = enableContent) {
                        navController.popBackStack()
                    }
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .sharedBounds(
                            rememberSharedContentState(key = TransitionKey.SettingButton),
                            animatedVisibilityScope
                        )
                        .size(24.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
            ) {
                Column(
                    modifier =
                    if (LocalView.current.isInGestureNavigationMode())
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .systemBarsPadding()
                            .padding(8.dp)
                    else
                        Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                            .verticalScroll(scrollState)
                            .statusBarsPadding()
                            .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp + 16.dp + 24.dp))
                    IconPlaceholder(
                        modifier = Modifier
                            .heightIn(72.dp, 128.dp)
                            .widthIn(72.dp, 128.dp)
                            .clip(RoundedCornerShape(32.dp - 16.dp)),
                        animatedVisibilityScope, sharedTransitionScope
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.skipToLookaheadSize()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 16.dp)
                        ) {
                            Text(
                                text = "OPMID",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.displayMedium,
                                modifier = Modifier
                                    .wrapContentHeight(Alignment.CenterVertically)
                                    .sharedBounds(
                                        rememberSharedContentState(key = TransitionKey.AppBarTitle),
                                        animatedVisibilityScope
                                    )
                            )
                            key(currentLocale) {
                                Text(
                                    text = loc.getString(R.string.app_abbreviation),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .wrapContentHeight(Alignment.CenterVertically)
                                )
                            }
                        }
                        SettingColumn {
                            SettingButton(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.History,
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp)
                                    )
                                },
                                title = {
                                    key(currentLocale) {
                                        Text(
                                            text = loc.getString(R.string.history),
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                },
                                action = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }
                        SettingColumn {
                            ExpandableSettingButton(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Language,
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp)
                                    )
                                },
                                title = {
                                    key(currentLocale) {
                                        Text(
                                            text = loc.getString(R.string.language),
                                            style = MaterialTheme.typography.bodyLarge,
                                        )
                                    }
                                },
                                description = {
                                    Text(
                                        text = when (SettingsManager.languageSettingData.value) {
                                            "id" -> loc.getString(R.string.lang_in)
                                            "en" -> loc.getString(R.string.lang_en)
                                            else -> "Auto"
                                        },
                                        style = MaterialTheme.typography.labelLarge,
                                    )
                                },
                                showDivider = false,
                                content = {
                                    LanguageSwitcher()
                                }
                            )
                            ExpandableSettingButton(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Contrast,
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp)
                                    )
                                },
                                title = {
                                    key(currentLocale) {
                                        Text(
                                            text = loc.getString(R.string.theme),
                                            style = MaterialTheme.typography.bodyLarge,
                                        )
                                    }
                                },
                                description = {
                                    key(currentLocale) {
                                        Text(
                                            text =
                                            when (SettingsManager.themeSettingData.value) {
                                                ThemeState.SYSTEM_DEFAULT.ordinal -> loc.getString(R.string.theme_auto)
                                                ThemeState.LIGHT.ordinal -> loc.getString(R.string.theme_bright)
                                                ThemeState.DARK.ordinal -> loc.getString(R.string.theme_dark)
                                                else -> "Auto"
                                            },
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }
                                },
                                content = {
                                    ThemeSwitcher()
                                }
                            )
                            SettingButton(
                                icon = {
                                    AnimatedContent(targetState = soundState) { state ->
                                        if (state) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
                                                contentDescription = null,
                                                modifier = Modifier.size(30.dp)
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Rounded.VolumeOff,
                                                contentDescription = null,
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }
                                    }
                                },
                                title = {
                                    key(currentLocale) {
                                        Text(
                                            text = loc.getString(R.string.sound_effect),
                                            style = MaterialTheme.typography.bodyLarge,
                                        )
                                    }
                                },
                                action = {
                                    Switch(
                                        checked = soundState,
                                        onCheckedChange =
                                        {
                                            HapticFeedbackHelper.performClickFeedback(context)

                                            SettingsManager.soundSetting = it

                                            if (it) {
                                                SoundPoolManager.playSound("button_click")
                                            }
                                        },
                                    )
                                }
                            )
                            SettingButton(
                                icon = {
                                    AnimatedContent(targetState = feedbackState) { state ->
                                        if (state) {
                                            Icon(
                                                imageVector = Icons.Rounded.Vibration,
                                                contentDescription = null,
                                                modifier = Modifier.size(30.dp)
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Rounded.Smartphone,
                                                contentDescription = null,
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }
                                    }
                                },
                                title = {
                                    key(currentLocale) {
                                        Text(
                                            text = loc.getString(R.string.feedback),
                                            style = MaterialTheme.typography.bodyLarge,
                                        )
                                    }
                                },
                                action = {
                                    Switch(
                                        checked = feedbackState,
                                        onCheckedChange = {
                                            HapticFeedbackHelper.performClickFeedback(context)

                                            if (it) {
                                                val permissionStatus =
                                                    ContextCompat.checkSelfPermission(
                                                        context,
                                                        Manifest.permission.VIBRATE
                                                    )

                                                when (permissionStatus) {
                                                    PackageManager.PERMISSION_GRANTED -> SettingsManager.feedbackSetting =
                                                        true

                                                    else -> vibrateRequestLauncher.launch(
                                                        Manifest.permission.VIBRATE
                                                    )
                                                }
                                            } else {
                                                SettingsManager.feedbackSetting = false
                                            }
                                        })
                                }
                            )
                        }
                        SettingColumn {
                            SettingButton(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Info,
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp)
                                    )
                                },
                                title = {
                                    key(currentLocale) {
                                        Text(
                                            text = loc.getString(R.string.about_app),
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                },
                                action = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }
                    }
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart),
                    thickness = if (navBarDivider) 1.dp else 0.dp
                )
            }

        }
    }
}

@Composable
private fun SettingColumn(
    modifier: Modifier = Modifier,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.33f)),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun SettingButton(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    action: @Composable () -> Unit = {},
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier =
        if (onClick != null)
            modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 24.dp, vertical = 18.dp)
        else
            modifier
                .padding(horizontal = 24.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Box(modifier = Modifier.weight(1f)) {
            title()
        }

        action()
    }
}

@Composable
private fun ExpandableSettingButton(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit = {},
    showDivider: Boolean = true,
    content: @Composable () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val iconRotation = animateFloatAsState(
        if (expanded) 180f else 0f, label = ""
    )

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = {
                    expanded = !expanded
                })
                .padding(horizontal = 24.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Column(
                modifier = Modifier.weight(1f)
            ) {
                title()
                AnimatedVisibility(
                    visible = !expanded,
                ) {
                    description()
                }
            }
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.rotate(iconRotation.value)
            )
        }
        AnimatedVisibility(visible = expanded) {
            Column {
                if (showDivider) Divider()
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxWidth()
                ) {
                    content()
                }
                if (showDivider) Divider()
            }
        }
    }
}