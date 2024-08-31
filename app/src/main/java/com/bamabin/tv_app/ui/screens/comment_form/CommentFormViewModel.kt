package com.bamabin.tv_app.ui.screens.comment_form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bamabin.tv_app.repo.CommentsRepository
import com.bamabin.tv_app.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentFormViewModel @Inject constructor(
    private val repository: CommentsRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val id: Int = savedStateHandle["id"]!!
    private val parentId: Int = savedStateHandle["parent_id"]!!

    private val _result = MutableStateFlow<DataResult<Any>?>(null)
    val result: StateFlow<DataResult<Any>?> = _result

    fun saveComment(content: String, hasSpoil: Boolean) = viewModelScope.launch {
        _result.value = DataResult.DataLoading()
        _result.value = repository.addComment(id, content, hasSpoil, parentId)
    }

    fun retry() {
        _result.value = null
    }
}