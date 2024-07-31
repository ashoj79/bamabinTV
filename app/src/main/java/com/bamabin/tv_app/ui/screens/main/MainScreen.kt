package com.bamabin.tv_app.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.bamabin.tv_app.data.remote.model.videos.HomeSection
import com.bamabin.tv_app.ui.widgeta.LoadingWidget
import com.bamabin.tv_app.ui.widgeta.MoreCard
import com.bamabin.tv_app.ui.widgeta.MovieCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope
) {
    val isLoading by mainViewModel.isLoading.collectAsState()

    val mainColumnController = rememberScrollState()
    var scrollState by remember { mutableIntStateOf(0) }
    var isRendered by remember { mutableStateOf(false) }

    LaunchedEffect(scrollState) {
        coroutineScope.launch {
            if (scrollState == 1) {
                mainColumnController.animateScrollTo(0)
            } else if (scrollState == 2) {
                mainColumnController.animateScrollTo(mainColumnController.maxValue)
            }
        }
    }

    if (!isLoading) {
        Column(
            modifier = Modifier
                .verticalScroll(mainColumnController)
                .onGloballyPositioned {
                    isRendered = true
                }
        ) {
            for (it in 0 until mainViewModel.homeSections.size) {
                Section(
                    homeSection = mainViewModel.homeSections[it],
                    modifier = Modifier.padding(
                        bottom = if (it == mainViewModel.homeSections.size - 1) 32.dp else 16.dp
                    ),
                    onListHovered = {
                        scrollState = when (it) {
                            mainViewModel.homeSections.size - 1 -> 2
                            0 -> 1
                            else -> 0
                        }
                    }
                )
            }
        }
    }

    if (!isRendered) {
        LoadingWidget()
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun Section(
    modifier: Modifier,
    homeSection: HomeSection,
    onListHovered: () -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
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

        TvLazyRow(modifier = modifier.onFocusChanged { if (it.hasFocus) onListHovered() }) {
            items(homeSection.posts.size + 1) {
                if (it < homeSection.posts.size) {
                    MovieCard(
                        post = homeSection.posts[it],
                        modifier = Modifier.padding(
                            start = if (it == 0) 16.dp else 0.dp,
                        )
                    )
                } else {
                    MoreCard()
                }
            }
        }
    }
}