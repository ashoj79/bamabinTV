package com.bamabin.tv_app.ui.screens.archive

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
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
    private val repository: VideosRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel(){

    private val types = savedStateHandle["type"] ?: ""
    private val broadcastStatuses = savedStateHandle["broadcast_status"] ?: ""
    private val miniSerial = savedStateHandle["mini_serial"] ?: ""
    private val orderBy = savedStateHandle["order_by"] ?: ""
    private val dlboxType = savedStateHandle["dlbox_type"] ?: ""
    val title = savedStateHandle["title"] ?: ""

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _order = MutableStateFlow(getOrderById(orderBy))
    val order: StateFlow<Int> = _order

    val posts = mutableStateListOf<Post>()

    private var type: PostType? = null
    private var page = 1
    private var isEnd = false

    init {
        if (types.isNotEmpty()) fetchData()
    }

    fun setPostType(postType: PostType) {
        resetAll()
        type = postType
        fetchData()
    }

    fun setOrder(value: Int) {
        resetAll()
        _order.value = value
        _isLoading.value = true
        fetchData()
    }

    fun fetchData() = viewModelScope.launch {
        if (isEnd)return@launch

        _isLoading.value = true
        val result = repository.getPosts(getType(), getOrderBy(), broadcastStatuses, dlboxType, miniSerial, page)
        _isLoading.value = false
        if (result is DataResult.DataError) {
            return@launch
        }

        page++
        isEnd = result.data!!.size < 10
        posts.addAll(result.data)
    }

    fun showBack() = types.isNotEmpty()

    private fun resetAll() {
        posts.clear()
        _isLoading.value = true
        _order.value = 0
        page = 1
        isEnd = false
    }

    private fun getOrderBy(): String {
        return when(_order.value) {
            1 -> "release_year"
            3 -> "imdb"
            else -> "modified"
        }
    }

    private fun getOrderById(orderBy: String) = when(orderBy){
        "release_year" -> 1
        "modified" -> 2
        "imdb" -> 3
        else -> 0
    }

    private fun getType() = type?.typeName ?: types
}