package com.bamabin.tv_app.data.remote.model.app

import com.google.gson.annotations.SerializedName

data class AboutUs(
    val about: String,
    @SerializedName("instagram_page")
    val instagram: String,
    @SerializedName("telegram_channel")
    val telegram: String
)
