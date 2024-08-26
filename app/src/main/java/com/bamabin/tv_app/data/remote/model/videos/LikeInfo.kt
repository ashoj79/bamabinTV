package com.bamabin.tv_app.data.remote.model.videos

import com.google.gson.annotations.SerializedName

data class LikeInfo(
    val likes: Int,
    val dislikes: Int,
    @SerializedName("like_percent")
    val percent: Float
)