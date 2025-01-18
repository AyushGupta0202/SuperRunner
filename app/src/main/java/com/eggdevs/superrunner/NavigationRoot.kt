package com.eggdevs.superrunner

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.eggdevs.auth.presentation.intro.IntroScreenRoot
import com.eggdevs.auth.presentation.login.LoginScreenRoot
import com.eggdevs.auth.presentation.register.RegisterScreenRoot
import com.eggdevs.run.presentation.active_run.ActiveRunScreenRoot
import com.eggdevs.core.notification.service.ActiveRunService
import com.eggdevs.run.presentation.run_overview.RunOverviewScreenRoot

@Composable
fun NavigationRoot(
    navController: NavHostController,
    isLoggedIn: Boolean,
    onAnalyticsClick: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Routes.Run else Routes.Auth
    ) {
        authGraph(navController)
        runGraph(navController, onAnalyticsClick)
    }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<Routes.Auth>(
        startDestination = Routes.Intro,
    ) {
        composable<Routes.Intro> {
            IntroScreenRoot(
                onSignInClick = {
                    navController.navigate(Routes.Login)
                },
                onSignUpClick = {
                    navController.navigate(Routes.Register)
                }
            )
        }

        composable<Routes.Register> {
            RegisterScreenRoot(
                onSignInClick = {
                    navController.navigate(Routes.Login) {
                        popUpTo(Routes.Register) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onSuccessfulRegistration = {
                    navController.navigate(Routes.Login)
                }
            )
        }

        composable<Routes.Login> {
            LoginScreenRoot(
                onLoginClick = {
                    navController.navigate(Routes.Run) {
                        popUpTo(Routes.Auth) {
                            inclusive = true
                        }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Routes.Register) {
                        popUpTo(Routes.Login) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                }
            )
        }
    }
}

private fun NavGraphBuilder.runGraph(
    navController: NavHostController,
    onAnalyticsClick: () -> Unit = {}
) {
    navigation<Routes.Run>(
        startDestination = if (ActiveRunService.isServiceActive.value) Routes.ActiveRun else Routes.RunOverview,
    ) {
        composable<Routes.RunOverview> {
            RunOverviewScreenRoot(
                onAnalyticsClick = {
                    onAnalyticsClick()
                },
                onLogoutClick = {
                    navController.navigate(Routes.Auth) {
                        popUpTo(Routes.Run) {
                            inclusive = true
                        }
                    }
                },
                onStartRunClick = {
                    navController.navigate(Routes.ActiveRun)
                }
            )
        }
        composable<Routes.ActiveRun>(
            deepLinks = listOf(
                navDeepLink<Routes.ActiveRun>(
                    basePath = "super_runner://active_run"
                )
            )
        ) {
            val context = LocalContext.current
            ActiveRunScreenRoot(
                onBack = {
                    navController.navigateUp()
                },
                onRunFinished = {
                    navController.navigateUp()
                },
                onServiceToggle = { shouldServiceRun ->
                    if (shouldServiceRun) {
                        context.startService(
                            ActiveRunService.createStartIntent(
                                context = context,
                                activityClass = MainActivity::class.java
                            )
                        )
                    } else {
                        context.startService(
                            ActiveRunService.createStopIntent(context)
                        )
                    }
                }
            )
        }
    }
}