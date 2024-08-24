package com.bamabin.tv_app.ui.screens.taxonomy_posts

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bamabin.tv_app.data.local.PostType
import com.bamabin.tv_app.data.remote.model.videos.Post
import com.bamabin.tv_app.repo.VideosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaxonomyPostsViewModel @Inject constructor(
    private val repository: VideosRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private var page = 1
    private var isEnd = false
    private var taxonomy = savedStateHandle["taxonomy"] ?: ""
    private var id = savedStateHandle["id"] ?: 0

    val title = savedStateHandle["title"] ?: ""

    private val _orderBy = MutableStateFlow(getOrderById(savedStateHandle["order"] ?: ""))
    val orderBy: StateFlow<Int> = _orderBy

    private val _postType = MutableStateFlow(-1)
    val postType: StateFlow<Int> = _postType

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _posts = mutableStateListOf<Post>()
    val posts: SnapshotStateList<Post> = _posts

    init {
        getPosts()
    }

    fun setOrder(order: Int) {
        _orderBy.value = order
        isEnd = false
        _posts.clear()
        page = 1
        getPosts()
    }

    fun setType(type: Int) {
        _postType.value = type
        isEnd = false
        _posts.clear()
        page = 1
        getPosts()
    }

    fun getPosts() = viewModelScope.launch {
        if (isEnd || _isLoading.value) return@launch
        _isLoading.value = true
        val pList = repository.getPostsWithTaxonomy(taxonomy, id, getOrderBy(), getPostType(), page).data ?: emptyList()
        _isLoading.value = false
        _posts.addAll(pList)
        page++
        isEnd = pList.size < 10
    }

    private fun getOrderBy(): String {
        return when(_orderBy.value) {
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

    private fun getPostType(): PostType? {
        return when(_postType.value){
            1 -> PostType.MOVIE
            2 -> PostType.SERIES
            3 -> PostType.ANIMATION
            4 -> PostType.ANIME
            else -> null
        }
    }
}