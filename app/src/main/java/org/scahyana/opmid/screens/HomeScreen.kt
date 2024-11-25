package org.scahyana.opmid.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.scahyana.opmid.LocalNavigation
import org.scahyana.opmid.R
import org.scahyana.opmid.components.IconPlaceholder
import org.scahyana.opmid.components.MainButton
import org.scahyana.opmid.services.LocaleManager
import org.scahyana.opmid.services.SoundPoolManager
import org.scahyana.opmid.values.TransitionKey

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {
    val loc: LocaleManager = viewModel()
    val navController = LocalNavigation.current

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {
        with(sharedTransitionScope) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconPlaceholder(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    animatedVisibilityScope, sharedTransitionScope
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .sharedBounds(
                            rememberSharedContentState(key = TransitionKey.AppBarContainer),
                            animatedVisibilityScope = animatedVisibilityScope,
                            zIndexInOverlay = 2f
                        )
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            MaterialTheme.colorScheme.primaryContainer
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = loc.getString(R.string.app_name),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .wrapContentHeight(Alignment.CenterVertically)
                            .sharedBounds(
                                rememberSharedContentState(key = TransitionKey.AppBarTitle),
                                animatedVisibilityScope
                            )
                    )
                }
                Surface(
                    modifier = Modifier
                        .sharedElement(
                            rememberSharedContentState(key = TransitionKey.SettingContainer),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .size(56.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            SoundPoolManager.playSound("button_click")
                            navController.navigate("setting")
                        },
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = null,
                            modifier = Modifier
                                .sharedBounds(
                                    rememberSharedContentState(key = TransitionKey.SettingButton),
                                    animatedVisibilityScope
                                )
                        )
                    }
                }
            }
        }
            Text(
                text = loc.getString(R.string.greeting_morning),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            )
        MainButton(
            modifier = Modifier
                .fillMaxWidth()

                .weight(1f),
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null
                )
            }, label = loc.getString(R.string.digital_mica),
            onClick = { navController.navigate("micaselector") },
            color = Color.Yellow.copy(alpha = 0.33f)
        )
        MainButton(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .defaultMinSize(minHeight = 128.dp),
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null
                )
            }, label = loc.getString(R.string.quiz), onClick = { /*TODO*/ },
            color = Color.Red.copy(alpha = 0.23f)
        )

    }

}

