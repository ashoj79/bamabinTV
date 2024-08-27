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
import com.bamabin.tv_app.ui.screens.archive.PostTypeArchiveScreen
import com.bamabin.tv_app.ui.screens.comment_form.CommentFormScreen
import com.bamabin.tv_app.ui.screens.comments.CommentsScreen
import com.bamabin.tv_app.ui.screens.login.LoginScreen
import com.bamabin.tv_app.ui.screens.home.HomeScreen
import com.bamabin.tv_app.ui.screens.player.PlayerScreen
import com.bamabin.tv_app.ui.screens.post_details.PostDetailsScreen
import com.bamabin.tv_app.ui.screens.recently_viewed.RecentlyViewedScreen
import com.bamabin.tv_app.ui.screens.request_form.RequestForm
import com.bamabin.tv_app.ui.screens.splash.SplashScreen
import com.bamabin.tv_app.ui.screens.subscribe.SubscribeScreen
import com.bamabin.tv_app.ui.screens.taxonomy_posts.TaxonomyPosts
import com.bamabin.tv_app.ui.screens.watch_list.WatchListsScreen
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
                                HomeScreen(navHostController)
                            }

                            composable(route = Routes.LOGIN.name) {
                                LoginScreen(navHostController)
                            }

                            composable(route = Routes.REQUEST_FORM.name) {
                                RequestForm(navHostController)
                            }

                            composable(
                                route = "${Routes.TAXONOMY_POSTS}?taxonomy={taxonomy}&id={id}&title={title}&order={order}",
                                arguments = listOf(
                                    navArgument("taxonomy") {
                                        type = NavType.StringType
                                    },
                                    navArgument("id") {
                                        type = NavType.IntType
                                    },
                                    navArgument("title") {
                                        type = NavType.StringType
                                    },
                                    navArgument("order") {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    }
                                )
                            ) {
                                TaxonomyPosts(navHostController)
                            }

                            composable(route = Routes.SUBSCRIBE.name){
                                SubscribeScreen(navHostController)
                            }

                            composable(
                                route = "${Routes.POST_DETAILS.name}/{id}",
                                arguments = listOf(
                                    navArgument("id"){
                                        type = NavType.IntType
                                    }
                                )
                            ){
                                PostDetailsScreen(navHostController)
                            }

                            composable(
                                route = "${Routes.PLAYER.name}/{item}/{season}/{episode}",
                                arguments = listOf(
                                    navArgument("item") {
                                        type = NavType.IntType
                                    },
                                    navArgument("season") {
                                        type = NavType.IntType
                                        defaultValue = -1
                                    },
                                    navArgument("episode") {
                                        type = NavType.IntType
                                        defaultValue = -1
                                    },
                                )
                            ) {
                                PlayerScreen(navHostController)
                            }

                            composable(
                                route = "${Routes.POST_TYPE.name}?type={type}&broadcast_status={broadcast_status}&mini_serial={mini_serial}&order_by={order_by}&dlbox_type={dlbox_type}&free={free}&title={title}",
                                arguments = listOf(
                                    navArgument("type") {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    },
                                    navArgument("broadcast_status") {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    },
                                    navArgument("mini_serial") {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    },
                                    navArgument("order_by") {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    },
                                    navArgument("dlbox_type") {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    },
                                    navArgument("free") {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    },
                                    navArgument("title") {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    },
                                )
                            ) {
                                PostTypeArchiveScreen(navHostController = navHostController)
                            }

                            composable(
                                route = "${Routes.COMMENTS.name}/{id}/{title}",
                                arguments = listOf(
                                    navArgument("id"){
                                        type = NavType.IntType
                                    },
                                    navArgument("title"){
                                        type = NavType.StringType
                                    }
                                )
                            ) {
                                CommentsScreen(navHostController)
                            }

                            composable(
                                route = "${Routes.COMMENT_FORM.name}/{id}",
                                arguments = listOf(
                                    navArgument("id"){
                                        type = NavType.IntType
                                    }
                                )
                            ){
                                CommentFormScreen(navHostController)
                            }

                            composable(
                                route = Routes.RECENTLY_VIEWED.name
                            ) {
                                RecentlyViewedScreen(navHostController)
                            }

                            composable(
                                route = Routes.WATCHLIST.name
                            ) {
                                WatchListsScreen(navHostController)
                            }
                        }
                    }
                }
            }
        }
    }
}