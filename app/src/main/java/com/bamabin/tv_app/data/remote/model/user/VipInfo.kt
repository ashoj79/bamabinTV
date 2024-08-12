package com.bamabin.tv_app.data.remote.model.user

import com.google.gson.annotations.SerializedName

data class VipInfo(
    @SerializedName("have_vip")
    val isVip: Boolean,
    @SerializedName("start_time")
    val startTime: Int,
    @SerializedName("expire_time")
    val endTime: Int
) {
    fun getVipDays(): Int {
        return if (!isVip || endTime <= startTime) 0
        else {
            (endTime - startTime) / 3600 / 24
        }
    }
}
