package com.bamabin.tv_app.data.remote

import com.bamabin.tv_app.utils.AESHelper
import javax.inject.Inject

class UrlHelper @Inject constructor() {
    companion object {
        var url: String? = null
            private set
        var cert: String? = null
            private set
        var allowVPN: Boolean = true
            private set
    }

    @Inject
    lateinit var aesHelper: AESHelper

    fun setData(data: String) {
        val parts = data.split(";")
        var baseUrl = parts[0]
        baseUrl = aesHelper.decrypt(baseUrl)
        val baseUrlParts = baseUrl.split(";")

        url = parts[0]
        allowVPN = baseUrlParts[1] == "1"
        cert = parts[1]
    }

    fun getDecryptedUrl() = url?.let { aesHelper.decrypt(it).split(";").first() } ?: ""
    fun getDecryptedCert() = cert?.let { aesHelper.decrypt(it) } ?: ""
}