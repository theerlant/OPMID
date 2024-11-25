@file:Suppress("SpellCheckingInspection")

package org.scahyana.opmid

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.graphics.Color
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.scahyana.opmid.operation.AdditionSameDenomScreen
import org.scahyana.opmid.operation.AdditionScreen
import org.scahyana.opmid.operation.SubtractionScreen
import org.scahyana.opmid.operation.OperationType
import org.scahyana.opmid.operation.SubtractionSameDenomScreen
import org.scahyana.opmid.screens.FinishSplashScreen
import org.scahyana.opmid.screens.FractionFinishScreen
import org.scahyana.opmid.screens.FractionSelectorScreen
import org.scahyana.opmid.screens.HomeScreen
import org.scahyana.opmid.screens.SettingOverlay
import org.scahyana.opmid.services.HapticFeedbackHelper
import org.scahyana.opmid.services.LocaleManager
import org.scahyana.opmid.services.SettingsManager
import org.scahyana.opmid.services.SoundPoolManager
import org.scahyana.opmid.ui.theme.OPMIDTheme

val LocalNavigation = staticCompositionLocalOf<NavHostController> { error("Not provided") }

class MainActivity : ComponentActivity() {
    // Create a custom locale manager
    private val localeManager: LocaleManager by viewModels()

    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            )
        )
        super.onCreate(savedInstanceState)

        SoundPoolManager.initialize()

        setContent {
            val navController = rememberNavController()

            val systemUiController = rememberSystemUiController()
            val configuration = LocalConfiguration.current
            val window = (LocalContext.current as Activity).window

            SettingsManager.initialize(LocalContext.current)

            HapticFeedbackHelper.initialize(LocalContext.current)

            SoundPoolManager.loadSound("button_click", LocalContext.current, R.raw.sfx_click, 1)

            OPMIDTheme(
                lifecycleOwner = this
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SharedTransitionLayout(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CompositionLocalProvider(
                            LocalNavigation provides navController
                        ) {
//                        NavHost(navController, startDestination = "intro") {
//                            composable("intro") {
//                                Intro()
//                            }
//                            composable("home") {
//
//                            }
//                        }
                            NavHost(
                                navController = navController,
                                startDestination = "home",
                                enterTransition = {
                                    slideInHorizontally(
                                        animationSpec = tween(500, 50, EaseOutCubic),
                                        initialOffsetX = {
                                            it / 4
                                        }
                                    ) + fadeIn(tween(500, 50, EaseOutCubic))
                                },
                                exitTransition = {
                                    if (targetState.destination.route == "home") {
                                        slideOutHorizontally(
                                            animationSpec = tween(500, 50, EaseOutCubic),
                                            targetOffsetX = {
                                                it / 4
                                            }
                                        ) + fadeOut(tween(500, 50, EaseOutCubic))
                                    } else {
                                        slideOutHorizontally(
                                            targetOffsetX = {
                                                -it / 4
                                            }
                                        ) + fadeOut(tween(500, 50, EaseOutCubic))
                                    }

                                }
                            ) {
                                composable("home",
                                    enterTransition = {
                                        if (initialState.destination.route == "setting") {
                                            fadeIn()
                                        } else {
                                            scaleIn(
                                                animationSpec = tween(300, 50),
                                                initialScale = 0.9f
                                            ) + fadeIn(tween(300, 100, EaseOutCubic))
                                        }
                                    },
                                    exitTransition = {
                                        if (targetState.destination.route == "setting") {
                                            fadeOut()
                                        } else {
                                            scaleOut(
                                                animationSpec = tween(300, 50),
                                                targetScale = 0.9f
                                            ) + fadeOut(tween(300, 100, EaseOutCubic))
                                        }
                                    }
                                ) {
                                    HomeScreen(
                                        animatedVisibilityScope = this@composable,
                                        sharedTransitionScope = this@SharedTransitionLayout
                                    )
                                }
                                composable("setting",
                                    enterTransition = {
                                        fadeIn()
                                    },
                                    exitTransition = {
                                        fadeOut()
                                    }
                                ) {
                                    SettingOverlay(
                                        animatedVisibilityScope = this@composable,
                                        sharedTransitionScope = this@SharedTransitionLayout
                                    )
                                }
                                composable("micaselector") {
                                    FractionSelectorScreen(
                                        animatedVisibilityScope = this@composable,
                                        sharedTransitionScope = this@SharedTransitionLayout
                                    )
                                }
                                composable(
                                    "addition/{numA}/{denomA}/{numB}/{denomB}",
                                    exitTransition = null,
                                    arguments = listOf(
                                        navArgument("numA") { type = NavType.IntType },
                                        navArgument("denomA") { type = NavType.IntType },
                                        navArgument("numB") { type = NavType.IntType },
                                        navArgument("denomB") { type = NavType.IntType },
                                    )
                                ) { backStackEntry ->
                                    val numA = backStackEntry.arguments?.getInt("numA")
                                    val denomA = backStackEntry.arguments?.getInt("denomA")
                                    val numB = backStackEntry.arguments?.getInt("numB")
                                    val denomB = backStackEntry.arguments?.getInt("denomB")

                                    if (numA != null && denomA != null && numB != null && denomB != null) {
                                        if (denomA == denomB) {
                                            AdditionSameDenomScreen(
                                                numeratorA = numA,
                                                numeratorB = numB,
                                                denominator = denomA,
                                                animatedVisibilityScope = this@composable,
                                                sharedTransitionScope = this@SharedTransitionLayout
                                            )
                                        } else {
                                            AdditionScreen(
                                                numeratorA = numA,
                                                denominatorA = denomA,
                                                numeratorB = numB,
                                                denominatorB = denomB,
                                                animatedVisibilityScope = this@composable,
                                                sharedTransitionScope = this@SharedTransitionLayout
                                            )
                                        }
                                    }
                                }
                                composable(
                                    "subtraction/{numA}/{denomA}/{numB}/{denomB}",
                                    exitTransition = null,
                                    arguments = listOf(
                                        navArgument("numA") { type = NavType.IntType },
                                        navArgument("denomA") { type = NavType.IntType },
                                        navArgument("numB") { type = NavType.IntType },
                                        navArgument("denomB") { type = NavType.IntType },
                                    )
                                ) { backStackEntry ->
                                    val numA = backStackEntry.arguments?.getInt("numA")
                                    val denomA = backStackEntry.arguments?.getInt("denomA")
                                    val numB = backStackEntry.arguments?.getInt("numB")
                                    val denomB = backStackEntry.arguments?.getInt("denomB")

                                    if (numA != null && denomA != null && numB != null && denomB != null) {
                                        if (denomA == denomB) {
                                            SubtractionSameDenomScreen(
                                                numeratorA = numA,
                                                numeratorB = numB,
                                                denominator = denomA,
                                                animatedVisibilityScope = this@composable,
                                                sharedTransitionScope = this@SharedTransitionLayout
                                            )
                                        } else {
                                            SubtractionScreen(
                                                numeratorA = numA,
                                                denominatorA = denomA,
                                                numeratorB = numB,
                                                denominatorB = denomB,
                                                animatedVisibilityScope = this@composable,
                                                sharedTransitionScope = this@SharedTransitionLayout
                                            )
                                        }
                                    }
                                }
                                composable(
                                    "finishscreen/{numA}/{denomA}/{numB}/{denomB}/{numClicked}/{denomClicked}/{op}",
                                    enterTransition = null,
                                    arguments = listOf(
                                        navArgument("numA") { type = NavType.IntType },
                                        navArgument("denomA") { type = NavType.IntType },
                                        navArgument("numB") { type = NavType.IntType },
                                        navArgument("denomB") { type = NavType.IntType },
                                        navArgument("numClicked") { type = NavType.IntType },
                                        navArgument("denomClicked") { type = NavType.IntType },
                                        navArgument("op") { type = NavType.IntType }
                                    )
                                ) { backStackEntry ->
                                    val numA = backStackEntry.arguments!!.getInt("numA")
                                    val denomA = backStackEntry.arguments!!.getInt("denomA")
                                    val numB = backStackEntry.arguments!!.getInt("numB")
                                    val denomB = backStackEntry.arguments!!.getInt("denomB")
                                    val numClicked = backStackEntry.arguments!!.getInt("numClicked")
                                    val denomClicked =
                                        backStackEntry.arguments!!.getInt("denomClicked")
                                    val op = backStackEntry.arguments!!.getInt("op")

                                    FractionFinishScreen(
                                        numeratorA = numA,
                                        denominatorA = denomA,
                                        numeratorB = numB,
                                        denominatorB = denomB,
                                        clickedNumerator = numClicked,
                                        clickedDenominator = denomClicked,
                                        animatedVisibilityScope = this@composable,
                                        sharedTransitionScope = this@SharedTransitionLayout,
                                        operation = if (op == 0) OperationType.ADDITION else OperationType.SUBTRACTION
                                    )
                                }
                                composable(
                                    "finishsplash",
                                    enterTransition = {scaleIn() + fadeIn()},
                                ) {
                                    FinishSplashScreen()
                                }
                            }

                            // TESTING SITE
//                            NavHost(navController= navController, startDestination = "home") {
//                                composable("home") {
//                                    FractionFinishScreen(
//                                        numeratorA = 1,
//                                        denominatorA = 2,
//                                        numeratorB = 7,
//                                        denominatorB = 8,
//                                        clickedNumerator = 22,
//                                        clickedDenominator = 16,
//                                        animatedVisibilityScope = this@composable,
//                                        sharedTransitionScope = this@SharedTransitionLayout,
//                                        operation = OperationType.ADDITION
//                                    )
//                                }
//                            }
                        }
                    }
                }
            }
        }
    }
}