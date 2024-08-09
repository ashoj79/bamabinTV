package com.bamabin.tv_app.ui.screens.player

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bamabin.tv_app.R
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.TracksInfo
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionOverrides
import com.google.android.exoplayer2.trackselection.TrackSelectionOverrides.TrackSelectionOverride
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.CaptionStyleCompat
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _showController = MutableStateFlow(true)
    val showController: StateFlow<Boolean> = _showController

    private val _showSubtitleAlert = MutableStateFlow(false)
    val showSubtitleAlert: StateFlow<Boolean> = _showSubtitleAlert

    private val _showAudioAlert = MutableStateFlow(false)
    val showAudioAlert: StateFlow<Boolean> = _showAudioAlert

    private val _showSetting = MutableStateFlow(false)
    val showSetting: StateFlow<Boolean> = _showSetting

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _ready = MutableStateFlow(false)
    val ready:StateFlow<Boolean> = _ready

    private val _currentTime = MutableStateFlow("00:00:00")
    val currentTime: StateFlow<String> = _currentTime

    private val _subtitles = mutableStateListOf<String>()
    val subtitles: SnapshotStateList<String> = _subtitles

    private val _audios = mutableStateListOf<String>()
    val audios: SnapshotStateList<String> = _audios

    private val _currentPosition = MutableStateFlow(0f)
    val currentPosition: StateFlow<Float> = _currentPosition

    private val _subtitleStyle = MutableStateFlow<CaptionStyleCompat?>(null)
    val subtitleStyle: StateFlow<CaptionStyleCompat?> = _subtitleStyle

    private val _subtitleSize = MutableStateFlow(34f)
    val subtitleSize: StateFlow<Float> = _subtitleSize

    private val _aspectRatio = MutableStateFlow(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH)
    val aspectRatio: StateFlow<Int> = _aspectRatio

    private val _currentPlaybackSpeed = MutableStateFlow(1)
    val currentPlaybackSpeed: StateFlow<Int> = _currentPlaybackSpeed

    private val _currentBgColor = MutableStateFlow(0)
    val currentBgColor: StateFlow<Int> = _currentBgColor

    private val _currentTextColor = MutableStateFlow(0)
    val currentTextColor: StateFlow<Int> = _currentTextColor

    private val _currentFont = MutableStateFlow(0)
    val currentFont: StateFlow<Int> = _currentFont

    private val _currentSize = MutableStateFlow(1)
    val currentSize: StateFlow<Int> = _currentSize

    lateinit var player: ExoPlayer
    var duration = 1F
        private set
    var currentSubtitle = 0
        private set
    var currentAudio = 0
        private set
    var wholeTime = "00:00:00"
        private set
    val playbackSpeeds = listOf("0.75x", "1.0x", "1.25x", "1.5x", "2.0x")
    val backgroundColors = listOf("مشکی", "تیره کم‌رنگ", "بی‌رنگ")
    val textColors = listOf("سفید", "زرد", "آبی")
    val fonts = listOf("ایران‌سنس", "وزیر متن", "دانا")
    val sizes = listOf("کوچک", "متوسط", "بزرگ")

    private var timer: CountDownTimer? = null

    private val trackSelector = DefaultTrackSelector(context)
    private var isDataExtracted = false
    private val subtitlesId = mutableListOf<List<Int>>()
    private val audiosId = mutableListOf<List<Int>>()
    private val availableAspectRatio = listOf(
        AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH,
        AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT,
        AspectRatioFrameLayout.RESIZE_MODE_FILL,
        AspectRatioFrameLayout.RESIZE_MODE_FIT,
        AspectRatioFrameLayout.RESIZE_MODE_ZOOM,
    )

    init {
        setupPlayer()
        setSubtitleView()
    }

    fun showController() {
        if (!_showController.value)
            _showController.value = true

        timer?.cancel()
        startHideTimer()
    }

    fun playPause() {
        if (player.isPlaying)
            player.pause()
        else
            player.play()
    }

    fun showSetting() {
        player.pause()
        _showSetting.value = true
    }

    fun closeSetting() {
        _showSetting.value = false
        player.play()
    }

    fun showSubtitleAlert() {
        timer?.cancel()
        _showController.value = true

        if (player.isPlaying)player.pause()
        _showSubtitleAlert.value = true
    }

    fun showAudioAlert() {
        timer?.cancel()
        _showController.value = true

        if (player.isPlaying)player.pause()
        _showAudioAlert.value = true
    }

    fun setSubtitle(index: Int){
        if (index > subtitlesId.lastIndex) {
            player.trackSelectionParameters = player.trackSelectionParameters
                .buildUpon()
                .setDisabledTrackTypes(mutableSetOf(C.TRACK_TYPE_TEXT))
                .build()

            _showSubtitleAlert.value = false
            _showController.value = false
            currentSubtitle = index
            player.play()

            return
        }

        val mappedTrackInfo = trackSelector.currentMappedTrackInfo!!

        val overrides = mutableListOf<TrackSelectionOverride>()
        for (o in player.trackSelectionParameters.trackSelectionOverrides.asList()) {
            if (o.trackType != C.TRACK_TYPE_TEXT)
                overrides.add(o)
        }

        if (index <= subtitlesId.lastIndex) {
            val indices = subtitlesId[index]
            val o = TrackSelectionOverride(
                mappedTrackInfo.getTrackGroups(indices[0]).get(indices[1]),
                listOf(indices[2])
            )
            overrides.add(o)
        }

        val overrideBuilder = TrackSelectionOverrides.Builder()
        for (ov in overrides){
            overrideBuilder.setOverrideForType(ov)
        }

        player.trackSelectionParameters = player.trackSelectionParameters
            .buildUpon()
            .setDisabledTrackTypes(mutableSetOf())
            .setTrackSelectionOverrides(overrideBuilder.build())
            .build()

        _showSubtitleAlert.value = false
        _showController.value = false
        currentSubtitle = index
        player.play()
    }

    fun setAudio(index: Int){
        val indices = audiosId[index]
        val mappedTrackInfo = trackSelector.currentMappedTrackInfo!!

        val overrides = mutableListOf<TrackSelectionOverride>()
        for (o in player.trackSelectionParameters.trackSelectionOverrides.asList()) {
            if (o.trackType != C.TRACK_TYPE_AUDIO)
                overrides.add(o)
        }

        val o = TrackSelectionOverride(
            mappedTrackInfo.getTrackGroups(indices[0]).get(indices[1]),
            listOf(indices[2])
        )
        overrides.add(o)

        val overrideBuilder = TrackSelectionOverrides.Builder()
        for (ov in overrides){
            overrideBuilder.addOverride(ov)
        }

        player.trackSelectionParameters = player.trackSelectionParameters
            .buildUpon()
            .setTrackSelectionOverrides(overrideBuilder.build())
            .build()

        _showAudioAlert.value = false
        _showController.value = false
        currentAudio = index
        player.play()
    }

    fun forward() {
        showController()
        player.seekForward()
        timer?.cancel()
        startHideTimer()
    }

    fun reward() {
        showController()
        player.seekBack()
        timer?.cancel()
        startHideTimer()
    }

    fun changeAspectRatio() {
        val currentIndex = availableAspectRatio.indexOf(_aspectRatio.value)
        val nextIndex = if (currentIndex < availableAspectRatio.lastIndex) currentIndex + 1 else 0
        _aspectRatio.value = availableAspectRatio[nextIndex]
    }

    fun releasePlayer() {
        try {
            player.stop()
            player.release()
        } catch (_: Exception) {}
    }

    fun setTextColor(index: Int) {
        _currentTextColor.value = index
        setSubtitleView()
    }

    fun setBgColor(index: Int) {
        _currentBgColor.value = index
        setSubtitleView()
    }

    fun setFont(index: Int) {
        _currentFont.value = index
        setSubtitleView()
    }

    fun setTextSize(index: Int) {
        _currentSize.value = index
        _subtitleSize.value = when(index) {
            2 -> 48f
            1 -> 34f
            else -> 24f
        }
    }

    fun setPlaybackSpeed(index: Int) {
        _currentPlaybackSpeed.value = index
        val speed = playbackSpeeds[index].replace("x", "").toFloat()
        player.playbackParameters = PlaybackParameters(speed)
    }

    private fun setSubtitleView() {
        val textColor = when(_currentTextColor.value) {
            1 -> 0xFFFFEB3B
            2 -> 0xFF2196F3
            else -> 0xFFFFFFFF
        }.toInt()

        val bgColor = when(_currentBgColor.value) {
            1 -> 0x88000000
            2 -> 0x00000000
            else -> 0xFF000000
        }.toInt()

        val fontId: Int = when(_currentFont.value) {
            1 -> R.font.vazir
            2 -> R.font.dana
            else -> R.font.iransans
        }

        _subtitleStyle.value = CaptionStyleCompat(
            textColor,
            bgColor,
            0x00000000,
            CaptionStyleCompat.EDGE_TYPE_DROP_SHADOW,
            0xFF000000.toInt(),
            ResourcesCompat.getFont(context, fontId)
        )
    }

    private fun setupPlayer() {
        val mediaSource = DefaultMediaSourceFactory(context).createMediaSource(
            MediaItem.fromUri("https://dl4.uplodingdl.shop/Movies/2024/The.Garfield.Movie.tt5779228/Dubbed/The.Garfield.Movie.2024.720p.WEB-DL.x264-YIFY.Dual.Audio.SoftSub.BMB.15A66F30.mkv")
        )

        player = ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()
            .apply {
                setMediaSource(mediaSource)

                addListener(object : Player.Listener{
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)

                        _isPlaying.value = isPlaying
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        super.onPlaybackStateChanged(playbackState)
                        _isLoading.value = playbackState == Player.STATE_BUFFERING

                        if (!isDataExtracted && playbackState == Player.STATE_READY) extractVideoInfo()
                    }
                })

                playWhenReady = true
                prepare()
            }
    }

    private fun extractVideoInfo() {
        isDataExtracted = true
        _ready.value = true
        duration = player.duration.toFloat()
        wholeTime = formatTime(duration)
        updateDuration()
        startHideTimer()

        val mappedTrackInfo = trackSelector.currentMappedTrackInfo
        mappedTrackInfo?.let {
            val s = mutableListOf<String>()
            val a = mutableListOf<String>()
            for (i in 0 until it.rendererCount) {
                val trackGroups = it.getTrackGroups(i)
                for (j in 0 until trackGroups.length) {
                    val trackGroup = trackGroups[j]
                    for (k in 0 until trackGroup.length) {
                        val format = trackGroup.getFormat(k)
                        when (player.getRendererType(i)) {
                            C.TRACK_TYPE_AUDIO -> {
                                a.add(getTrackName(format))
                                audiosId.add(listOf(i, j, k))
                            }
                            C.TRACK_TYPE_TEXT -> {
                                s.add(getTrackName(format))
                                subtitlesId.add(listOf(i, j, k))
                            }
                        }
                    }
                }
            }
            _subtitles.addAll(s)
            _subtitles.add("غیرفعال")
            _audios.addAll(a)
        }
    }

    private fun getTrackName(format: Format): String {
        val specialNames = listOf("Gapfilm", "Alphamedia", "Soren", "Qualima", "Namava", "Filimo", "Fimnet", "Avaje", "Glory", "Moasese", "Filmnet", "IRIB", "Dubbed")
        val lbl = format.label

        lbl?.let {
            for (n in specialNames) {
                if (it.lowercase().contains(n.lowercase()))
                    return "$it - فارسی"
            }
        }

        val lngName = when((format.language ?: "").lowercase()) {
            "fa", "per" -> "فارسی"
            "und" -> "زبان اصلی"
            "en", "mul" -> "انگلیسی"
            "ar", "ara" -> "عربی"
            "ko", "kor" -> "کره‌ای"
            "ja", "jpn" -> "ژاپنی"
            "es", "spa" -> "اسپانیایی"
            "fr", "fre" -> "فرانسوی"
            "de", "ger" -> "آلمانی"
            "nl", "dut" -> "هلندی"
            "tr", "tur" -> "ترکی"
            "ta", "tam" -> "تمیل"
            "ru", "rus" -> "روسی"
            else -> ""
        }

        return lbl?.let { "$it - $lngName" } ?: lngName
    }

    private fun updateDuration() = viewModelScope.launch {
        while (true){
            if (player.isPlaying){
                _currentTime.value = formatTime(player.currentPosition.toFloat())
                _currentPosition.value = player.currentPosition.toFloat()
            }
            delay(1000)
        }
    }

    private fun startHideTimer() {
        timer = object : CountDownTimer(5000L, 1000L){
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                _showController.value = false
            }
        }
        timer?.start()
    }

    @SuppressLint("DefaultLocale")
    private fun formatTime(milliseconds: Float): String {
        val totalSeconds = milliseconds / 1000
        val hours = (totalSeconds / 3600).toInt()
        val minutes = ((totalSeconds % 3600) / 60).toInt()
        val seconds = (totalSeconds % 60).toInt()

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}