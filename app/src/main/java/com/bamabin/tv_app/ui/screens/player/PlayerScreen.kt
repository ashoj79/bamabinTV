package com.bamabin.tv_app.ui.screens.player

import android.app.Activity
import android.content.Context
import android.view.KeyEvent
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.AspectRatio
import androidx.compose.material.icons.outlined.FontDownload
import androidx.compose.material.icons.outlined.FormatSize
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SlowMotionVideo
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import com.bamabin.tv_app.ui.widgeta.LoadingWidget
import com.google.android.exoplayer2.text.Cue
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.ui.SubtitleView
import kotlinx.coroutines.delay
import okhttp3.internal.http2.Settings

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    context: Context = LocalContext.current,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val defaultFocusRequester = remember { FocusRequester() }

    val isLoading by viewModel.isLoading.collectAsState()
    val ready by viewModel.ready.collectAsState()
    val showController by viewModel.showController.collectAsState()
    val showSetting by viewModel.showSetting.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val currentTime by viewModel.currentTime.collectAsState()
    val showAudioAlert by viewModel.showAudioAlert.collectAsState()
    val showSubtitleAlert by viewModel.showSubtitleAlert.collectAsState()
    val subtitleStyle by viewModel.subtitleStyle.collectAsState()
    val aspectRatio by viewModel.aspectRatio.collectAsState()
    val subtitleSize by viewModel.subtitleSize.collectAsState()
    val subtitles = viewModel.subtitles.toList()
    val audios = viewModel.audios.toList()

    val interactiveSource = remember { MutableInteractionSource() }
    var isSliderFocused by remember { mutableStateOf(false) }

    val offset by animateDpAsState(
        targetValue = if (showController) 0.dp else 200.dp,
        animationSpec = TweenSpec(durationMillis = 300), label = ""
    )

    val centerOptionsAlpha by animateFloatAsState(
        targetValue = if (showController) 1f else 0f,
        animationSpec = TweenSpec(durationMillis = 300), label = ""
    )

    DisposableEffect(Unit) {
        val window = (context as Activity).window
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            viewModel.releasePlayer()
        }
    }

    LaunchedEffect(ready, showSetting) {
        if (ready && !showSetting){
            delay(100)
            defaultFocusRequester.requestFocus()
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .focusable()
            .onKeyEvent {
                viewModel.showController()
                false
            }
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                StyledPlayerView(context).apply {
                    player = viewModel.player
                    resizeMode = aspectRatio
                    useController = false

                    subtitleStyle?.let {
                        subtitleView?.setStyle(it)
                    }
                    subtitleView?.setFixedTextSize(1, subtitleSize)
                }
            },
            update = {view ->
                view.resizeMode = aspectRatio
                subtitleStyle?.let {
                    view.subtitleView?.setStyle(it)
                }
                view.subtitleView?.setFixedTextSize(1, subtitleSize)
            }
        )

        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer(alpha = centerOptionsAlpha),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                colors = ButtonDefaults.colors(
                    containerColor = Color.Transparent,
                    focusedContainerColor = Color.White.copy(0.3f),
                ),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                enabled = !showSetting,
                onClick = { viewModel.forward() }) {
                Icon(
                    imageVector = Icons.Filled.FastForward,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                IconButton(
                    colors = ButtonDefaults.colors(
                        containerColor = Color.Transparent,
                        focusedContainerColor = Color.White.copy(0.3f),
                    ),
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(48.dp)
                        .focusRequester(defaultFocusRequester),
                    enabled = !showSetting,
                    onClick = { viewModel.playPause() }) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(
                colors = ButtonDefaults.colors(
                    containerColor = Color.Transparent,
                    focusedContainerColor = Color.White.copy(0.3f),
                ),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                enabled = !showSetting,
                onClick = { viewModel.reward() }) {
                Icon(
                    imageVector = Icons.Filled.FastRewind,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.size(56.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(82.dp)
                .align(Alignment.TopEnd)
                .offset(y = -offset)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.9f),
                            Color.Black.copy(alpha = 0f)
                        )
                    )
                )
                .padding(all = 16.dp)
        ) {
            Text(
                text = "عنوان فیلم",
                style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
            )
        }
        
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .align(Alignment.BottomStart)
            .offset(y = offset)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0f),
                        Color.Black.copy(alpha = 0.9f),
                    ),
                )
            )
            .padding(all = 16.dp)
        ) {
            Text(
                text = currentTime,
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                modifier = Modifier.align(Alignment.TopEnd)
            )
            Text(
                text = viewModel.wholeTime,
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                modifier = Modifier.align(Alignment.TopStart)
            )
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Slider(
                    value = currentPosition,
                    valueRange = 0f..viewModel.duration,
                    onValueChange = {},
                    enabled = viewModel.duration > 1 && !showSetting,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = Color.White.copy(alpha = 0.1f),
                    ),
                    track = { state ->
                        val activeWidth = (screenWidth - 32.dp) * (state.value / state.valueRange.endInclusive)

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(if (isSliderFocused) 12.dp else 4.dp)
                                .background(
                                    color = Color.White.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(if (isSliderFocused) 6.dp else 2.dp)
                                )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(activeWidth)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(if (isSliderFocused) 6.dp else 2.dp)
                                    )
                            )
                        }
                    },
                    thumb = {
                        Box(modifier = Modifier
                            .size(if (isSliderFocused) 16.dp else 0.dp)
                            .background(
                                color = if (isSliderFocused) Color.White else Color.Transparent,
                                shape = CircleShape
                            )
                        )
                    },
                    interactionSource = interactiveSource,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 48.dp)
                        .onFocusChanged {
                            isSliderFocused = it.isFocused
                        }
                        .focusable()
                        .onKeyEvent {
                            if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DPAD_LEFT && it.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                                viewModel.reward()
                                return@onKeyEvent true
                            }
                            if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && it.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                                viewModel.forward()
                                return@onKeyEvent true
                            }

                            return@onKeyEvent false
                        }
                )
            }

            IconButton(
                colors = ButtonDefaults.colors(
                    containerColor = Color.Transparent,
                    focusedContainerColor = Color.White.copy(0.3f),
                ),
                enabled = !showSetting,
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = { viewModel.changeAspectRatio() }) {
                Icon(imageVector = Icons.Outlined.AspectRatio, contentDescription = "", tint = Color.White)
            }

            IconButton(
                colors = ButtonDefaults.colors(
                    containerColor = Color.Transparent,
                    focusedContainerColor = Color.White.copy(0.3f),
                ),
                enabled = !showSetting,
                modifier = Modifier.align(Alignment.BottomStart),
                onClick = { viewModel.showSetting() }) {
                Icon(imageVector = Icons.Outlined.Settings, contentDescription = "", tint = Color.White)
            }

            if (audios.size > 1) {
                IconButton(
                    colors = ButtonDefaults.colors(
                        containerColor = Color.Transparent,
                        focusedContainerColor = Color.White.copy(0.3f),
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = 48.dp),
                    enabled = !showSetting,
                    onClick = { viewModel.showAudioAlert() }) {
                    Icon(imageVector = Icons.Filled.Mic, contentDescription = "", tint = Color.White)
                }
            }

            if (subtitles.size > 1) {
                IconButton(
                    colors = ButtonDefaults.colors(
                        containerColor = Color.Transparent,
                        focusedContainerColor = Color.White.copy(0.3f),
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(
                            x = if (audios.size > 1) 96.dp else 48.dp
                        ),
                    enabled = !showSetting,
                    onClick = { viewModel.showSubtitleAlert() }) {
                    Icon(imageVector = Icons.Filled.ClosedCaption, contentDescription = "", tint = Color.White)
                }
            }
        }

        AnimatedVisibility(
            visible = showSetting,
            enter = slideInHorizontally(
                initialOffsetX = { 350 },
                animationSpec = tween(durationMillis = 300)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { 350 },
                animationSpec = tween(durationMillis = 300)
            ),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            SettingPanel()
        }
    }

    if (showSubtitleAlert) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            SubtitleAlert()
        }
    }

    if (showAudioAlert) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            AudioAlert()
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun SubtitleAlert(viewModel: PlayerViewModel = hiltViewModel()) {
    val subtitles = viewModel.subtitles
    var focusedIndex by remember { mutableIntStateOf(viewModel.currentSubtitle) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        try {
            focusRequester.requestFocus()
        }catch (_: Exception){}
    }

    AlertDialog(
        containerColor = Color(0xFF2B2B2B),
        onDismissRequest = {},
        confirmButton = {},
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "انتخاب زیرنویس",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.W700, color = Color.White),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                for (i in subtitles.indices) {
                    var modifier = Modifier
                        .padding(all = 8.dp)
                        .onFocusChanged {
                            if (it.isFocused) focusedIndex = i
                        }

                    if (i == viewModel.currentSubtitle)
                        modifier = modifier.focusRequester(focusRequester)

                    Card(
                        colors = CardDefaults.colors(
                            containerColor = Color.Transparent,
                            focusedContainerColor = Color.White
                        ),
                        border = CardDefaults.border(
                            focusedBorder = Border.None
                        ),
                        modifier = modifier,
                        onClick = { viewModel.setSubtitle(i) }) {
                        Text(
                            text = subtitles[i],
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = if (focusedIndex == i) Color.Black else Color.Gray,
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(all = 8.dp)
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun AudioAlert(viewModel: PlayerViewModel = hiltViewModel()) {
    val audios = viewModel.audios
    var focusedIndex by remember { mutableIntStateOf(viewModel.currentAudio) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        try {
            focusRequester.requestFocus()
        }catch (_: Exception){}
    }

    AlertDialog(
        containerColor = Color(0xFF2B2B2B),
        onDismissRequest = {},
        confirmButton = {},
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "انتخاب صدا",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.W700, color = Color.White),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                for (i in audios.indices) {
                    var modifier = Modifier
                        .padding(all = 8.dp)
                        .onFocusChanged {
                            if (it.isFocused) focusedIndex = i
                        }

                    if (i == viewModel.currentAudio)
                        modifier = modifier.focusRequester(focusRequester)

                    Card(
                        colors = CardDefaults.colors(
                            containerColor = Color.Transparent,
                            focusedContainerColor = Color.White
                        ),
                        border = CardDefaults.border(
                            focusedBorder = Border.None
                        ),
                        modifier = modifier,
                        onClick = { viewModel.setAudio(i) }) {
                        Text(
                            text = audios[i],
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = if (focusedIndex == i) Color.Black else Color.Gray,
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(all = 8.dp)
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun SettingPanel(
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val defaultFocusRequester = remember { FocusRequester() }

    val speed by viewModel.currentPlaybackSpeed.collectAsState()
    val background by viewModel.currentBgColor.collectAsState()
    val color by viewModel.currentTextColor.collectAsState()
    val font by viewModel.currentFont.collectAsState()
    val size by viewModel.currentSize.collectAsState()

    LaunchedEffect(Unit) {
        defaultFocusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(350.dp)
            .background(Color.Black.copy(alpha = 0.8f))
            .padding(all = 16.dp)
            .focusRequester(defaultFocusRequester)
            .onKeyEvent { false }
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Icon(imageVector = Icons.Outlined.Settings, contentDescription = "", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "تنظیمات",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                )
            }

            Button(
                colors = ButtonDefaults.colors(
                    containerColor = Color(0xFFCECECE)
                ),
                border = ButtonDefaults.border(
                    focusedBorder = Border(
                        border = BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ),
                    border = Border.None
                ),
                shape = ButtonDefaults.shape(
                    shape = RoundedCornerShape(8.dp)
                ),
                onClick = { viewModel.closeSetting() }) {
                Text(
                    text = "بستن",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        HorizontalDivider(
            thickness = 2.dp,
            color = Color.White.copy(alpha = 0.2f),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        SettingsRow(
            icon = Icons.Outlined.SlowMotionVideo,
            title = "سرعت پخش",
            items = viewModel.playbackSpeeds,
            selectedItem = speed
        ) {
            viewModel.setPlaybackSpeed(it)
        }

        SettingsRow(
            icon = Icons.Outlined.Palette,
            title = "رنگ پس زمینه زیرنویس",
            items = viewModel.backgroundColors,
            selectedItem = background
        ) {
            viewModel.setBgColor(it)
        }

        SettingsRow(
            icon = Icons.Outlined.Palette,
            title = "رنگ زیرنویس",
            items = viewModel.textColors,
            selectedItem = color
        ) {
            viewModel.setTextColor(it)
        }

        SettingsRow(
            icon = Icons.Outlined.FontDownload,
            title = "فونت",
            items = viewModel.fonts,
            selectedItem = font
        ) {
            viewModel.setFont(it)
        }

        SettingsRow(
            icon = Icons.Outlined.FormatSize,
            title = "اندازه متن",
            items = viewModel.sizes,
            selectedItem = size
        ) {
            viewModel.setTextSize(it)
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    items: List<String>,
    selectedItem: Int,
    onClick: (index: Int) -> Unit
) {
    Row {
        Icon(imageVector = icon, contentDescription = "", tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    TvLazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(items.size) {
            Button(
                colors = ButtonDefaults.colors(
                    containerColor = if(selectedItem == it) MaterialTheme.colorScheme.primary else Color.Black.copy(alpha = 0.2f),
                    focusedContainerColor = if(selectedItem == it) MaterialTheme.colorScheme.primary else Color.Black.copy(alpha = 0.2f),
                ),
                border = ButtonDefaults.border(
                    focusedBorder = Border(
                        border = BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ),
                    border = Border.None
                ),
                shape = ButtonDefaults.shape(shape = RoundedCornerShape(8.dp)),
                modifier = Modifier.padding(horizontal = 4.dp),
                onClick = { onClick(it) }) {
                Text(
                    items[it],
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White
                    )
                )
            }
        }
    }

    HorizontalDivider(
        thickness = 2.dp,
        color = Color.White.copy(alpha = 0.2f),
        modifier = Modifier.padding(vertical = 16.dp)
    )
}