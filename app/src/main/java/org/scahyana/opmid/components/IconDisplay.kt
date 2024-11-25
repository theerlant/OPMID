package org.scahyana.opmid.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.scahyana.opmid.R

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationSpecApi::class)
@Composable
fun IconPlaceholder(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {
    with (sharedTransitionScope) {
        BoxWithConstraints(
            contentAlignment = Alignment.Center,
            modifier = modifier.sharedBounds(
                rememberSharedContentState(key = "appicon"),
                animatedVisibilityScope,
                boundsTransform = BoundsTransform { initialBounds, targetBounds ->
                        keyframes {
                        durationMillis = 300
                        initialBounds at 0 using ArcMode.ArcBelow using FastOutSlowInEasing
                        targetBounds at 300
                        }
                    }
            )
        ) {
            val size = remember { mutableStateOf(0.dp) }
            println(maxHeight)
            size.value = if (maxWidth > maxHeight) maxHeight else maxWidth

            Image(
                painterResource(R.drawable.ic_launcher_background),
                contentDescription = "OPMID",
                modifier = Modifier
                    .size(size.value)
                    .clip(RoundedCornerShape(25))
            )
            Image(
                painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "OPMID",
                modifier = Modifier.size(size.value)
            )
        }
    }
}