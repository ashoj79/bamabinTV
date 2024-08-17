package com.bamabin.tv_app.data.local

import com.bamabin.tv_app.data.remote.model.app.AboutUs
import com.bamabin.tv_app.data.remote.model.user.VipInfo
import com.bamabin.tv_app.data.remote.model.videos.Genre
import com.bamabin.tv_app.data.remote.model.videos.Post
import kotlinx.coroutines.flow.MutableStateFlow

class TempDB {
    companion object {
        val isLogin = MutableStateFlow(false)
        val vipDays = MutableStateFlow(0)

        var genres: List<Genre> = emptyList()
            private set
        var aboutUs: AboutUs? = null
            private set
        var vipInfo: VipInfo? = null
            private set
        var promotions: List<Post> = emptyList()
            private set
        var token: String = ""
            private set
        var supportLink: String = ""
            private set

        fun saveGenres(data:List<Genre>) {
            genres = data
        }

        fun saveVipInfo(data:VipInfo?) {
            vipInfo = data
            vipDays.value = data?.days ?: 0
        }

        fun saveAboutUs(data:AboutUs) {
            aboutUs = data
        }

        fun savePromotions(data:List<Post>) {
            promotions = data
        }

        fun saveToken(data: String) {
            token = data
        }

        fun saveSupportLink(data: String) {
            supportLink = data
        }
    }
}