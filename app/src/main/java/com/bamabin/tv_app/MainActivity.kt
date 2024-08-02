package com.bamabin.tv_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.bamabin.tv_app.ui.screens.home.HomeScreen
import com.bamabin.tv_app.ui.screens.splash.SplashScreen
import com.bamabin.tv_app.ui.theme.BamabinTVTheme
import com.bamabin.tv_app.utils.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val coroutineScope = rememberCoroutineScope()
            val navHostController = rememberNavController()

            BamabinTVTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        NavHost(
                            navController = navHostController,
                            startDestination = Routes.SPLASH.name
                        ) {
                            composable(route = Routes.SPLASH.name) {
                                SplashScreen(navHostController)
                            }

                            composable(route = Routes.START.name) {
                                HomeScreen(coroutineScope, navHostController)
                            }

//                            composable(
//                                route = "${Routes.ARCHIVE}/{post_type}",
//                                arguments = listOf(
//                                    navArgument("post_type") {
//                                        type = NavType.StringType
//                                        defaultValue = ""
//                                    }
//                                )
//                            ) { backStackEntry ->
//                                val typeName = backStackEntry.arguments?.getString("post_type") ?: ""
//                                val postType = try { PostType.valueOf(typeName) } catch (_:Exception) { null }
//
//                                ArchiveScreen(postType, navHostController)
//                            }
                        }
                    }
                }
            }
        }
    }
}