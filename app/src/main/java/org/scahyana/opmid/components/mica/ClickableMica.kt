package org.scahyana.opmid.components.mica

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun AdditionClickableMica(
    modifier: Modifier = Modifier,
    numeratorA: Int,
    denominatorA: Int,
    numeratorB: Int,
    denominatorB: Int,
    addNumCallback: () -> Unit = {},
    addDenomCallback: () -> Unit = {},
    targetPosition: Offset,
    numColor: Color,
    denomColor: Color,
) {
    var clickedNumerator by remember { mutableIntStateOf(0) }

    fun addNum() {
        clickedNumerator++
        addNumCallback()
    }

    Row(
        modifier = modifier
    ) {
        GridDividerVertical(1f)
        Column(modifier = Modifier.weight(1f)) {
            GridDividerHorizontal(1f)
            for (i in 1..denominatorA) {
                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    for (j in 1..denominatorB) {
                        Row(modifier = Modifier.weight(1f)) {
                            Box(modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()) {
                                if (i <= numeratorA) {
                                    MicaBox(
                                        targetPosition = targetPosition,
                                        color = numColor,
                                        onClick = { addNum() }
                                    )
                                }
                                if (j <= numeratorB) {
                                    MicaBox(
                                        targetPosition = targetPosition,
                                        color = numColor,
                                        onClick = { addNum() }
                                    )
                                }
                                androidx.compose.animation.AnimatedVisibility(
                                    visible = (clickedNumerator == numeratorA * denominatorB + numeratorB * denominatorA),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    MicaBox(
                                        targetPosition = targetPosition,
                                        color = denomColor,
                                        onClick = addDenomCallback
                                    )
                                }
                            }
                            GridDividerVertical(1f)
                        }
                    }
                }
                GridDividerHorizontal(1f)
            }
        }
    }
}

@Composable
fun AdditionSameDenomClickableMica(
    modifier: Modifier = Modifier,
    numeratorA: Int,
    numeratorB: Int,
    denominator: Int,
    addNumCallback: () -> Unit = {},
    addDenomCallback: () -> Unit = {},
    targetPosition: Offset,
    numColor: Color,
    denomColor: Color,
) {
    var clickedNumerator by remember { mutableIntStateOf(0) }

    fun addNum() {
        clickedNumerator++
        addNumCallback()
    }

    Row(
        modifier = modifier
    ) {
        GridDividerVertical(1f)
        Column(modifier = Modifier.weight(1f)) {
            GridDividerHorizontal(1f)
            Row(modifier = Modifier.weight(1f)) {
                for (i in 1..denominator) {
                    Box(modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()) {
                        if (i <= numeratorA) {
                            MicaBox(
                                targetPosition = targetPosition,
                                color = numColor,
                                onClick = { addNum() }
                            )
                        }
                        if (i <= numeratorB) {
                            MicaBox(
                                targetPosition = targetPosition,
                                color = numColor,
                                onClick = { addNum() }
                            )
                        }
                        androidx.compose.animation.AnimatedVisibility(
                            visible = (clickedNumerator == numeratorA + numeratorB),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            MicaBox(
                                targetPosition = targetPosition,
                                color = denomColor,
                                onClick = addDenomCallback
                            )
                        }
                    }
                    GridDividerVertical(1f)
                }
            }
            GridDividerHorizontal(1f)
        }
    }
}

@Composable
fun SubtractionClickableMica(
    modifier: Modifier = Modifier,
    numeratorA: Int,
    denominatorA: Int,
    numeratorB: Int,
    denominatorB: Int,
    addNumCallback: () -> Unit = {},
    removeNumCallback: () -> Unit = {},
    addDenomCallback: () -> Unit = {},
    targetPosition: Offset,
    numPositiveColor: Color,
    numNegativeColor: Color,
    denomColor: Color,
) {
    var clickedNumerator by remember { mutableIntStateOf(0) }

    Row(
        modifier = modifier
    ) {
        GridDividerVertical(1f)
        Column(modifier = Modifier.weight(1f)) {
            GridDividerHorizontal(1f)
            for (i in 1..denominatorA) {
                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    for (j in 1..denominatorB) {
                        Row(modifier = Modifier.weight(1f)) {
                            Box(modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()) {
                                if (i <= numeratorA) {
                                    MicaBox(
                                        targetPosition = targetPosition,
                                        color = numPositiveColor,
                                        onClick = { clickedNumerator++; addNumCallback() }
                                    )
                                }
                                if (j <= numeratorB) {
                                    MicaBox(
                                        targetPosition = targetPosition,
                                        color = numNegativeColor,
                                        onClick = { clickedNumerator++; removeNumCallback() }
                                    )
                                }
                                androidx.compose.animation.AnimatedVisibility(
                                    visible = (clickedNumerator == numeratorA * denominatorB + numeratorB * denominatorA),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    MicaBox(
                                        targetPosition = targetPosition,
                                        color = denomColor,
                                        onClick = addDenomCallback
                                    )
                                }
                            }
                            GridDividerVertical(1f)
                        }
                    }
                }
                GridDividerHorizontal(1f)
            }
        }
    }
}

@Composable
fun SubtractionSameDenomClickableMica(
    modifier: Modifier = Modifier,
    numeratorA: Int,
    numeratorB: Int,
    denominator: Int,
    addNumCallback: () -> Unit = {},
    removeNumCallback: () -> Unit = {},
    addDenomCallback: () -> Unit = {},
    targetPosition: Offset,
    numPositiveColor: Color,
    numNegativeColor: Color,
    denomColor: Color,
) {
    var clickedNumerator by remember { mutableIntStateOf(0) }

    Row(
        modifier = modifier
    ) {
        GridDividerVertical(1f)
        Column(modifier = Modifier.weight(1f)) {
            GridDividerHorizontal(1f)
            Row(modifier = Modifier.weight(1f)) {
                for (i in 1..denominator) {
                    Box(modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()) {
                        if (i <= numeratorA) {
                            MicaBox(
                                targetPosition = targetPosition,
                                color = numPositiveColor,
                                onClick = { clickedNumerator++; addNumCallback()  }
                            )
                        }
                        if (i <= numeratorB) {
                            MicaBox(
                                targetPosition = targetPosition,
                                color = numNegativeColor,
                                onClick = { clickedNumerator++; removeNumCallback() }
                            )
                        }
                        androidx.compose.animation.AnimatedVisibility(
                            visible = (clickedNumerator == numeratorA + numeratorB),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            MicaBox(
                                targetPosition = targetPosition,
                                color = denomColor,
                                onClick = addDenomCallback
                            )
                        }
                    }
                    GridDividerVertical(1f)
                }
            }
            GridDividerHorizontal(1f)
        }
    }
}