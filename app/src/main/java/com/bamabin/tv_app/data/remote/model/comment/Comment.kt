package com.bamabin.tv_app.data.remote.model.comment

import com.bamabin.tv_app.data.remote.model.videos.LikeInfo
import com.google.gson.annotations.SerializedName

data class Comment(
    val id: Int,
    val content: String,
    val author: String,
    val avatar: String,
    @SerializedName("like_info")
    val likeInfo: LikeInfo,
    @SerializedName("hasSpoil")
    val hasSpoil: Boolean,
    @SerializedName("parent_id")
    val prentId: Int,
)
