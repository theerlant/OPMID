package org.scahyana.opmid.components.mica

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import org.scahyana.opmid.MathHelper

@Composable
fun MicaBox(
    targetPosition: Offset,
    color: Color,
    onClick: () -> Unit
) {
    // Second checks that made sure that the box is removed after clicked
    var isVisible by remember { mutableStateOf(true) }
    var isClicked by remember { mutableStateOf(false) }

    // State to animate offset
    var targetOffset by remember { mutableStateOf(Offset.Zero) }
    val currentOffsetX = remember { Animatable(0f) }
    val currentOffsetY = remember { Animatable(0f) }

    // State for animations
    val scale = remember { Animatable(1f) }
    val alpha = remember { Animatable(1f) }
    val cornerRadius = remember { Animatable(0f) }
    val animationDurationMs = 500

    val coroutineScope = rememberCoroutineScope()

    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    targetOffset = MathHelper.calculateOffset(
                        Offset(it.positionInWindow().x, it.positionInWindow().y),
                        targetPosition
                    )
                }
                .offset {
                    IntOffset(
                        currentOffsetX.value.toInt(),
                        currentOffsetY.value.toInt()
                    )
                }
                .scale(scale.value)
                .alpha(alpha.value)
                .background(color)
                .clickable(!isClicked) {
                    isClicked = true

                    onClick()

                    coroutineScope
                        .launch {
                            val jobOffsetX = launch {
                                currentOffsetX.animateTo(
                                    targetOffset.x,
                                    animationSpec = tween(animationDurationMs)
                                )
                            }
                            val jobOffsetY = launch {
                                currentOffsetY.animateTo(
                                    targetOffset.y,
                                    animationSpec = tween(animationDurationMs)
                                )
                            }
                            val jobScale = launch {
                                scale.animateTo(
                                    0f,
                                    animationSpec = tween(animationDurationMs)
                                )
                            }
                            val jobAlpha = launch {
                                alpha.animateTo(
                                    0f,
                                    animationSpec = tween(animationDurationMs)
                                )
                            }
                            val jobCornerRadius = launch {
                                cornerRadius.animateTo(
                                    50f,
                                    animationSpec = tween(animationDurationMs)
                                )
                            }

                            jobOffsetX.join()
                            jobOffsetY.join()
                            jobScale.join()
                            jobAlpha.join()
                            jobCornerRadius.join()
                        }
                        .invokeOnCompletion {
                            isVisible = false
                        }
                }
        )
    }
}