package org.scahyana.opmid.operation

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.SentimentDissatisfied
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.scahyana.opmid.LocalNavigation
import org.scahyana.opmid.components.CustomAlertDialog
import org.scahyana.opmid.components.FloatingAppBar
import org.scahyana.opmid.components.mica.MicaOrientation
import org.scahyana.opmid.components.mica.StaticMica
import org.scahyana.opmid.components.mica.SubtractionClickableMica
import kotlin.math.roundToInt

@SuppressLint("UnrememberedMutableInteractionSource")
@OptIn(
    ExperimentalSharedTransitionApi::class
)
@Composable
fun SubtractionScreen(
    numeratorA: Int,
    denominatorA: Int,
    numeratorB: Int,
    denominatorB: Int,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {
    val navController = LocalNavigation.current

    var movablePosition by remember { mutableStateOf(Offset.Zero) }
    var targetPosition by remember { mutableStateOf(Offset.Zero) }
    var targetWorldPos by remember { mutableStateOf(Offset.Zero) }
    var targetOffset by remember { mutableStateOf(Offset.Zero) }
    var isDropped by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    // State to track offset values
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }

    var showBottomSheet by remember { mutableStateOf(false) }

    // Helper text opacity
    val helperTextAlpha = remember { Animatable(1f) }

    // State of the fraction game
    var clickedNumerator by remember { mutableIntStateOf(0) }
    var _actualClickedNumerator by remember { mutableIntStateOf(0) }
    var clickedDenominator by remember { mutableIntStateOf(0) }

    val isNumeratorFinished = remember {
        mutableStateOf(false)
    }
    val isDenominatorFinished = remember {
        mutableStateOf(false)
    }


    fun addNumerator(remove: Boolean = false) {
        if (isNumeratorFinished.value) return
        if (remove) clickedNumerator-- else clickedNumerator++
        _actualClickedNumerator++
        if (_actualClickedNumerator == numeratorA * denominatorB + numeratorB * denominatorA) {
            isNumeratorFinished.value = true
            coroutineScope.launch { helperTextAlpha.animateTo(1f) }
        }
    }

    fun addDenominator() {
        if (isDenominatorFinished.value) return
        clickedDenominator++
        if (clickedDenominator == denominatorA * denominatorB) {
            isDenominatorFinished.value = true
        }
    }

    val fractionAlpha by animateFloatAsState(
        if (showBottomSheet) 1f else 0f,
        label = "",
        animationSpec = tween(
            durationMillis = 500,
            easing = EaseInOutSine
        )
    )
    val topBoxWeight by animateFloatAsState(
        if (!isDenominatorFinished.value) 1f else 0.6f
    )
    val bottomBoxWeight = 1f


    // Buttons
    val openCloseDialog = remember {
        mutableStateOf(false)
    }
    val openHelpDialog = remember {
        mutableStateOf(false)
    }

    val numeratorTextPosition = remember { mutableStateOf(Offset.Zero) }
    val denominatorTextPosition = remember { mutableStateOf(Offset.Zero) }

    // Manually handle Back button
    BackHandler {
        openCloseDialog.value = true
    }

    // Immediate navigate after operation finished
    when {
        isDenominatorFinished.value -> {
            if (isDenominatorFinished.value) {
                LaunchedEffect(Unit) {
                    delay(500)
                    navController.navigate("finishscreen/${numeratorA}/${denominatorA}/${numeratorB}/${denominatorB}/$clickedNumerator/$clickedDenominator/1") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }
        }
    }

    // Handle dialogs
    when {
        openCloseDialog.value -> {
            CustomAlertDialog(
                openDialog = openCloseDialog,
                headerIcon = {
                    Icon(
                        imageVector = Icons.Rounded.SentimentDissatisfied,
                        contentDescription = null,
                        modifier = Modifier
                            .size(96.dp)
                            .padding(vertical = 12.dp)
                    )
                },
                message = {
                    Text(
                        text = "Apakah kamu yakin ingin kembali sebelum menyelesaikan pecahan ini?",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                actions = {
                    TextButton(onClick = {
                        openCloseDialog.value = false
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = false }
                        }
                    }) {
                        Text(
                            text = "Ya, Kembali",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    TextButton(onClick = { openCloseDialog.value = false }) {
                        Text(text = "Tidak", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            )
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.navigationBarsPadding()
    ) {
        with(sharedTransitionScope) {
            FloatingAppBar(
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState(key = "appbar"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ),
                title = "Pengurangan",
                navigationIcon = {
                    Box(
                        modifier = Modifier.fillMaxSize(0.5f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                    }
                },
                navigationEnabled = !isDenominatorFinished.value,
                navigationOnClick = { openCloseDialog.value = true },
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
                actionEnabled = !isDenominatorFinished.value,
                actionOnClick = { println("Coming soon!") },
                animationVisibilityScope = animatedVisibilityScope,
                sharedTransitionScope = sharedTransitionScope
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(topBoxWeight)
        ) {
            BoxWithConstraints {
                val boxSize = remember { mutableStateOf(0.dp) }

                // Calculate the smaller dimension and set it to boxSize
                boxSize.value = if (maxWidth < maxHeight) maxWidth else maxHeight
                boxSize.value -= 24.dp

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Top box before combined
                    if (!showBottomSheet) {
                        StaticMica(
                            modifier = Modifier
                                .size(boxSize.value)
                                .onGloballyPositioned { layoutCoordinates ->
                                    targetWorldPos = layoutCoordinates.positionInWindow()
                                    targetPosition = layoutCoordinates
                                        .positionInWindow()
                                        .let { position ->
                                            Offset(
                                                position.x / density.density,
                                                position.y / density.density
                                            )
                                        }
                                },
                            micaOrientation = MicaOrientation.Vertical,
                            numerator = numeratorA,
                            denominator = denominatorA,
                            additiveMica = true
                        )
                    }
                    // Top box after combined
                    else {
                        // To wrap the edges on left and right
                        SubtractionClickableMica(
                            modifier = Modifier.size(boxSize.value),
                            numeratorA = numeratorA,
                            denominatorA = denominatorA,
                            numeratorB = numeratorB,
                            denominatorB = denominatorB,
                            addNumCallback = { addNumerator() },
                            removeNumCallback = { addNumerator(true) },
                            addDenomCallback = { addDenominator() },
                            targetPosition = numeratorTextPosition.value,
                            numPositiveColor = Color.Green.copy(alpha = 0.33f),
                            numNegativeColor = Color.Red.copy(alpha = 0.33f),
                            denomColor = Color.Blue.copy(alpha = 0.33f)
                        )
                    }
                }
            }
        }
        BoxWithConstraints(
            modifier = Modifier
                .weight(bottomBoxWeight)
                .padding(0.dp),
            contentAlignment = Alignment.Center
        ) {
            val boxSize = remember { mutableStateOf(0.dp) }

            // Calculate the smaller dimension and set it to boxSize
            boxSize.value = if (maxWidth < maxHeight) maxWidth else maxHeight
            boxSize.value -= 24.dp

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                if (!showBottomSheet) {
                    // Bottom box
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        StaticMica(
                            modifier = Modifier
                                .size(boxSize.value)
                                .offset {
                                    IntOffset(
                                        offsetX.value.roundToInt(),
                                        offsetY.value.roundToInt()
                                    )
                                }
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragEnd = {
                                            val tolerance = with(density) { 30.dp.toPx() }
                                            isDropped = (
                                                    movablePosition.x < targetPosition.x + tolerance
                                                            && movablePosition.x > targetPosition.x - tolerance
                                                            && movablePosition.y < targetPosition.y + tolerance
                                                            && movablePosition.y > targetPosition.y - tolerance
                                                    )

                                            coroutineScope.launch {
                                                if (isDropped) {
                                                    val jobX = launch {
                                                        offsetX.animateTo(
                                                            targetOffset.x,
                                                            tween(300, easing = LinearOutSlowInEasing)
                                                        )
                                                    }
                                                    val jobY = launch {
                                                        offsetY.animateTo(
                                                            targetOffset.y,
                                                            tween(300, easing = LinearOutSlowInEasing)
                                                        )
                                                    }
                                                    jobX.join()
                                                    jobY.join()
                                                    delay(100)
                                                    showBottomSheet = true
                                                } else {
                                                    launch { offsetX.animateTo(0f) }
                                                    launch { offsetY.animateTo(0f) }
                                                }
                                            }
                                        }
                                    ) { change, dragAmount ->
                                        change.consume()
                                        coroutineScope.launch {
                                            offsetX.snapTo(offsetX.value + dragAmount.x)
                                            offsetY.snapTo(offsetY.value + dragAmount.y)
                                        }
                                    }
                                }
                                .onGloballyPositioned { layoutCoordinates ->
                                    if (!isDropped) {
                                        movablePosition = layoutCoordinates
                                            .positionInWindow()
                                            .let { position ->
                                                Offset(
                                                    position.x / density.density,
                                                    position.y / density.density
                                                )
                                            }
                                    }
                                    if (targetOffset == Offset.Zero) {
                                        targetOffset = layoutCoordinates.windowToLocal(targetWorldPos)
                                    }
                                },
                            micaOrientation = MicaOrientation.Horizontal,
                            numerator = numeratorB,
                            denominator = denominatorB,
                            additiveMica = false
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .alpha(fractionAlpha)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = if (!showBottomSheet) Modifier else Modifier.verticalScroll(
                            rememberScrollState()
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            with (sharedTransitionScope) {
                                FractionDisplay(
                                    numerator = clickedNumerator,
                                    denominator = clickedDenominator,
                                    denominatorTextPosition = denominatorTextPosition,
                                    numeratorTextPosition = numeratorTextPosition,
                                    modifier = Modifier.weight(1f).sharedElement(
                                        rememberSharedContentState("finalfraction"),
                                        animatedVisibilityScope
                                    ),
                                    textStyle = MaterialTheme.typography.displayMedium
                                )
                            }
                        }
                    }
                    val text =
                        if (!isNumeratorFinished.value) "Tap kotak berwarna hijau untuk menambah pembilang." else "Tap kotak berwarna biru untuk menambah penyebut."
                    Text(
                        text = text,
                        modifier = Modifier.alpha(helperTextAlpha.value),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,

                        )
                }
            }
        }
    }
}

