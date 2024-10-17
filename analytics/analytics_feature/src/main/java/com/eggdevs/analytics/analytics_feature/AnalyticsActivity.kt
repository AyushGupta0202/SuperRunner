package com.eggdevs.analytics.analytics_feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eggdevs.analytics.data.di.analyticsDataModule
import com.eggdevs.analytics.presentation.dashboard.AnalyticsDashboardScreenRoot
import com.eggdevs.analytics.presentation.di.analyticsPresentationModule
import com.eggdevs.core.presentation.designsystem.SuperRunnerTheme
import com.google.android.play.core.splitcompat.SplitCompat
import org.koin.core.context.loadKoinModules

class AnalyticsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(
            listOf(
                analyticsDataModule,
                analyticsPresentationModule
            )
        )
        SplitCompat.installActivity(this)
        setContent {
            SuperRunnerTheme {
                val navigationController = rememberNavController()
                NavHost(
                    navController = navigationController,
                    startDestination = "analytics_dashboard"
                ) {
                    composable(route = "analytics_dashboard") {
                        AnalyticsDashboardScreenRoot(
                            onBackClick = {
                                finish()
                            }
                        )
                    }
                }
            }
        }
    }
}