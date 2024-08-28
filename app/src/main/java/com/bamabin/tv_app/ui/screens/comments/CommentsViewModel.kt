package com.bamabin.tv_app.ui.screens.comments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bamabin.tv_app.data.remote.model.comment.Comment
import com.bamabin.tv_app.repo.CommentsRepository
import com.bamabin.tv_app.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val repository: CommentsRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel(){
    val id: Int = savedStateHandle["id"]!!
    val title = "${savedStateHandle.get<String>("title")!!} - نظرات کاربران"

    private val _result = MutableStateFlow<DataResult<List<Comment>>>(DataResult.DataLoading())
    val result: StateFlow<DataResult<List<Comment>>> = _result

    init {
        getComments()
    }

    fun getComments() = viewModelScope.launch {
        _result.value = DataResult.DataLoading()
        _result.value = repository.getComments(id)
    }

    fun closeError() {
        _result.value = DataResult.DataLoading()
    }

    fun like(id: Int) = viewModelScope.launch {
        repository.likeComment(id, "like")
    }

    fun dislike(id: Int) = viewModelScope.launch {
        repository.likeComment(id, "dislike")
    }
}