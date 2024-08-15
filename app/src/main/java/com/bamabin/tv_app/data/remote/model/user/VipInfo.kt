package com.bamabin.tv_app.data.remote.model.user

import com.google.gson.annotations.SerializedName

data class VipInfo(
    @SerializedName("have_vip")
    val isVip: Boolean,
    val days: Int = 0
)