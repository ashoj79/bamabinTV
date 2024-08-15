package com.bamabin.tv_app.ui.screens.request_form

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bamabin.tv_app.repo.UserRepository
import com.bamabin.tv_app.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestFormViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {
    private val _result = MutableStateFlow<DataResult<Any>?>(null)
    val result: StateFlow<DataResult<Any>?> = _result

    fun sendRequest(title: String, release: String, type: Int) = viewModelScope.launch {
        if (title.trim().length < 3){
            _result.value = DataResult.DataError("عنوان را وارد کنید")
            return@launch
        }

        if (release.trim().length!= 4 || !release.trim().isDigitsOnly()){
            _result.value = DataResult.DataError("سال انتشار را وارد کنید")
            return@launch
        }

        _result.value = DataResult.DataLoading()
        _result.value = repository.sendRequests(title.trim(), release.trim(), getType(type))
    }

    fun retry() {
        _result.value = null
    }

    private fun getType(index: Int) = when(index){
        3 -> "anime"
        2 -> "animations"
        1 -> "series"
        else -> "movies"
    }
}