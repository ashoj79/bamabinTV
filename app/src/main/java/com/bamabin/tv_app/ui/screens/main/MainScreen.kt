package com.bamabin.tv_app.ui.screens.main

import android.util.Log
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.ClosedCaption
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bamabin.tv_app.R
import com.bamabin.tv_app.data.remote.model.videos.HomeSection
import com.bamabin.tv_app.data.remote.model.videos.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = viewModel(),
    coroutineScope: CoroutineScope
) {
    val isLoading by mainViewModel.isLoading.collectAsState()

    val mainColumnController = rememberScrollState()
    var scrollState by remember { mutableIntStateOf(0) }
    var isRendered by remember { mutableStateOf(false) }
    val lottieComposition by rememberLottieComposition(spec = LottieCompositionSpec.Asset("app_loading.json"))
    val lottieProcess by animateLottieCompositionAsState(lottieComposition, iterations = 100)

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
            modifier = Modifier.verticalScroll(mainColumnController).onGloballyPositioned {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
                    .padding(all = 0.dp)
                    .clipToBounds(),
            ) {
                LottieAnimation(
                    composition = lottieComposition,
                    progress = { lottieProcess },
                    modifier = Modifier.scale(1.5f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "در حال دریافت اطلاعات",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = TextUnit(20f, TextUnitType.Sp)
                )
            )
        }
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

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MovieCard(modifier: Modifier, post: Post) {
    var isFocused by remember { mutableStateOf(false) }
    val animatedBottomMargin by animateDpAsState(
        targetValue = if (isFocused) 24.dp else 8.dp,
        animationSpec = TweenSpec(durationMillis = 100)
    )

    Card(
        onClick = {},
        colors = CardDefaults.colors(containerColor = Color.Transparent),
        border = CardDefaults.border(focusedBorder = Border.None),
        scale = CardDefaults.scale(focusedScale = 1.25f),
        modifier = modifier
            .padding(horizontal = 4.dp)
            .onFocusChanged { isFocused = it.isFocused }
    ) {
        Column(
            modifier = Modifier
                .width(120.dp)
                .height(250.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .border(
                        width = if (isFocused) 2.dp else 0.dp,
                        color = if (isFocused) Color.White else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                AsyncImage(
                    model = post.thumbnail,
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(
                            bottom = animatedBottomMargin,
                            start = 8.dp
                        )
                        .background(
                            Color.Black.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(all = 2.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape),
                        painter = painterResource(id = R.drawable.imdb),
                        contentDescription = ""
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = post.imdbRate + " ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = TextUnit(10f, TextUnitType.Sp)
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(
                            bottom = animatedBottomMargin,
                            end = 4.dp
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.7f), shape = CircleShape)
                            .padding(all = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ClosedCaption,
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    if (post.hasAudio) {
                        Box(
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.7f), shape = CircleShape)
                                .padding(all = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Mic,
                                contentDescription = "",
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }

                if (isFocused) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 2.dp, end = 8.dp, start = 8.dp)
                    ) {
                        for (i in 0 until minOf(2, post.genres.size)) {
                            Text(
                                text = post.genres[i].name,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White,
                                    fontSize = TextUnit(9f, TextUnitType.Sp)
                                ),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier
                                    .padding(horizontal = 2.dp)
                                    .background(
                                        Color.Black.copy(alpha = 0.7f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = post.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = TextUnit(
                        12f,
                        TextUnitType.Sp
                    ),
                    color = if (isFocused) Color.White else Color.White.copy(alpha = 0.6f)
                ),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MoreCard(modifier: Modifier = Modifier) {
    var isFocused by remember { mutableStateOf(false) }

    Card(
        onClick = {},
        colors = CardDefaults.colors(containerColor = Color.Transparent),
        border = CardDefaults.border(focusedBorder = Border.None),
        scale = CardDefaults.scale(focusedScale = 1.25f),
        modifier = modifier
            .padding(end = 16.dp)
            .onFocusChanged { isFocused = it.isFocused }
    ) {
        Column(
            modifier = Modifier
                .width(120.dp)
                .height(250.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .background(color = Color(0xFF5F5E5E), shape = RoundedCornerShape(8.dp))
                    .border(
                        width = if (isFocused) 2.dp else 0.dp,
                        color = if (isFocused) Color.White else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreHoriz,
                    contentDescription = "",
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            color = Color(0xFF9B9B9B),
                            shape = CircleShape
                        )
                        .padding(all = 16.dp),
                    tint = Color.White,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "مشاهده بیشتر",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = TextUnit(
                        12f,
                        TextUnitType.Sp
                    ),
                    color = if (isFocused) Color.White else Color.White.copy(alpha = 0.6f)
                ),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}