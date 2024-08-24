package com.bamabin.tv_app.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.bamabin.tv_app.data.remote.model.videos.HomeSection
import com.bamabin.tv_app.ui.widgeta.ErrorDialog
import com.bamabin.tv_app.ui.widgeta.LoadingWidget
import com.bamabin.tv_app.ui.widgeta.MoreCard
import com.bamabin.tv_app.ui.widgeta.MovieCard
import com.bamabin.tv_app.utils.DataResult
import com.bamabin.tv_app.utils.Routes

@Composable
fun MainScreen(navHostController: NavHostController, mainViewModel: MainViewModel = hiltViewModel()) {
    val result by mainViewModel.homeSections.collectAsState()

    if (result is DataResult.DataSuccess) {
        TvLazyColumn {
            items(result.data!!.size) {
                Section(
                    homeSection = result.data!![it],
                    modifier = Modifier.padding(
                        bottom = if (it == result.data!!.size - 1) 32.dp else 16.dp
                    ),
                    navHostController = navHostController
                ) {
                    navHostController.navigate(mainViewModel.getMoreRoute(it))
                }
            }
        }
    }

    if (result is DataResult.DataLoading) {
        LoadingWidget()
    }

    if (result is DataResult.DataError) {
        ErrorDialog(message = result.message) {
            mainViewModel.getSection()
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun Section(
    modifier: Modifier,
    homeSection: HomeSection,
    navHostController: NavHostController,
    onMoreClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .width(8.dp)
                .height(32.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(
                        topEnd = 80.dp,
                        bottomEnd = 80.dp
                    )
                )
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = homeSection.name,
            style = MaterialTheme.typography.bodyLarge
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    TvLazyRow(modifier = modifier) {
        items(homeSection.posts.size + 1) {
            if (it < homeSection.posts.size) {
                MovieCard(
                    post = homeSection.posts[it],
                    modifier = Modifier.padding(
                        start = if (it == 0) 16.dp else 0.dp,
                    )
                ) {
                    val id = homeSection.posts[it].id
                    navHostController.navigate("${Routes.POST_DETAILS.name}/$id")
                }
            } else {
                MoreCard(onClick = onMoreClick)
            }
        }
    }
}