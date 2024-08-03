package com.bamabin.tv_app.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bamabin.tv_app.data.remote.model.videos.HomeSection
import com.bamabin.tv_app.repo.VideosRepository
import com.bamabin.tv_app.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: VideosRepository
): ViewModel() {
    private val _homeSections = MutableStateFlow<DataResult<List<HomeSection>>>(DataResult.DataLoading())
    val homeSections:StateFlow<DataResult<List<HomeSection>>> = _homeSections

    init {
        getSection()
    }

    fun getSection() = viewModelScope.launch {
        _homeSections.value = DataResult.DataLoading()
        _homeSections.value = repository.getHomeSections()
    }
}