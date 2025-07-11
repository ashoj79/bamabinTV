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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.ClosedCaption
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
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
import com.bamabin.tv_app.data.local.TempDB
import com.bamabin.tv_app.ui.theme.AgeRateBox
import com.bamabin.tv_app.ui.theme.Failed
import com.bamabin.tv_app.ui.widgeta.EpisodeBox
import com.bamabin.tv_app.ui.widgeta.ErrorDialog
import com.bamabin.tv_app.ui.widgeta.LoadingWidget
import com.bamabin.tv_app.utils.DataResult
import com.bamabin.tv_app.utils.Routes
import kotlinx.coroutines.delay

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
    val likeStatus by viewModel.likeStatus.collectAsState()
    val bottomSheetItems = viewModel.bottomSheetItems

    val scrollState = rememberTvLazyListState()
    val defaultFocusRequester = remember { FocusRequester() }
    val seasonsFocusRequester = remember { FocusRequester() }
    val showSeasons by viewModel.showSeasons.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val showLoginDialog by viewModel.showLoginDialog.collectAsState()
    val showBuyDialog by viewModel.showBuyDialog.collectAsState()

    LaunchedEffect(showSeasons) {
        if (showSeasons) {
            scrollState.scrollToItem(8)
            seasonsFocusRequester.requestFocus()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadWatchData()
    }

    LaunchedEffect(data) {
        if (data is DataResult.DataSuccess) {
            try {
                defaultFocusRequester.requestFocus()
            }catch (_:Exception){}
            delay(100)
            scrollState.scrollToItem(0)
        }
    }

    if (data is DataResult.DataLoading){
        LoadingWidget()
    } else if (data is DataResult.DataError){
        ErrorDialog(message = data.message, onCloseClick = {viewModel.closeError()}, onRetryClick = {viewModel.getDetails()})
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
                        .padding(start = 16.dp),
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
                                    containerColor = Color.Transparent,
                                    focusedContainerColor = MaterialTheme.colorScheme.primary
                                ),
                                border = ButtonDefaults.border(border = Border.None),
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
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }

                            if (it.isSeries){
                                Spacer(modifier = Modifier.width(24.dp))

                                Button(
                                    colors = ButtonDefaults.colors(
                                        containerColor = Color.Transparent,
                                        focusedContainerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    border = ButtonDefaults.border(border = Border.None),
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
                                            color = Color.White,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }

                            if (viewModel.shouldShowReplayButton()){
                                Spacer(modifier = Modifier.width(24.dp))

                                Button(
                                    colors = ButtonDefaults.colors(
                                        containerColor = Color.Transparent,
                                        focusedContainerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    border = ButtonDefaults.border(border = Border.None),
                                    shape = ButtonDefaults.shape(RoundedCornerShape(4.dp)),
                                    onClick = {
                                        viewModel.showMovieBottomSheet()
                                    }) {
                                    Row {
                                        Icon(
                                            imageVector = Icons.Filled.Refresh,
                                            contentDescription = "",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = "از سرگیری پخش",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }
                        }
                    }

                    if (TempDB.isLogin.value) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                colors = ButtonDefaults.colors(
                                    containerColor = Color.Transparent,
                                    focusedContainerColor = MaterialTheme.colorScheme.primary
                                ),
                                border = ButtonDefaults.border(border = Border.None),
                                shape = ButtonDefaults.shape(RoundedCornerShape(4.dp)),
                                onClick = { viewModel.updateWatchlist() }) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (it.isInWatchlist) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                        contentDescription = "",
                                        tint = Color.White
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = if (it.isInWatchlist) "حذف از علاقه‌مندی‌ها" else "افزودن به علاقه‌مندی‌ها",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            colors = ButtonDefaults.colors(
                                containerColor = Color.Transparent,
                                focusedContainerColor = MaterialTheme.colorScheme.primary
                            ),
                            border = ButtonDefaults.border(border = Border.None),
                            shape = ButtonDefaults.shape(RoundedCornerShape(4.dp)),
                            onClick = { navHostController.navigate("${Routes.COMMENTS.name}/${it.id}/${it.title}") }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.comments),
                                    contentDescription = "",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "نظرات کاربران",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }

                    if (TempDB.isLogin.value) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))

                            Row {
                                IconButton(
                                    colors = ButtonDefaults.colors(
                                        containerColor = Color.Black.copy(alpha = .5f),
                                        focusedContainerColor = Color.Black.copy(alpha = .5f),
                                    ),
                                    border = ButtonDefaults.border(
                                        border = Border.None,
                                        focusedBorder = Border(
                                            border = BorderStroke(1.dp, Color.White),
                                            shape = CircleShape
                                        )
                                    ),
                                    modifier = Modifier.padding(4.dp),
                                    onClick = { viewModel.like() }
                                ){
                                    Icon(
                                        imageVector = if (likeStatus == 1) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                        contentDescription = "",
                                        tint = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                IconButton(
                                    colors = ButtonDefaults.colors(
                                        containerColor = Color.Black.copy(alpha = .5f),
                                        focusedContainerColor = Color.Black.copy(alpha = .5f),
                                    ),
                                    border = ButtonDefaults.border(
                                        border = Border.None,
                                        focusedBorder = Border(
                                            border = BorderStroke(1.dp, Color.White),
                                            shape = CircleShape
                                        )
                                    ),
                                    modifier = Modifier.padding(4.dp),
                                    onClick = { viewModel.dislike() }
                                ){
                                    Icon(
                                        imageVector = if (likeStatus == 2) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
                                        contentDescription = "",
                                        tint = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "(${it.likeInfo.likes + it.likeInfo.dislikes} رای) ${it.likeInfo.percent}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White
                                )
                            )
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
                                    var modifier = Modifier.padding(horizontal = 4.dp)
                                    if (i == selectedSeason)
                                        modifier = modifier.focusRequester(seasonsFocusRequester)

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
                                        modifier = modifier,
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
                                            episode = it.seasons[selectedSeason].episodes[j],
                                            isWatched = viewModel.isEpisodeWatched(j)
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
            onPlayClick = {
                val route = viewModel.playItem(it)
                route?.let {
                    navHostController.navigate(it)
                }
            }
        )
    }

    if (errorMessage.isNotEmpty()) {
        ErrorDialog(message = errorMessage, onRetryClick = {viewModel.hideDialogs()}, onCloseClick = {viewModel.hideDialogs()})
    }

    if (showLoginDialog) {
        ErrorDialog(
            title = "ورود به حساب کاربری",
            message = "",
            firstBtnText = "ورود",
            secondBtnText = "بعداً، گشت‌و‌گذار در برنامه",
            showTelegramChannel = false,
            onCloseClick = { viewModel.hideDialogs() },
            onRetryClick = {
                viewModel.hideDialogs()
                navHostController.navigate(Routes.LOGIN.name)
            },
            onSecondBtnClicked = { viewModel.hideDialogs() }
        )
    }

    if (showBuyDialog) {
        ErrorDialog(
            title = "خرید اشتراک",
            message = "شما اشتراک فعال ندارین. برای تماشای فیلم باید اشتراک خریداری کنید",
            firstBtnText = "خرید اشتراک",
            secondBtnText = "بعداً",
            showTelegramChannel = false,
            onCloseClick = { viewModel.hideDialogs() },
            onRetryClick = {
                viewModel.hideDialogs()
                navHostController.navigate(Routes.SUBSCRIBE.name)
            },
            onSecondBtnClicked = { viewModel.hideDialogs() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTvMaterial3Api::class)
@Composable
private fun ItemsBottomSheet(
    title: String,
    items: List<String>,
    onCloseRequest: () -> Unit,
    onPlayClick: (index: Int) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var focusedItemIndex by remember { mutableIntStateOf(-1) }

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
                            color = if (focusedItemIndex == it) MaterialTheme.colorScheme.primary else Color(
                                0xFF2B2B2B
                            ),
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
                        modifier = Modifier.onFocusChanged { focusState ->
                            focusedItemIndex = if (focusState.isFocused) it
                            else -1
                        },
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