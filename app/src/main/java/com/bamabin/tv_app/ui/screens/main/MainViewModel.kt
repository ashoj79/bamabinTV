package com.bamabin.tv_app.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bamabin.tv_app.data.remote.model.videos.HomeSection
import com.bamabin.tv_app.repo.VideosRepository
import com.bamabin.tv_app.utils.DataResult
import com.bamabin.tv_app.utils.Routes
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

    fun getMoreRoute(index: Int): String {
        val title = _homeSections.value.data!![index].name
        var taxonomy = _homeSections.value.data!![index].taxonomy
        val taxonomyId = _homeSections.value.data!![index].taxonomyId
        val order = _homeSections.value.data!![index].orderBy

        if (taxonomy != null && taxonomyId != null){
            taxonomy = if (taxonomy == "country") "countries" else taxonomy
            return "${Routes.TAXONOMY_POSTS.name}?taxonomy=$taxonomy&id=$taxonomyId&title=$title&order=$order"
        }

        val types = _homeSections.value.data!![index].postTypes.joinToString(",")
        val broadcastStatuses = _homeSections.value.data!![index].broadcastStatuses.joinToString(",")
        val miniSerial = if (_homeSections.value.data!![index].miniSerial) "yes" else ""
        val dlboxType = _homeSections.value.data!![index].dlboxType
        val free = if (_homeSections.value.data!![index].isFree) "yes" else ""

        return "${Routes.POST_TYPE.name}?type=$types&broadcast_status=$broadcastStatuses&mini_serial=$miniSerial&order_by=$order&dlbox_type=$dlboxType&title=$title&free=$free"
    }
}