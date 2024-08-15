package com.bamabin.tv_app.data.remote.api_service

import com.bamabin.tv_app.data.remote.model.ApiResponse
import com.bamabin.tv_app.data.remote.model.user.Request
import com.bamabin.tv_app.data.remote.model.user.Transaction
import com.bamabin.tv_app.data.remote.model.user.UserData
import com.bamabin.tv_app.data.remote.model.user.VipInfo
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApiService {
    @POST("api/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ResponseBody

    @GET("api/panel/profile")
    suspend fun getUserData(): ApiResponse<UserData>

    @GET("api/panel/payments")
    suspend fun getPayments(): ApiResponse<List<Transaction>>

    @GET("api/panel/request/index")
    suspend fun getRequests(): ApiResponse<List<Request>>

    @POST("api/panel/request/create")
    @FormUrlEncoded
    suspend fun sendRequest(
        @Field("title") title: String,
        @Field("release") release: String,
        @Field("type") type: String
    ): ApiResponse<Any>
}