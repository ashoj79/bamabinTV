package com.bamabin.tv_app.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bamabin.tv_app.data.remote.model.app.AppVersion
import com.bamabin.tv_app.repo.AppRepository
import com.bamabin.tv_app.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: AppRepository
): ViewModel() {

    private val _result = MutableStateFlow<DataResult<AppVersion>>(DataResult.DataLoading())
    val result: StateFlow<DataResult<AppVersion>> = _result

    init {
        fetchData()
    }

    fun fetchData() = viewModelScope.launch {
        _result.value = DataResult.DataLoading()
        _result.value = repository.getStartupData()
    }
}