package com.bamabin.tv_app.data.remote.api_service

import com.bamabin.tv_app.data.remote.model.ApiResponse
import com.bamabin.tv_app.data.remote.model.app.StartupData
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface AppApiService {
    @GET("uc")
    suspend fun getBaseUrl(
        @Query("export") export: String,
        @Query("id") id: String
    ): ResponseBody

    @GET("api/startup")
    suspend fun getStartupData(): ApiResponse<StartupData>
}