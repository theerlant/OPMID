package org.scahyana.opmid.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.scahyana.opmid.LocalNavigation
import org.scahyana.opmid.components.ActionButton
import org.scahyana.opmid.components.FloatingAppBar
import org.scahyana.opmid.operation.FractionPicker
import org.scahyana.opmid.operation.OperationType

enum class Operation {
    ADDITION,
    SUBSTRACTION,
}


@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun FractionSelectorScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    val navController = LocalNavigation.current

    val numeratorA = remember { mutableIntStateOf(0) }
    val denominatorA = remember { mutableIntStateOf(0) }
    val numeratorB = remember { mutableIntStateOf(0) }
    val denominatorB = remember { mutableIntStateOf(0) }

    // Validity check
    val numeratorAValid = (numeratorA.intValue > 0 && numeratorA.intValue <= denominatorA.intValue)
    val denominatorAValid = (denominatorA.intValue > 0 && denominatorA.intValue >= numeratorA.intValue)
    val numeratorBValid = (numeratorB.intValue > 0 && numeratorB.intValue <= denominatorB.intValue)
    val denominatorBValid = (denominatorB.intValue > 0 && denominatorB.intValue >= numeratorB.intValue)


    val operationType = remember { mutableStateOf(OperationType.ADDITION) }
    val leftPadWeight by animateFloatAsState(
        targetValue = if (operationType.value == OperationType.ADDITION) 0.01f else 1f, label = ""
    )
    val rightPadWeight by animateFloatAsState(
        targetValue = if (operationType.value == OperationType.ADDITION) 1f else 0.01f, label = ""
    )

    Column(
        modifier = Modifier.navigationBarsPadding()
    ) {
        with (sharedTransitionScope) {
            FloatingAppBar(
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState(key = "appbar"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ),
                title = "Pemilihan",
                navigationIcon = {
                    Box(modifier = Modifier.fillMaxSize(0.5f), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                navigationOnClick = { navController.popBackStack() },
                navigationEnabled = true,
                animationVisibilityScope = animatedVisibilityScope,
                sharedTransitionScope = sharedTransitionScope
            )
        }
        Text(
            text = "Masukkan pecahan dan pilih jenis operasi yang kamu ingin lakukan",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            FractionPicker(
                numerator = numeratorA,
                denominator = denominatorA,
                numeratorValid = numeratorAValid,
                denominatorValid = denominatorAValid, textStyle = MaterialTheme.typography.displayMedium
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
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
                            .width(if (operationType.value == OperationType.ADDITION) 0.dp else 16.dp)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .clip(RoundedCornerShape(24.dp - 8.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                    )
                    Box(
                        modifier = Modifier
                            .animateContentSize()
                            .width(if (operationType.value == OperationType.ADDITION) 16.dp else 0.dp)
                    )
                    Box(modifier = Modifier.weight(rightPadWeight))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Icon(modifier = Modifier
                        .size(56.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp - 8.dp))
                        .clickable(
                            enabled = (operationType.value != OperationType.ADDITION),
                        ) {
                            operationType.value = OperationType.ADDITION
                        }
                        .padding(12.dp),
                        imageVector = Icons.Rounded.Add, contentDescription = "Penjumlahan")
                    Box(
                        modifier = Modifier
                            .width(16.dp)
                    )
                    Icon(modifier = Modifier
                        .size(56.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp - 8.dp))
                        .clickable(
                            enabled = (operationType.value == OperationType.ADDITION)
                        ) {
                            operationType.value = OperationType.SUBTRACTION
                        }
                        .padding(12.dp),
                        imageVector = Icons.Rounded.Remove,
                        contentDescription = "Pengurangan")
                }
            }
            FractionPicker(
                numerator = numeratorB,
                denominator = denominatorB,
                numeratorValid = numeratorBValid,
                denominatorValid = denominatorBValid,
                textStyle = MaterialTheme.typography.displayMedium
            )
        }
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            ActionButton(
                enabled = (numeratorAValid && numeratorBValid && denominatorAValid && denominatorBValid),
                onClick = {
                    println("Called")
                    if (operationType.value == OperationType.ADDITION) {
                        navController.navigate("addition/${numeratorA.intValue}/${denominatorA.intValue}/${numeratorB.intValue}/${denominatorB.intValue}") {
                            popUpTo("home") { inclusive = true }
                        }
                    } else {
                        navController.navigate("subtraction/${numeratorA.intValue}/${denominatorA.intValue}/${numeratorB.intValue}/${denominatorB.intValue}") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                text = "Mulai"
            )
        }
    }
}