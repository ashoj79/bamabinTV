package com.bamabin.tv_app.data.remote.api_service

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

    @GET("api/vip_info")
    suspend fun getVipInfo(): VipInfo
}