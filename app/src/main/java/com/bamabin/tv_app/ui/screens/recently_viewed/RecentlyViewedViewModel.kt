package com.bamabin.tv_app.ui.screens.recently_viewed

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
class RecentlyViewedViewModel @Inject constructor(
    private val repository: VideosRepository
): ViewModel(){

    private val _postsResult = MutableStateFlow<DataResult<List<Post>>>(DataResult.DataLoading())
    val postsResult: StateFlow<DataResult<List<Post>>> = _postsResult

    init {
        getPosts()
    }

    fun getPosts() = viewModelScope.launch {
        _postsResult.value = DataResult.DataLoading()
        _postsResult.value = repository.getRecentlyViewed()
    }

    fun delete(index: Int) = viewModelScope.launch {
        val posts = mutableListOf<Post>()
        posts.addAll(_postsResult.value.data ?: emptyList())
        val id = posts[index].id
        posts.removeAt(index)
        repository.deleteWatchDataWithId(id)
        _postsResult.value = DataResult.DataSuccess(posts)
    }
}