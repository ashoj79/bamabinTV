package com.bamabin.tv_app.ui.screens.watch_list

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bamabin.tv_app.data.remote.model.videos.Post
import com.bamabin.tv_app.repo.VideosRepository
import com.bamabin.tv_app.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchListViewModel @Inject constructor(
    private val repository: VideosRepository
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val posts = mutableStateListOf<Post>()

    private var page = 1
    private var isEnd = false

    init {
        fetchData()
    }

    fun fetchData() = viewModelScope.launch {
        if (isEnd || _isLoading.value) return@launch

        _isLoading.value = true
        val result = repository.getWatchlist(page)
        _isLoading.value = false
        if (result is DataResult.DataError) return@launch

        page++
        isEnd = result.data!!.size < 10
        posts.addAll(result.data)
    }

    fun remove(index: Int) = viewModelScope.launch {
        val id = posts[index].id
        posts.removeAt(index)
        repository.updateWatchlist(id, "remove")
    }
}