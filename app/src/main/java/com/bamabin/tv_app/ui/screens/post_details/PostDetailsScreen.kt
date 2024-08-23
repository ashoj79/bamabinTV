package com.bamabin.tv_app.ui.screens.post_details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.ClosedCaption
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.rememberTvLazyListState
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import coil.compose.AsyncImage
import com.bamabin.tv_app.R
import com.bamabin.tv_app.ui.theme.AgeRateBox
import com.bamabin.tv_app.ui.theme.Failed
import com.bamabin.tv_app.ui.widgeta.EpisodeBox
import com.bamabin.tv_app.ui.widgeta.ErrorDialog
import com.bamabin.tv_app.ui.widgeta.LoadingWidget
import com.bamabin.tv_app.utils.DataResult

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun PostDetailsScreen(
    navHostController: NavHostController,
    viewModel: PostDetailsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val displayMetrics = context.resources.displayMetrics

    val data by viewModel.data.collectAsState()
    val selectedSeason by viewModel.selectedSeason.collectAsState()
    val bottomSheetItems = viewModel.bottomSheetItems

    val scrollState = rememberTvLazyListState()
    val defaultFocusRequester = remember { FocusRequester() }
    val showSeasons by viewModel.showSeasons.collectAsState()

    LaunchedEffect(showSeasons) {
        if (showSeasons) scrollState.scrollToItem(8)
    }

    LaunchedEffect(Unit) {
        viewModel.loadWatchData()
    }

    if (data is DataResult.DataLoading){
        LoadingWidget()
    } else if (data is DataResult.DataError){
        ErrorDialog(message = data.message) {
            viewModel.getDetails()
        }
    } else {
        data.data?.let {
            Box {
                AsyncImage(model = it.bgThumbnail, contentDescription = "", modifier = Modifier.fillMaxSize())

                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.1f),
                                Color.Black.copy(alpha = 0.3f),
                                Color.Black.copy(alpha = 0.4f),
                                Color.Black.copy(alpha = 0.6f),
                                Color.Black.copy(alpha = 0.7f),
                                Color.Black.copy(alpha = 0.9f),
                            ),
                            center = Offset.Zero,
                            radius = displayMetrics.widthPixels.toFloat() / 1.6f
                        )
                    )
                )

                TvLazyColumn(
                    state = scrollState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 8.dp),
                    horizontalAlignment = Alignment.Start,
                    contentPadding = PaddingValues(top = 40.dp)
                ) {
                    item {
                        Text(
                            text = it.title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = TextUnit(28f, TextUnitType.Sp)
                            ),
                            modifier = Modifier.focusable()
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = it.faTitle,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = TextUnit(20f, TextUnitType.Sp)
                                )
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Icon(
                                imageVector = Icons.Outlined.AccessTime,
                                contentDescription = "",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = it.getTime(),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White,
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item {
                        Row {
                            it.genres.forEach {
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .background(
                                            color = Color.Black.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .padding(vertical = 4.dp, horizontal = 8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = it.ageRate,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .background(
                                        color = AgeRateBox,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(vertical = 4.dp, horizontal = 8.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(
                                        color = Color.Black,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 8.dp)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.imdb_black),
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(32.dp, 16.dp)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = buildAnnotatedString {
                                        append("10/ ")
                                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = TextUnit(18f, TextUnitType.Sp))){
                                            append(it.imdbRate)
                                        }
                                        if (it.imdbVoteCount > 0)
                                            append(" (${it.getVoteCount()})")
                                    },
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color.White
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Icon(
                                imageVector = Icons.Outlined.ClosedCaption,
                                contentDescription = "",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "زیرنویس",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White,
                                )
                            )

                            if (it.hasDubbed){
                                Spacer(modifier = Modifier.width(8.dp))

                                Icon(
                                    imageVector = Icons.Outlined.Mic,
                                    contentDescription = "",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "دوبله",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color.White,
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        Text(
                            text = it.summary,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                Color.White
                            ),
                            maxLines = 8,
                            modifier = Modifier.width((screenWidth / 3).dp)
                        )
                    }

                    if (it.isSeries){
                        item {
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier
                                    .background(
                                        color = Color.DarkGray.copy(alpha = 0.8f),
                                        shape = RoundedCornerShape(2.dp)
                                    )
                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Filled.Subscriptions, contentDescription = "", tint = Color.White)

                                Text(
                                    text = "  تعداد کل فصل‌ها: ${it.seasons?.size ?: 0} |  وضعیت پخش: ${it.status}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color.White
                                    )
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        Row {
                            Button(
                                colors = ButtonDefaults.colors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    focusedContainerColor = MaterialTheme.colorScheme.primary
                                ),
                                border = ButtonDefaults.border(
                                    border = Border.None,
                                    focusedBorder = Border(
                                        border = BorderStroke(1.dp, Color.White),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                ),
                                shape = ButtonDefaults.shape(RoundedCornerShape(4.dp)),
                                modifier = Modifier.focusRequester(defaultFocusRequester),
                                onClick = {
                                    val route = viewModel.play()
                                    route?.let {
                                        navHostController.navigate(it)
                                    }
                                }) {
                                Row {
                                    Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "", tint = Color.White)
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = viewModel.getPlayButtonText(),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.White
                                    )
                                )
                            }

                            if (it.isSeries){
                                Spacer(modifier = Modifier.width(24.dp))

                                Button(
                                    colors = ButtonDefaults.colors(
                                        containerColor = Color.Black.copy(alpha = .5f),
                                        focusedContainerColor = Color.Black.copy(alpha = .5f)
                                    ),
                                    border = ButtonDefaults.border(
                                        border = Border.None,
                                        focusedBorder = Border(
                                            border = BorderStroke(1.dp, Color.White),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                    ),
                                    shape = ButtonDefaults.shape(RoundedCornerShape(4.dp)),
                                    onClick = {
                                        viewModel.showSeasons()
                                    }) {
                                    Row {
                                        Icon(
                                            painter = painterResource(R.drawable.series),
                                            contentDescription = "",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = "قسمت‌ها",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color.White
                                        )
                                    )
                                }
                            }
                        }
                    }

                    if (it.isSeries && showSeasons){
                        item {
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                               Text(
                                   text = "فصل‌ها:",
                                   style = MaterialTheme.typography.bodyMedium.copy(
                                       color = Color.White
                                   )
                               )

                                Spacer(modifier = Modifier.width(16.dp))

                                for (i in 0 until it.seasons!!.size){
                                    Button(
                                        colors = ButtonDefaults.colors(
                                            containerColor = if (selectedSeason == i) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            focusedContainerColor = if (selectedSeason == i) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        ),
                                        border = ButtonDefaults.border(
                                            border = Border.None,
                                            focusedBorder = Border(
                                                border = BorderStroke(1.dp, Color.White),
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                        ),
                                        shape = ButtonDefaults.shape(RoundedCornerShape(4.dp)),
                                        modifier = Modifier.padding(horizontal = 4.dp),
                                        onClick = { viewModel.changeSeason(i) }) {
                                        Text(
                                            text = "فصل ${it.seasons[i].name}",
                                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            for (i in 0 until it.seasons!![selectedSeason].episodes.size step 4){
                                val itemsCount = it.seasons[selectedSeason].episodes.size - i
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp),
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    for (j in i until i + minOf(4, itemsCount)){
                                        EpisodeBox(
                                            seasonName = it.seasons[selectedSeason].name,
                                            thumbnail = it.bgThumbnail,
                                            width = (screenWidth.dp - 32.dp) / 4,
                                            episode = it.seasons[selectedSeason].episodes[j]
                                        ) {
                                            viewModel.showEpisodeBottomSheet(j)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    if (bottomSheetItems.isNotEmpty()) {
        ItemsBottomSheet(
            title = viewModel.bottomSheetTitle,
            items = bottomSheetItems,
            onCloseRequest = { viewModel.hideBottomSheet() },
            onDownloadClick = {},
            onPlayClick = {
                val route = viewModel.playItem(it)
                route?.let {
                    navHostController.navigate(it)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTvMaterial3Api::class)
@Composable
private fun ItemsBottomSheet(
    title: String,
    items: List<String>,
    onCloseRequest: () -> Unit,
    onDownloadClick: (index: Int) -> Unit,
    onPlayClick: (index: Int) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        containerColor = Color.Black.copy(alpha = 0.7f),
        sheetState = sheetState,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.8f),
        onDismissRequest = onCloseRequest
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1f)
            )

            IconButton(
                colors = ButtonDefaults.colors(
                    containerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                border = ButtonDefaults.border(
                    border = Border.None,
                    focusedBorder = Border(
                        border = BorderStroke(1.dp, Color.White)
                    )
                ),
                onClick = onCloseRequest
            ) {
                Icon(
                    imageVector = Icons.Outlined.Cancel,
                    contentDescription = "",
                    tint = Failed
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TvLazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            items(items.size){
                val s = items[it]

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(all = 24.dp)
                ) {
                    Text(
                        text = s,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        colors = ButtonDefaults.colors(
                            containerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        ),
                        border = ButtonDefaults.border(
                            border = Border.None,
                            focusedBorder = Border(
                                border = BorderStroke(1.dp, Color.White)
                            )
                        ),
                        onClick = { onDownloadClick(it) }) {
                        Icon(
                            imageVector = Icons.Filled.Download,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(24.dp))

                    IconButton(
                        colors = ButtonDefaults.colors(
                            containerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        ),
                        border = ButtonDefaults.border(
                            border = Border.None,
                            focusedBorder = Border(
                                border = BorderStroke(1.dp, Color.White)
                            )
                        ),
                        onClick = { onPlayClick(it) }) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}