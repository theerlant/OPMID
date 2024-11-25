package org.scahyana.opmid.components.mica

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun GridDividerHorizontal(zIndex: Float) {
    Box(
        modifier = Modifier
            .background(Color.DarkGray)
            .fillMaxWidth()
            .zIndex(zIndex)
            .height(6.dp)
    )
}

@Composable
fun GridDividerVertical(zIndex: Float) {
    Box(
        modifier = Modifier
            .background(Color.DarkGray)
            .fillMaxHeight()
            .zIndex(zIndex)
            .width(6.dp)
    )
}