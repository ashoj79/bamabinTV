package com.bamabin.tv_app.data.remote.api_service

import com.bamabin.tv_app.data.remote.model.ApiResponse
import com.bamabin.tv_app.data.remote.model.videos.Post
import com.bamabin.tv_app.data.remote.model.videos.SearchResult
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("api/search")
    suspend fun search(
        @Query("s") s: String
    ): ApiResponse<List<Post>>

    @GET("api/archive/taxonomy/{taxonomy}/{taxonomy_id}")
    suspend fun getPostWithTaxonomy(
        @Path("taxonomy") taxonomy: String,
        @Path("taxonomy_id") id: Int,
        @Query("order_by") orderBy: String,
        @Query("type") type: String,
        @Query("page") page: Int
    ): ApiResponse<List<Post>>
}