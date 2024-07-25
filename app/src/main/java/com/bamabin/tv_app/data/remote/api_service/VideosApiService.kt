package com.bamabin.tv_app.data.remote.api_service

import okhttp3.ResponseBody
import retrofit2.http.GET

interface VideosApiService {
    @GET("home/sections")
    suspend fun getHomeSections(): ResponseBody
}