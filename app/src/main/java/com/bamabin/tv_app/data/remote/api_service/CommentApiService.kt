package com.bamabin.tv_app.data.remote.api_service

import com.bamabin.tv_app.data.remote.model.ApiResponse
import com.bamabin.tv_app.data.remote.model.comment.Comment
import com.bamabin.tv_app.data.remote.model.videos.LikeInfo
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentApiService {
    @GET("api/comments/{post_id}")
    suspend fun getComments(
        @Path("post_id") id: Int
    ): ApiResponse<List<Comment>>

    @GET("api/like/comment/{comment_id}/{type}")
    suspend fun like(
        @Path("comment_id") id: Int,
        @Path("type") type: String
    ): ApiResponse<LikeInfo>

    @POST("api/comment/add/{post_id}")
    @FormUrlEncoded
    suspend fun addComment(
        @Path("post_id") id: Int,
        @Field("content_comment") content: String,
        @Field("spoil_comment") hasSpoil: Int,
        @Field("parent_comment_id") parentId: Int
    )
}