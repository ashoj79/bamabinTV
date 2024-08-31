package com.bamabin.tv_app.ui.screens.player

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bamabin.tv_app.R
import com.bamabin.tv_app.data.local.TempDB
import com.bamabin.tv_app.data.local.database.model.WatchData
import com.bamabin.tv_app.data.local.database.model.WatchedEpisode
import com.bamabin.tv_app.data.remote.model.videos.EpisodeInfo
import com.bamabin.tv_app.repo.AppRepository
import com.bamabin.tv_app.repo.VideosRepository
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.text.Cue
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionOverrides
import com.google.android.exoplayer2.trackselection.TrackSelectionOverrides.TrackSelectionOverride
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.CaptionStyleCompat
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@HiltViewModel
class PlayerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val appRepository: AppRepository,
    private val videosRepository: VideosRepository
): ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _showController = MutableStateFlow(true)
    val showController: StateFlow<Boolean> = _showController

    private val _showSubtitleAlert = MutableStateFlow(false)
    val showSubtitleAlert: StateFlow<Boolean> = _showSubtitleAlert

    private val _showAudioAlert = MutableStateFlow(false)
    val showAudioAlert: StateFlow<Boolean> = _showAudioAlert

    private val _showQualityAlert = MutableStateFlow(false)
    val showQualityAlert: StateFlow<Boolean> = _showQualityAlert

    private val _showSetting = MutableStateFlow(false)
    val showSetting: StateFlow<Boolean> = _showSetting

    private val _showSeasons = MutableStateFlow(false)
    val showSeasons: StateFlow<Boolean> = _showSeasons

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _ready = MutableStateFlow(false)
    val ready: StateFlow<Boolean> = _ready

    private val _currentTime = MutableStateFlow("00:00:00")
    val currentTime: StateFlow<String> = _currentTime

    private val _subtitles = mutableStateListOf<String>()
    val subtitles: SnapshotStateList<String> = _subtitles

    private val _audios = mutableStateListOf<String>()
    val audios: SnapshotStateList<String> = _audios

    private val _currentPosition = MutableStateFlow(0f)
    val currentPosition: StateFlow<Float> = _currentPosition

    private val _subtitleSize = MutableStateFlow(34f)
    val subtitleSize: StateFlow<Float> = _subtitleSize

    private val _aspectRatio = MutableStateFlow(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH)
    val aspectRatio: StateFlow<Int> = _aspectRatio

    private val _textColor = MutableStateFlow(Color.White)
    val textColor: StateFlow<Color> = _textColor

    private val _bgColor = MutableStateFlow(Color.Black)
    val bgColor: StateFlow<Color> = _bgColor

    private val _fontId = MutableStateFlow(R.font.iransans)
    val fontId: StateFlow<Int> = _fontId

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

    private val _selectedSeasonIndex = MutableStateFlow(0)
    val selectedSeasonIndex: StateFlow<Int> = _selectedSeasonIndex

    private val _subtitleText = MutableStateFlow("")
    val subtitleText: StateFlow<String> = _subtitleText

    lateinit var player: ExoPlayer
    var duration = 1F
        private set
    var currentSubtitle = 0
        private set
    var currentAudio = 0
        private set
    var wholeTime = "00:00:00"
        private set
    var qualityIndex = 0
        private set
    val title: String
        get() {
            return if (TempDB.selectedPost!!.isSeries) {
                "${TempDB.selectedPost!!.title} - فصل ${TempDB.selectedPost!!.seasons!![seasonIndex].name} قسمت ${TempDB.selectedPost!!.seasons!![seasonIndex].episodes[episodeIndex].name}"
            } else {
                TempDB.selectedPost!!.title
            }
        }
    val isSeries: Boolean
        get() = TempDB.selectedPost!!.isSeries
    val seasonsName: List<String>
        get() = TempDB.selectedPost!!.seasons!!.map { it.name }
    val episodes: List<EpisodeInfo>
        get() = TempDB.selectedPost!!.seasons!![_selectedSeasonIndex.value].episodes
    val thumbnail: String
        get() = TempDB.selectedPost!!.bgThumbnail
    val qualities = mutableMapOf<Int, String>()

    val playbackSpeeds = listOf("0.75x", "1.0x", "1.25x", "1.5x", "2.0x")
    val backgroundColors = listOf("مشکی", "تیره کم‌رنگ", "بی‌رنگ")
    val textColors = listOf("سفید", "زرد", "آبی")
    val fonts = listOf("ایران‌سنس", "وزیر متن", "دانا")
    val sizes = listOf("کوچک", "متوسط", "بزرگ")

    private var seasonIndex: Int = savedStateHandle["season"] ?: -1
    private var episodeIndex: Int = savedStateHandle["episode"] ?: -1
    private var itemIndex: Int = savedStateHandle["item"] ?: -1

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
    private var timerJob: Job? = null
    private var isEnd = false
    private var watchData: WatchData? = null
    private var previousPosition = 0L
    private val watchedEpisodes = mutableListOf<WatchedEpisode>()

    private val subtitleBeginSpecificChars = listOf(".", "?", "!", ",", "،", "؟", "(", ")", "«", "»", "\"", " ")
    private val subtitleEndSpecificChars = listOf("-", " ", "(", ")", "«", "»", "\"")

    init {
        viewModelScope.launch {
            watchData = videosRepository.getWatchData(TempDB.selectedPost!!.id)
            watchedEpisodes.addAll(videosRepository.getWatchedEpisodes(TempDB.selectedPost!!.id))
        }
        loadDefaultSubtitleConfigs()
        setupPlayer()
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

        if (player.isPlaying) player.pause()
        _showSubtitleAlert.value = true
    }

    fun showAudioAlert() {
        timer?.cancel()
        _showController.value = true

        if (player.isPlaying) player.pause()
        _showAudioAlert.value = true
    }

    fun setSubtitle(index: Int) {
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
        for (ov in overrides) {
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

    fun setAudio(index: Int) {
        if (index > audiosId.lastIndex) return

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
        for (ov in overrides) {
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
        val cPos = player.currentPosition
        player.seekTo(if(cPos > player.duration - 11000) player.duration else cPos + 10000)
        timer?.cancel()
        startHideTimer()
    }

    fun reward() {
        val cPos = player.currentPosition
        player.seekTo(if(cPos < 11000) 0L else cPos - 10000)
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
        _subtitleSize.value = when (index) {
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

    fun showQualities() {
        val q =
            if (TempDB.selectedPost!!.isSeries) {
                TempDB.selectedPost!!.seasons!![seasonIndex].episodes[episodeIndex].items.getQualities(itemIndex)
            } else {
                TempDB.selectedPost!!.movieDownloadBox!!.getQualities(itemIndex)
            }

        val data =
            if (TempDB.selectedPost!!.isSeries) {
                TempDB.selectedPost!!.seasons!![seasonIndex].episodes[episodeIndex].items.getItemInfo(itemIndex)
            } else {
                TempDB.selectedPost!!.movieDownloadBox!!.getItemInfo(itemIndex)
            }

        if (player.isPlaying) player.pause()
        qualityIndex = q.values.indexOf(data[1])
        qualities.clear()
        q.forEach { (t, u) ->
            qualities[t] = u
        }
        _showQualityAlert.value = true
    }

    fun changeQuality(index: Int) {
        _showQualityAlert.value = false
        val iIndex = qualities.keys.elementAt(index)

        if (iIndex == itemIndex) {
            player.play()
            return
        }

        itemIndex = iIndex
        previousPosition = player.currentPosition
        isDataExtracted = false
        setupPlayer()
    }

    fun showSeasons() {
        if (player.isPlaying) player.pause()
        _selectedSeasonIndex.value = seasonIndex
        _showSeasons.value = true
    }

    fun hideSeasons() {
        try {
            if (!isEnd) player.play()
        } catch (_: Exception){}
        _showSeasons.value = false
    }

    fun changeSelectedSeason(index: Int) {
        _selectedSeasonIndex.value = index
    }

    fun changeEpisode(index: Int, season: Int = -1) {
        val newSeason = if (season > -1) season else _selectedSeasonIndex.value

        val data =
            TempDB.selectedPost!!.seasons!![seasonIndex].episodes[episodeIndex].items.getItemInfo(
                itemIndex
            )
        val newItemIndex =
            TempDB.selectedPost!!.seasons!![newSeason].episodes[index].items.getSimilarIndex(
                data
            )
        if (newItemIndex == -1) return

        _ready.value = false
        _showSeasons.value = false
        seasonIndex = newSeason
        episodeIndex = index
        itemIndex = newItemIndex

        player.stop()
        _showController.value = true
        isDataExtracted = false
        duration = 0f
        wholeTime = "00:00:00"
        _currentTime.value = "00:00:00"
        audiosId.clear()
        subtitlesId.clear()
        _subtitles.clear()
        _audios.clear()

        setupPlayer()
    }

    fun goToNextEpisode() {
        if (!TempDB.selectedPost!!.isSeries) return

        var newSeasonIndex = -1
        var newEpisodeIndex = -1
        if (TempDB.selectedPost!!.seasons!![seasonIndex].episodes.lastIndex > episodeIndex) {
            newSeasonIndex = seasonIndex
            newEpisodeIndex = episodeIndex + 1
        } else if (seasonIndex > 0) {
            newSeasonIndex = seasonIndex - 1
            newEpisodeIndex = 0
        }

        if (newSeasonIndex > -1 && newEpisodeIndex > -1) {
            changeEpisode(newEpisodeIndex, newSeasonIndex)
        }
    }

    fun getIntent(): Intent {
        player.pause()
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndTypeAndNormalize(Uri.parse(getPlayLink()), "video/*")
        }

        return Intent.createChooser(intent, "پلیر را انتخاب کنید")
    }

    fun isEpisodeWatched(index: Int): Boolean {
        return watchedEpisodes.any { it.season == _selectedSeasonIndex.value && it.episode == index }
    }

    private fun setSubtitleView() {
        _textColor.value = Color(
            when (_currentTextColor.value) {
                1 -> 0xFFFFEB3B
                2 -> 0xFF2196F3
                else -> 0xFFFFFFFF
            }.toInt()
        )

        _bgColor.value = Color(
            when (_currentBgColor.value) {
                1 -> 0x88000000
                2 -> 0x00000000
                else -> 0xFF000000
            }.toInt()
        )

        _fontId.value = when (_currentFont.value) {
            1 -> R.font.vazir
            2 -> R.font.dana
            else -> R.font.iransans
        }
    }

    private fun setupPlayer() {
        val mediaSource = DefaultMediaSourceFactory(context).createMediaSource(
            MediaItem.fromUri(getPlayLink())
        )

        player = ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()
            .apply {
                setMediaSource(mediaSource)

                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)

                        _isPlaying.value = isPlaying
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        super.onPlaybackStateChanged(playbackState)
                        _isLoading.value = playbackState == Player.STATE_BUFFERING
                        isEnd = playbackState == Player.STATE_ENDED
                        if (isEnd) afterEnding()

                        if (!isDataExtracted && playbackState == Player.STATE_READY) extractVideoInfo()
                    }

                    override fun onCues(cues: MutableList<Cue>) {
                        super.onCues(cues)
                        _subtitleText.value = cues.joinToString("\n") { cue ->
                            val text = cue.text?.toString() ?: ""
                            val lines = text.split("\n").toMutableList()
                            for (l in lines.indices) {
                                var line = lines[l]
                                var beginChars = ""
                                var endChars = ""

                                for (i in line.indices) {
                                    if (subtitleBeginSpecificChars.contains(line[i].toString())){
                                        beginChars += line[i]
                                    } else {
                                        break
                                    }
                                }

                                for (i in line.lastIndex downTo 0) {
                                    if (subtitleEndSpecificChars.contains(line[i].toString())){
                                        endChars += line[i]
                                    } else {
                                        break
                                    }
                                }

                                line = line.trimStart(*beginChars.toCharArray()).trimEnd(*endChars.toCharArray())

                                beginChars = beginChars.replace("(", ")").replace("«", "»").reversed()
                                endChars = endChars.replace(")", "(").replace("»", "«").reversed()

                                lines[l] = "$endChars$line$beginChars"
                            }

                            lines.joinToString("\n")
                        }
                    }
                })

                playWhenReady = true
                prepare()
            }
    }

    private fun extractVideoInfo() {
        isDataExtracted = true
        if (!_ready.value) _ready.value = true
        duration = player.duration.toFloat()
        wholeTime = formatTime(duration)

        setPreviousWatchData()
        updateDuration()
        startHideTimer()
        saveWatchedEpisode()

        audiosId.clear()
        subtitlesId.clear()
        audios.clear()
        subtitles.clear()

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
        val specialNames = listOf(
            "Gapfilm",
            "Alphamedia",
            "Alpha Media",
            "Soren",
            "Qualima",
            "Namava",
            "Filimo",
            "Fimnet",
            "Avaje",
            "Glory",
            "Moasese",
            "Filmnet",
            "IRIB",
            "Dubbed"
        )
        val lbl = format.label

        lbl?.let {
            for (n in specialNames) {
                if (it.lowercase().contains(n.lowercase()))
                    return "$it - فارسی"
            }
        }

        val lngName = when ((format.language ?: "").lowercase()) {
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

    private fun updateDuration() {
        timerJob = viewModelScope.launch {
            while (true) {
                if (player.isPlaying) {
                    _currentTime.value = formatTime(player.currentPosition.toFloat())
                    _currentPosition.value = player.currentPosition.toFloat()
                }
                delay(1000)
            }
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

    private fun loadDefaultSubtitleConfigs() = viewModelScope.launch {
        _currentTextColor.value = appRepository.getTextColor()
        _currentBgColor.value = appRepository.getBgColor()
        _currentSize.value = appRepository.getSize()
        _currentFont.value = appRepository.getFont()
        setSubtitleView()
    }

    private fun getPlayLink(): String {
        TempDB.selectedPost?.let {
            return if (it.isSeries){
                it.seasons!![seasonIndex].episodes[episodeIndex].items.getAllItemsLink()[itemIndex]
            } else {
                it.movieDownloadBox!!.getAllItemsLink()[itemIndex]
            }
        }

        return ""
    }

    private fun setPreviousWatchData() = viewModelScope.launch {
        delay(1000)
        if (previousPosition > 0 && player.duration >= previousPosition){
            player.seekTo(previousPosition)
        }
        watchData?.let {
            if (
                duration > it.time.toFloat() &&
                it.time > 0 &&
                previousPosition == 0L &&
                ((TempDB.selectedPost!!.isSeries && seasonIndex == it.season && episodeIndex == it.episode) ||
                        (!TempDB.selectedPost!!.isSeries))
            ) {
                player.seekTo(it.time)
            }

            if (it.subtitleTrack > -1) setSubtitle(it.subtitleTrack)
            if (it.audioTrack > -1) setAudio(it.audioTrack)
        }
    }

    private fun saveWatchedEpisode()= viewModelScope.launch {
        if (TempDB.selectedPost!!.isSeries) {
            if (watchedEpisodes.any { it.season == seasonIndex && it.episode == episodeIndex })
                return@launch

            val watchedEpisode = WatchedEpisode(
                id = TempDB.selectedPost!!.id,
                season = seasonIndex,
                episode = episodeIndex
            )
            videosRepository.saveWatchedEpisode(watchedEpisode)
            watchedEpisodes.add(watchedEpisode)
        }
    }

    private fun afterEnding()= viewModelScope.launch {
        if (TempDB.selectedPost!!.isSeries) {
            var newSeasonIndex = -1
            var newEpisodeIndex = -1
            if (TempDB.selectedPost!!.seasons!![seasonIndex].episodes.lastIndex > episodeIndex) {
                newSeasonIndex = seasonIndex
                newEpisodeIndex = episodeIndex + 1
            } else if (seasonIndex > 0) {
                newSeasonIndex = seasonIndex - 1
                newEpisodeIndex = 0
            } else if (watchData != null) {
                videosRepository.deleteWatchData(watchData!!)
            }

            if (newSeasonIndex > -1 && newEpisodeIndex > -1) {
                val newItemIndex = TempDB.selectedPost!!.seasons!![newSeasonIndex].episodes[newEpisodeIndex].items.getDefaultItemIndex()
                val data = TempDB.selectedPost!!.seasons!![newSeasonIndex].episodes[newEpisodeIndex].items.getItemInfo(newItemIndex)

                videosRepository.saveWatchData(
                    WatchData(
                        TempDB.selectedPost!!.id,
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        0,
                        seasonIndex,
                        newEpisodeIndex
                    )
                )

                showSeasons()
            }
        } else if (watchData != null) {
            videosRepository.deleteWatchData(watchData!!)
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (!isEnd) {
            runBlocking {
                val data =
                    if (TempDB.selectedPost!!.isSeries)
                        TempDB.selectedPost!!.seasons!![seasonIndex].episodes[episodeIndex].items.getItemInfo(itemIndex)
                    else
                        TempDB.selectedPost!!.movieDownloadBox!!.getItemInfo(itemIndex)

                videosRepository.saveWatchData(
                    WatchData(
                        TempDB.selectedPost!!.id,
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        _currentPosition.value.toLong(),
                        seasonIndex,
                        episodeIndex,
                        currentAudio,
                        currentSubtitle
                    )
                )
            }
        }
    }
}