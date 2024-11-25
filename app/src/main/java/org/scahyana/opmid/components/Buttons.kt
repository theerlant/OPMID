package org.scahyana.opmid.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.scahyana.opmid.services.SoundPoolManager

@Composable
fun ActionButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    text: String = "Button",
    style: TextStyle = MaterialTheme.typography.titleMedium,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors()
) {
    FilledTonalButton(
        onClick = {
            SoundPoolManager.playSound("button_click")
            onClick()
        },
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        shape = RoundedCornerShape(24.dp)
        ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(16.dp),
            style = style
        )
    }
}

@Composable
fun MainButton(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    icon: @Composable () -> Unit,
    label: String,
    onClick: () -> Unit,
    vertical: Boolean = true,
    ) {
    if (vertical) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .clip(RoundedCornerShape(24.dp))
                .background(color)
                .clickable(onClick = {
                    SoundPoolManager.playSound("button_click")
                    onClick()
                })
                .padding(8.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)) {
                icon()
            }
            Text(text = label, style = MaterialTheme.typography.titleMedium)
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .clip(RoundedCornerShape(24.dp))
                .background(color)
                .clickable(onClick = onClick)
                .padding(8.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                icon()
            }
            Text(text = label, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
        }
    }
}