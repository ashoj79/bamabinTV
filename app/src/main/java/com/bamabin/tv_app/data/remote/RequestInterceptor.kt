package com.bamabin.tv_app.data.remote

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.ByteArrayInputStream
import java.io.IOException
import java.net.URL
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

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