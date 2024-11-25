package org.scahyana.opmid.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.scahyana.opmid.values.TransitionKey

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FloatingAppBar(
    modifier: Modifier = Modifier,
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    navigationEnabled: Boolean = false,
    navigationOnClick: () -> Unit = {},
    actionIcon: @Composable () -> Unit = {},
    actionEnabled: Boolean = false,
    actionOnClick: () -> Unit = {},
    animationVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    val windowInsets = WindowInsets.systemBars.asPaddingValues()

    val shapeCornerRadius by animateDpAsState(if (navigationEnabled || actionEnabled) 16.dp else 0.dp)
    val floatingStatusBarPadding by animateDpAsState(if (navigationEnabled || actionEnabled) windowInsets.calculateTopPadding() + 8.dp else 0.dp)
    val filledstatusBarPadding by animateDpAsState(if (navigationEnabled || actionEnabled) 0.dp else windowInsets.calculateTopPadding() + 8.dp)
    val floatPadding by animateDpAsState(if (navigationEnabled || actionEnabled) 8.dp else 0.dp)
    val titlePadding = 12.dp
    val buttonSize by animateDpAsState(if (navigationEnabled || actionEnabled) 56.dp else 0.dp)
    val actionAlpha by animateFloatAsState(if (actionEnabled) 0.33f else 0f)

    var fullStatusBar by remember {
        mutableStateOf(!(navigationEnabled || actionEnabled))
    }

    LaunchedEffect(navigationEnabled, actionEnabled) {
        fullStatusBar = !(navigationEnabled || actionEnabled)
    }

    Row(
        modifier = modifier
            .padding(
                start = floatPadding,
                top = floatingStatusBarPadding,
                end = floatPadding,
                bottom = floatPadding
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(floatPadding)
    ) {
        with(animationVisibilityScope) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(buttonSize)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.33f))
                    .clickable(
                        enabled = navigationEnabled,
                        onClick = navigationOnClick
                    )
                    .animateEnterExit(
                        enter = slideInHorizontally { -it / 2 },
                        exit = slideOutHorizontally { it / 2 }
                    )
            ) {
                navigationIcon()
            }
            with(sharedTransitionScope) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .zIndex(1f)
                        .sharedBounds(
                            rememberSharedContentState(key = TransitionKey.AppBarContainer),
                            animatedVisibilityScope = animationVisibilityScope,
                        )
                        .weight(1f)
                        .clip(RoundedCornerShape(shapeCornerRadius))
                        .background(
                            MaterialTheme.colorScheme.primaryContainer
                        )
                        .padding(
                            start = titlePadding,
                            top = filledstatusBarPadding + titlePadding,
                            end = titlePadding,
                            bottom = titlePadding
                        )
                ) {
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .wrapContentHeight(Alignment.CenterVertically)
                    )
                }
            }
            with(animationVisibilityScope) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(buttonSize)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Yellow.copy(alpha = actionAlpha))
                        .clickable(
                            enabled = actionEnabled,
                            onClick = actionOnClick
                        )
                        .animateEnterExit(
                            enter = slideInHorizontally { -it / 2 },
                            exit = slideOutHorizontally { it / 2 }
                        )
                ) {
                    actionIcon()
                }
            }
        }
    }
}