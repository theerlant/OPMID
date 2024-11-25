package org.scahyana.opmid.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.scahyana.opmid.LocalNavigation
import org.scahyana.opmid.MathHelper
import org.scahyana.opmid.R
import org.scahyana.opmid.components.ActionButton
import org.scahyana.opmid.components.FloatingAppBar
import org.scahyana.opmid.components.CustomAlertDialog
import org.scahyana.opmid.operation.FractionDisplay
import org.scahyana.opmid.operation.OperationType

@Composable
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationGraphicsApi::class)
fun FractionFinishScreen(
    numeratorA: Int,
    denominatorA: Int,
    numeratorB: Int,
    denominatorB: Int,
    clickedNumerator: Int,
    clickedDenominator: Int,
    operation: OperationType,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    val simplifiable: Boolean = MathHelper.gcd(clickedNumerator, clickedDenominator) > 1
    val skipDialog = remember { mutableStateOf(false) }

    val navController = LocalNavigation.current

    BackHandler {
        if (simplifiable) skipDialog.value = true
        else {
            navController.navigate("home") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    when {
        skipDialog.value -> {
            CustomAlertDialog(
                openDialog = skipDialog,
                message = {
                    Text(
                        text = "Apakah kamu yakin ingin melewati operasi penyederhanaan pecahan ini?",
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    TextButton(onClick = {
                        skipDialog.value = false
                        // TODO: Add to history
                        navController.navigate("finishsplash") {
                            popUpTo(0) { inclusive = true }
                        }
                    }) {
                        Text(
                            text = "Ya, Lewati",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 18.sp
                        )
                    }
                    TextButton(onClick = { skipDialog.value = false }) {
                        Text(text = "Tidak", fontSize = 18.sp)
                    }
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize(),
    ) {
        with(sharedTransitionScope) {
            FloatingAppBar(
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState(key = "appbar"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ),
                title = operation.toString(),
                navigationIcon = {
                    Box(
                        modifier = Modifier.fillMaxSize(0.5f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                    }
                },
                navigationEnabled = false,
                actionIcon = {
                    Box(
                        modifier = Modifier.fillMaxSize(0.5f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.QuestionMark,
                            contentDescription = "null"
                        )
                    }
                },
                actionEnabled = false,
                actionOnClick = { println("Coming soon!") },
                animationVisibilityScope = animatedVisibilityScope,
                sharedTransitionScope = sharedTransitionScope
            )
        }
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FractionDisplay(
                        numerator = numeratorA,
                        denominator = denominatorA,
                        textStyle = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = if (operation == OperationType.ADDITION) "+" else "-", style = MaterialTheme.typography.displayMedium)
                    Spacer(modifier = Modifier.width(16.dp))
                    FractionDisplay(
                        numerator = numeratorB,
                        denominator = denominatorB,
                        textStyle = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.weight(1f)
                    )
                }
                with (sharedTransitionScope) {
                    FractionDisplay(
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState("finalfraction"),
                            animatedVisibilityScope
                        ),
                        numerator = clickedNumerator,
                        denominator = clickedDenominator,
//                denominatorTextPosition = denominatorTextPosition,
//                numeratorTextPosition = numeratorTextPosition,
                        textStyle = MaterialTheme.typography.displayMedium,
                    )
                }
            }
            val promptText =
                if (simplifiable) "Pecahan dapat disederhanakan! Mari lanjut menyederhakan pecahan ini"
                else "Pecahan tidak dapat disederhanakan karena tidak memiliki pembagi selain 1"
            Text(
                text = promptText,
                modifier = Modifier
                    .padding(16.dp),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (simplifiable) {
                    ActionButton(
                        onClick = { skipDialog.value = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        text = "Lewati",
                        modifier = Modifier.weight(1f)
                    )
                }
                ActionButton(
                    onClick =
                    {
//                        /*TODO: Add to History*/
                        if (!simplifiable) {
                            navController.navigate("finishsplash") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    text = if (simplifiable) "Lanjut" else "Kembali",
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

        }
    }

}