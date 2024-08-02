package com.bamabin.tv_app.data.remote.model.app

import com.google.gson.annotations.SerializedName

data class AppVersion(
    val version: Int,
    @SerializedName("direct_download_link")
    val directLink: String,
    @SerializedName("playstore_download_link")
    val playStoreLink: String,
    var needUpdate: Boolean = false
)
