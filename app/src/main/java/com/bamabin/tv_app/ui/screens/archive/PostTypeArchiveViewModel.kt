package com.bamabin.tv_app.ui.screens.archive

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bamabin.tv_app.data.local.PostType
import com.bamabin.tv_app.data.remote.model.videos.Post
import com.bamabin.tv_app.repo.VideosRepository
import com.bamabin.tv_app.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostTypeArchiveViewModel @Inject constructor(
    private val repository: VideosRepository
): ViewModel(){

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _genreId = MutableStateFlow(0)
    val genreId: StateFlow<Int> = _genreId

    private val _order = MutableStateFlow(0)
    val order: StateFlow<Int> = _order

    val posts = mutableStateListOf<Post>()

    private lateinit var type: PostType
    private var page = 1
    private var isEnd = false

    fun setPostType(postType: PostType) {
        resetAll()
        type = postType
        fetchData()
    }

    fun setGenreId(id: Int) {
        val o = _order.value
        resetAll()
        _genreId.value = id
        _isLoading.value = true
        _order.value = o
        fetchData()
    }

    fun setOrder(value: Int) {
        val g = _genreId.value
        resetAll()
        _order.value = value
        _isLoading.value = true
        _genreId.value = g
        fetchData()
    }

    fun fetchData() = viewModelScope.launch {
        if (isEnd)return@launch

        _isLoading.value = true
        val result = repository.getPosts(type, genreId.value, getOrderBy(), page)
        _isLoading.value = false
        if (result is DataResult.DataError) {
            return@launch
        }

        page++
        isEnd = result.data!!.size < 10
        posts.addAll(result.data)
    }

    private fun resetAll() {
        posts.clear()
        _isLoading.value = true
        _genreId.value = 0
        _order.value = 0
        page = 1
        isEnd = false
    }

    private fun getOrderBy(): String {
        return "date"
    }
}