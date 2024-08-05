package com.bamabin.tv_app.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bamabin.tv_app.data.remote.model.videos.Post
import com.bamabin.tv_app.repo.VideosRepository
import com.bamabin.tv_app.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: VideosRepository
): ViewModel() {
    private var job: Job? = null

    private val _searchResult = MutableStateFlow<DataResult<List<Post>>?>(null)
    val searchResult: StateFlow<DataResult<List<Post>>?> = _searchResult

    fun search(s: String) {
        if (s.length < 3) return

        if (job != null){
            job?.cancel()
            job = null
        }

        _searchResult.value = DataResult.DataLoading()

        job = viewModelScope.launch {
            val result = repository.search(s.lowercase())
            if (result is DataResult.DataError){
                _searchResult.value = DataResult.DataError(result.message)
                return@launch
            }
            val posts = mutableListOf<Post>()
            posts.addAll(result.data!!.series)
            posts.addAll(result.data.movies)
            posts.addAll(result.data.animations)
            posts.addAll(result.data.anime)
            _searchResult.value = DataResult.DataSuccess(posts)
        }
    }
}