package com.bamabin.tv_app.data.remote.model.videos

import com.google.gson.annotations.SerializedName

data class Genre(
    val id: Int,
    val name: String,
    val link: String,
    val icon: String,
    @SerializedName("background_url")
    val backgroundUrl: String
)
