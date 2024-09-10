package com.bamabin.tv_app.ui.screens.post_details

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bamabin.tv_app.data.local.TempDB
import com.bamabin.tv_app.data.local.database.model.WatchData
import com.bamabin.tv_app.data.local.database.model.WatchedEpisode
import com.bamabin.tv_app.data.remote.model.videos.PostDetails
import com.bamabin.tv_app.repo.VideosRepository
import com.bamabin.tv_app.utils.DataResult
import com.bamabin.tv_app.utils.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val repository: VideosRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val id: Int = savedStateHandle["id"] ?: 0
    private var selectedEpisode = -1
    private var watchData: WatchData? = null
    private var isWatched = false
    private val watchedEpisodes = mutableListOf<WatchedEpisode>()

    var bottomSheetTitle = ""
        private set

    private val _data = MutableStateFlow<DataResult<PostDetails>>(DataResult.DataLoading())
    val data: StateFlow<DataResult<PostDetails>> = _data

    private val _selectedSeason = MutableStateFlow(0)
    val selectedSeason: StateFlow<Int> = _selectedSeason

    private val _likeStatus = MutableStateFlow(0)
    val likeStatus: StateFlow<Int> = _likeStatus

    private val _bottomSheetItems = mutableStateListOf<String>()
    val bottomSheetItems: SnapshotStateList<String> = _bottomSheetItems

    private val _showSeasons = MutableStateFlow(false)
    val showSeasons: StateFlow<Boolean> = _showSeasons

    private val _errorMessage = MutableStateFlow("")
    val errorMessage:StateFlow<String> = _errorMessage

    private val _showLoginDialog = MutableStateFlow(false)
    val showLoginDialog: StateFlow<Boolean> = _showLoginDialog

    private val _showBuyDialog = MutableStateFlow(false)
    val showBuyDialog: StateFlow<Boolean> = _showBuyDialog

    init {
        getDetails()
    }

    fun getDetails() = viewModelScope.launch{
        watchedEpisodes.clear()
        watchedEpisodes.addAll(repository.getWatchedEpisodes(id))
        watchData = repository.getWatchData(id)
        isWatched = watchData != null
        _data.value = DataResult.DataLoading()
        _data.value = repository.getPostDetails(id)
    }

    fun closeError() {
        _data.value = DataResult.DataLoading()
    }

    fun loadWatchData() = viewModelScope.launch {
        delay(1000)
        watchedEpisodes.clear()
        watchedEpisodes.addAll(repository.getWatchedEpisodes(id))
        watchData = repository.getWatchData(id)
        isWatched = watchData != null
    }

    fun isEpisodeWatched(index: Int): Boolean {
        return watchedEpisodes.any { it.season == _selectedSeason.value && it.episode == index }
    }

    fun getPlayButtonText(): String{
        var text = ""
        text += if (!isWatched) "پخش " else "ادامه "
        text += if (_data.value.data!!.isSeries) "سریال" else "فیلم"
        return text
    }

    fun showSeasons() {
        _showSeasons.value = true
    }

    fun changeSeason(index: Int){
        _selectedSeason.value = index
    }

    fun play(): String? {
        if (!canSeeVideo()) return null

        try {
            watchData?.let {
                TempDB.setSelectedPost(_data.value.data)
                if (_data.value.data!!.isSeries){
                    val itemIndex = _data.value.data!!.seasons!![it.season].episodes[it.episode].items.getSimilarIndex(
                        listOf(it.type, it.quality, it.qualityCode, it.encoder)
                    )
                    return "${Routes.PLAYER.name}/$itemIndex/${it.season}/${it.episode}"
                } else {
                    val itemIndex = _data.value.data!!.movieDownloadBox!!.getSimilarIndex(
                        listOf(it.type, it.quality, it.qualityCode, it.encoder)
                    )
                    return "${Routes.PLAYER.name}/$itemIndex/-1/-1"
                }
            }
        }catch (_: Exception){}

        if (_data.value.data!!.isSeries){
            TempDB.setSelectedPost(_data.value.data)
            val sIndex = _data.value.data!!.seasons!!.lastIndex
            val itemIndex = _data.value.data!!.seasons!![sIndex].episodes[0].items.getDefaultItemIndex()
            isWatched = true
            watchedEpisodes.add(WatchedEpisode(0, _data.value.data!!.id, sIndex, 0))
            return "${Routes.PLAYER.name}/$itemIndex/$sIndex/0"
        } else {
            showMovieBottomSheet()
        }

        return null
    }

    fun playItem(itemIndex: Int): String? {
        if (!canSeeVideo()) return null

        _bottomSheetItems.clear()
        TempDB.setSelectedPost(_data.value.data)
        isWatched = true
        if (_data.value.data!!.isSeries){
            watchedEpisodes.add(WatchedEpisode(0, _data.value.data!!.id, _selectedSeason.value, selectedEpisode))
        }
        return if (_data.value.data!!.isSeries) "${Routes.PLAYER.name}/$itemIndex/${_selectedSeason.value}/$selectedEpisode" else "${Routes.PLAYER.name}/$itemIndex/-1/-1"
    }

    fun showEpisodeBottomSheet(episodeIndex: Int) {
        selectedEpisode = episodeIndex
        val season = _data.value.data!!.seasons!![selectedSeason.value]
        val episode = season.episodes[episodeIndex]
        bottomSheetTitle = "فصل ${season.name} قسمت ${episode.name}"

        if (_bottomSheetItems.isNotEmpty()) _bottomSheetItems.clear()
        _bottomSheetItems.addAll(episode.items.getAllItemsName())
    }

    fun hideBottomSheet() = _bottomSheetItems.clear()

    fun hideDialogs() {
        _errorMessage.value = ""
        _showBuyDialog.value = false
        _showLoginDialog.value = false
    }

    fun shouldShowReplayButton() = !_data.value.data!!.isSeries && watchData != null

    fun updateWatchlist() = viewModelScope.launch {
        val isInWatchlist = _data.value.data!!.isInWatchlist
        _data.value = DataResult.DataSuccess(_data.value.data!!.copy(isInWatchlist = !isInWatchlist))
        repository.updateWatchlist(
            _data.value.data!!.id,
            if (isInWatchlist) "remove" else "add"
        )
    }

    fun like() = viewModelScope.launch {
        _likeStatus.value = 1
        val likeInfo = repository.like(_data.value.data!!.id, "like")
        if (likeInfo is DataResult.DataError) return@launch

        _data.value = DataResult.DataSuccess(_data.value.data!!.copy(likeInfo = likeInfo.data!!))
    }

    fun dislike() = viewModelScope.launch {
        _likeStatus.value = 2
        val likeInfo = repository.like(_data.value.data!!.id, "dislike")
        if (likeInfo is DataResult.DataError) return@launch

        _data.value = DataResult.DataSuccess(_data.value.data!!.copy(likeInfo = likeInfo.data!!))
    }

    fun showMovieBottomSheet() {
        bottomSheetTitle = _data.value.data!!.title
        if (_bottomSheetItems.isNotEmpty()) _bottomSheetItems.clear()
        _bottomSheetItems.addAll(_data.value.data!!.movieDownloadBox!!.getAllItemsName())
    }

    private fun canSeeVideo(): Boolean{
        if (_data.value.data!!.message != null) {
            _errorMessage.value = _data.value.data!!.message ?: ""
            return false
        }

        if (_data.value.data!!.isFree) return true

        if (!TempDB.isLogin.value || TempDB.vipInfo == null) {
            _showLoginDialog.value = true
            return false
        }

        if (!TempDB.vipInfo!!.isVip) {
            _showBuyDialog.value = true
            return false
        }

        return true
    }
}