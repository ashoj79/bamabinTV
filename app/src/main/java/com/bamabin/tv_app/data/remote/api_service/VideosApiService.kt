package com.bamabin.tv_app.data.remote.api_service

import com.bamabin.tv_app.data.remote.model.ApiResponse
import com.bamabin.tv_app.data.remote.model.videos.Post
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface VideosApiService {
    @GET("api/home/sections")
    suspend fun getHomeSections(): ResponseBody

    @GET("api/archive/post_type")
    suspend fun getPosts(
        @Query("type") type: String,
        @Query("order_by") orderBy: String,
        @Query("genre_id") genreId: Int,
        @Query("page") page: Int
    ): ApiResponse<List<Post>>
}