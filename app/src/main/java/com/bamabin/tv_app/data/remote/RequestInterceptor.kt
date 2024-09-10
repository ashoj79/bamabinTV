package com.bamabin.tv_app.data.remote

import android.content.Context
import com.bamabin.tv_app.data.local.TempDB
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RequestInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val urlHelper: UrlHelper
):Interceptor {

    external fun getURL():String

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    fun getPackageManager()=context.packageManager
    fun getPackageName()=context.packageName

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val url = (UrlHelper.url?.let { urlHelper.getDecryptedUrl() } ?: getURL()).toHttpUrl()
        val urlRequestBuilder = request.url.newBuilder()
            .scheme(url.scheme)
            .host(url.host)

        val urlRequest = urlRequestBuilder.build()

        request = request.newBuilder()
            .url(urlRequest)
            .addHeader("BAMABIN_TV", "1")
            .addHeader("BAMABIN_PLAY_STORE", "1")
            .addHeader("BAMABIN_API_KEY", TempDB.token)
            .build()

        if (UrlHelper.url == null) {
            return chain
                .withReadTimeout(10, TimeUnit.SECONDS)
                .withWriteTimeout(10, TimeUnit.SECONDS)
                .withConnectTimeout(10, TimeUnit.SECONDS)
                .proceed(request)
        }

        return chain.proceed(request)
    }
}