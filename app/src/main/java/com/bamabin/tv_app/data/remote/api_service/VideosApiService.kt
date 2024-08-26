package com.bamabin.tv_app.data.remote.api_service

import com.bamabin.tv_app.data.remote.model.ApiResponse
import com.bamabin.tv_app.data.remote.model.videos.LikeInfo
import com.bamabin.tv_app.data.remote.model.videos.Post
import com.bamabin.tv_app.data.remote.model.videos.PostDetails
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VideosApiService {
    @GET("api/home/sections")
    suspend fun getHomeSections(): ResponseBody

    @GET("api/archive/post_type")
    suspend fun getPosts(
        @Query("type") type: String,
        @Query("order_by") orderBy: String,
        @Query("broadcast_status") broadcastStatus: String,
        @Query("dlbox_type") dlboxType: String,
        @Query("mini_serial") miniSerial: String,
        @Query("free") free: String,
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

    @GET("api/post/show/v2/{id}")
    suspend fun getPostDetails(
        @Path("id") id: Int
    ): ApiResponse<PostDetails>

    @POST("api/watchlist/set")
    @FormUrlEncoded
    suspend fun updateWatchList(
        @Field("post_id") postId: Int,
        @Field("action") action: String
    )

    @GET("api/like/post/{post_id}/{type}")
    suspend fun like(
        @Path("post_id") postId: Int,
        @Path("type") type: String
    ): ApiResponse<LikeInfo>
}