package com.bamabin.tv_app.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bamabin.tv_app.data.remote.model.videos.HomeSection
import com.bamabin.tv_app.repo.VideosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: VideosRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _homeSections = mutableListOf<HomeSection>()
    val homeSections:List<HomeSection> = _homeSections

    init {
        getSection()
    }

    private fun getSection() = viewModelScope.launch {
        _homeSections.addAll(repository.getHomeSections().data ?: emptyList())
        _isLoading.value = false
    }
}