package org.scahyana.opmid.operation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import org.scahyana.opmid.components.AnimatedCounter
import org.scahyana.opmid.components.NumberPicker

@Composable
fun FractionDisplay(
    modifier: Modifier = Modifier,
    numerator: Int,
    denominator: Int,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    textStyle: TextStyle = MaterialTheme.typography.displayMedium,
    denominatorTextPosition: MutableState<Offset>? = null,
    numeratorTextPosition: MutableState<Offset>? = null
) {
    Column(
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement,
        modifier = modifier
    ) {
        AnimatedCounter(
            value = numerator,
            modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
                if (numeratorTextPosition != null) {
                    val position = layoutCoordinates.positionInWindow()
                    val size = layoutCoordinates.size
                    numeratorTextPosition.value =
                        Offset(
                            x = position.x + size.width / 2,
                            y = position.y + size.height / 2
                        )
                }
            },
            textStyle = textStyle
        )
        Box(
            modifier = Modifier
                .background(Color.Black)
                .width(with(LocalDensity.current) { textStyle.fontSize.toDp() * 1.2f })
                .height(with(LocalDensity.current) { textStyle.fontSize.toDp() / 10 })
        )
        AnimatedCounter(
            value = denominator,
            modifier = Modifier
                .onGloballyPositioned { layoutCoordinates ->
                    if (denominatorTextPosition != null) {
                        val position = layoutCoordinates.positionInWindow()
                        val size = layoutCoordinates.size
                        denominatorTextPosition.value =
                            Offset(
                                x = position.x + size.width / 2,
                                y = position.y + size.height / 2
                            )
                    }
                },
            textStyle = textStyle
        )
    }
}

@Composable
fun FractionPicker(
    modifier: Modifier = Modifier,
    numerator: MutableIntState = remember { mutableIntStateOf(0) },
    denominator: MutableIntState = remember { mutableIntStateOf(0) },
    numeratorValid: Boolean = true,
    denominatorValid: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        NumberPicker(count = numerator, textStyle = textStyle, valid = numeratorValid)
        Box(
            modifier = Modifier
                .background(Color.Black)
                .width(with(LocalDensity.current) { textStyle.fontSize.toDp() * 1.2f })
                .height(with(LocalDensity.current) { textStyle.fontSize.toDp() / 10 })
        )
        NumberPicker(count = denominator, textStyle = textStyle, valid = denominatorValid)
    }
}