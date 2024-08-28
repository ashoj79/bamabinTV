package com.bamabin.tv_app.data.remote.model.user

import android.icu.text.DecimalFormat
import androidx.compose.ui.graphics.Color
import com.bamabin.tv_app.ui.theme.Failed
import com.bamabin.tv_app.ui.theme.Success
import com.google.gson.annotations.SerializedName
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat

data class Transaction(
    val id: Int,
    val price: String,
    val status: String,
    val days: String,
    @SerializedName("created_at")
    val date: String,
    val authority: String
) {
    fun getStatusLabel() = when(status){
        "success" -> "موفق"
        "failed" -> "ناموفق"
        else -> status
    }

    fun getStatusColor() = when(status){
        "success" -> Success
        "failed" -> Failed
        else -> Color.White
    }

    fun getMonth() = "${days.toInt() / 30} ماه"

    fun getPriceText() = DecimalFormat("#,###").format(price.toInt())

    fun getPersianDate(): String {
        val persianDate = PersianDate()
        val dateParts = date.split(" ")[0].split("-")
        val timeParts = date.split(" ")[1].split(":")

        persianDate.grgYear = dateParts[0].toInt()
        persianDate.grgMonth = dateParts[1].toInt()
        persianDate.grgDay = dateParts[2].toInt()
        persianDate.hour = timeParts[0].toInt()
        persianDate.minute = timeParts[1].toInt()
        persianDate.second = timeParts[2].toInt()

        val pFormater = PersianDateFormat("Y - m - d H:i:s")
        return pFormater.format(persianDate)
    }
}
