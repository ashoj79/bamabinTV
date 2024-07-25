package com.bamabin.tv_app.data.remote

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RequestInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
):Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request=chain
            .request()
            .newBuilder()

        return chain.proceed(request.build())
    }
}