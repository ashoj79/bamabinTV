package com.bamabin.tv_app.di

import com.bamabin.tv_app.data.remote.RequestInterceptor
import com.bamabin.tv_app.data.remote.api_service.AppApiService
import com.bamabin.tv_app.data.remote.api_service.CommentApiService
import com.bamabin.tv_app.data.remote.api_service.UserApiService
import com.bamabin.tv_app.data.remote.api_service.VideosApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun provideOkHttpClient(requestInterceptor: RequestInterceptor)=
        OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

    @Provides
    fun provideRetrofit(client: OkHttpClient)=
        Retrofit.Builder()
            .baseUrl("https://bamabin.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Provides
    fun provideVideosApiService(retrofit: Retrofit)=retrofit.create(VideosApiService::class.java)

    @Provides
    fun provideAppApiService(retrofit: Retrofit)=retrofit.create(AppApiService::class.java)

    @Provides
    fun provideAppUserService(retrofit: Retrofit)=retrofit.create(UserApiService::class.java)

    @Provides
    fun provideCommentsApiService(retrofit: Retrofit)=retrofit.create(CommentApiService::class.java)
}