package org.scahyana.opmid.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.ArrowLeft
import androidx.compose.material.icons.automirrored.rounded.ArrowRight
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idapgroup.autosizetext.AutoSizeText

@Composable
fun CounterButton(icon: ImageVector, enabled: Boolean = true, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(CircleShape)
            .size(24.dp)
            .clickable(enabled = enabled, onClick = onClick)
    ) {
        AnimatedVisibility(
            visible = enabled,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Reduce counter",
            )
        }
    }
}

@Composable
fun AnimatedCounter(
    value: Int,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge
) {
    AnimatedContent(
        targetState = value,
        transitionSpec = {
            if (targetState > initialState) {
                slideInHorizontally(initialOffsetX = { it }) + scaleIn() + fadeIn() togetherWith slideOutHorizontally(
                    targetOffsetX = { -it }) + scaleOut() + fadeOut()
            } else {
                slideInHorizontally(initialOffsetX = { -it }) + scaleIn() + fadeIn() togetherWith slideOutHorizontally(
                    targetOffsetX = { it }) + scaleOut() + fadeOut()
            }
        },
        modifier = modifier
    ) { count ->
        AutoSizeText(
            text = count.toString(),
            textAlign = TextAlign.Center,
            style = textStyle,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 16.dp).width(with(LocalDensity.current) {textStyle.fontSize.toDp() * 1.5f})
        )
    }
}

@Composable
fun NumberPicker(
    count: MutableIntState,
    upperLimit: Int = 10,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
    valid: Boolean = true,
) {
    val allowDecrement = (count.intValue > 0)
    val allowIncrement = (count.intValue < upperLimit)
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CounterButton(icon = Icons.AutoMirrored.Rounded.ArrowBackIos, enabled = allowDecrement) {
            count.intValue--
        }
        AnimatedCounter(value = count.intValue, textStyle = textStyle.copy(color = if (valid) Color.Unspecified else Color.Red))
        CounterButton(icon = Icons.AutoMirrored.Rounded.ArrowForwardIos, enabled = allowIncrement) {
            count.intValue++
        }
    }
}