package com.andmar.weather.ui.navigate

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.NavHostController
import com.andmar.weather.ui.weather.WeatherScreenDestination
import com.andmar.weather.ui.weather.WeatherScreen
import com.andmar.weather.ui.location.AddLocationDestination
import com.andmar.weather.ui.location.AddLocationScreen
import com.andmar.weather.ui.location.LocationDestination
import com.andmar.weather.ui.location.LocationScreen
import com.andmar.weather.ui.weather.DetailsDestination
import com.andmar.weather.ui.weather.DetailsScreen
import com.andmar.weather.ui.settings.SettingsDestination
import com.andmar.weather.ui.settings.SettingsScreen


@Composable
fun WeatherNavigateHost(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        startDestination = WeatherScreenDestination.route,
        navController = navController,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(
            route = WeatherScreenDestination.route
        ) {
            WeatherScreen(
                onNavSettings = {
                    navController.navigate(SettingsDestination.route)
                },
                onNavLocation = {
                    navController.navigate(LocationDestination.route)
                },
                onNavDetails = {
                    navController.navigate(DetailsDestination.route)
                }
            )
        }
        
        composable(
            route = LocationDestination.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            LocationScreen(
                onNavAdd = {
                    navController.navigate(AddLocationDestination.route)
                },
                onNavBack = {
                    navController.navigateUp()
                }
            )
        }
        
        composable(
            route = AddLocationDestination.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            AddLocationScreen(
                onNavBack = {
                    navController.navigateUp()
                }
            )
        }
        
        composable(
            route = DetailsDestination.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            DetailsScreen(
                onNavBack = {
                    navController.navigateUp()
                }
            )
        }
        
        composable(
            route = SettingsDestination.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            SettingsScreen(
                onNavBack = {
                    navController.navigateUp()
                }
            )
        }
    }

}