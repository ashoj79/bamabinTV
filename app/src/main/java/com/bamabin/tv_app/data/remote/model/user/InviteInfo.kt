package com.bamabin.tv_app.data.remote.model.user

import com.google.gson.annotations.SerializedName

data class InviteInfo(
    @SerializedName("invites_count")
    val invitesCount:Int,
    @SerializedName("is_invited")
    val isInvited:Boolean,
    @SerializedName("invite_code")
    val inviteCode:String
)
