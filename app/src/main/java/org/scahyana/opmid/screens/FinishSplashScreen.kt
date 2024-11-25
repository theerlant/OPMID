package org.scahyana.opmid.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.scahyana.opmid.LocalNavigation
import org.scahyana.opmid.R

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun FinishSplashScreen() {
    val navController = LocalNavigation.current

    val checkmarkIcon = AnimatedImageVector.animatedVectorResource(R.drawable.checkmark_anim)
    var animationState by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        animationState = true
        delay(3000)
        navController.navigate("home") {
            popUpTo(0) { inclusive = true }
        }
    }

    // Checkmark UI
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .systemBarsPadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.padding(48.dp)
            ) {
                Image(
                    painter = rememberAnimatedVectorPainter(
                        animatedImageVector = checkmarkIcon,
                        atEnd = animationState
                    ),
                    contentDescription = "Finish Icon",
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        blendMode = BlendMode.SrcIn
                    )
                )
            }
        }
}