package com.bamabin.tv_app.data.remote.model.user

import androidx.compose.ui.graphics.Color
import com.bamabin.tv_app.ui.theme.Failed
import com.bamabin.tv_app.ui.theme.Success
import com.google.gson.annotations.SerializedName
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat

data class Request(
    val id: Int,
    val title: String,
    @SerializedName("referral_link")
    val postId: String,
    val status: String,
    val message: String,
    @SerializedName("created_at")
    val date: String
) {
    fun getPersianDate(): String{
        val persianDate = PersianDate()
        val dateParts = date.split(" ")[0].split("-")

        persianDate.grgYear = dateParts[0].toInt()
        persianDate.grgMonth = dateParts[1].toInt()
        persianDate.grgDay = dateParts[2].toInt()

        val pFormater = PersianDateFormat("d / m / Y")
        return pFormater.format(persianDate)
    }

    fun getStatusLabel() = when(status){
        "declined" -> "رد شده"
        "confirmed" -> "تائید شده"
        else -> "در حال بررسی"
    }

    fun getStatusColor() = when(status){
        "declined" -> Failed
        "confirmed" -> Success
        else -> Color.White
    }

    fun isSubmitted() = status == "confirmed"
}
