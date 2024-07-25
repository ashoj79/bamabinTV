package com.bamabin.tv_app.repo

import com.bamabin.tv_app.data.remote.api_service.VideosApiService
import com.bamabin.tv_app.data.remote.model.videos.HomeSection
import com.bamabin.tv_app.utils.ConnectionChecker
import com.bamabin.tv_app.utils.DataResult
import javax.inject.Inject

class VideosRepository @Inject constructor(
    private val connectionChecker: ConnectionChecker,
    private val videosApiService: VideosApiService
) {

    suspend fun getHomeSections(): DataResult<List<HomeSection>> {
        return try {
            val response = videosApiService.getHomeSections()
            val data=HomeSection.createFromJson(response.charStream().readText())
            DataResult.DataSuccess(data)
        } catch (e: Exception){
            DataResult.DataError(e.message ?: "")
        }
    }
}