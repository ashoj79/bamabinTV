package com.bamabin.tv_app.data.remote.model.app

import com.bamabin.tv_app.data.remote.model.user.VipInfo
import com.bamabin.tv_app.data.remote.model.videos.Genre
import com.bamabin.tv_app.data.remote.model.videos.Post
import com.google.gson.annotations.SerializedName

data class StartupData(
    val genres: List<Genre>,
    val version: AppVersion,
    @SerializedName("about_us")
    val aboutUs: AboutUs,
    @SerializedName("vip_info")
    val vipInfo: VipInfo,
    val promotions: List<Post>,
    @SerializedName("support_link")
    val supportLink: String
)
