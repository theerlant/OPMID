package org.scahyana.opmid.components.mica

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

enum class MicaOrientation {
    Vertical, Horizontal
}

// Generate a single Mica (not combined)
@Composable
fun StaticMica(
    modifier: Modifier = Modifier,
    micaOrientation: MicaOrientation,
    numerator: Int,
    denominator: Int,
    additiveMica: Boolean
) {
    if (micaOrientation == MicaOrientation.Horizontal) {
        Column(
            modifier = modifier
        ) {
            GridDividerHorizontal(1f)
            Row(
                modifier = Modifier.weight(1f)
            ) {
                GridDividerVertical(1f)
                for (i in 1..denominator) {
                    Box(
                        modifier = Modifier
                            .background(
                                if (i <= numerator) {
                                    if (additiveMica) Color.Green.copy(
                                        alpha = 0.33f
                                    )
                                    else Color.Red.copy(alpha = 0.33f)
                                }
                                else Color.Transparent
                            )
                            .fillMaxHeight()
                            .weight(1f)
                    )
                    GridDividerVertical(1f)
                }
            }
            GridDividerHorizontal(1f)
        }
    } else {
        Row(
            modifier = modifier
        ) {
            GridDividerVertical(1f)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                GridDividerHorizontal(1f)
                for (i in 1..denominator) {
                    Box(
                        modifier = Modifier
                            .background(
                                if (i <= numerator) {
                                    if (additiveMica) Color.Green.copy(
                                        alpha = 0.33f
                                    )
                                    else Color.Red.copy(alpha = 0.33f)
                                }
                                else Color.Transparent
                            )
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    GridDividerHorizontal(1f)
                }
            }
            GridDividerVertical(1f)
        }
    }
}