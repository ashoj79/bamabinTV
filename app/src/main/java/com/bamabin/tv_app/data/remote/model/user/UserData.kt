package com.bamabin.tv_app.data.remote.model.user

import com.google.gson.annotations.SerializedName

data class UserData(
    val avatar: String,
    val username: String,
    val email: String,
    @SerializedName("vip_info")
    val vipInfo: VipInfo? = null
)
