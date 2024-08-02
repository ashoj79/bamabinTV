package com.bamabin.tv_app.data.local

import com.bamabin.tv_app.data.remote.model.app.AboutUs
import com.bamabin.tv_app.data.remote.model.user.VipInfo
import com.bamabin.tv_app.data.remote.model.videos.Genre

class TempDB {
    companion object {
        var genres: List<Genre> = emptyList()
            private set
        var aboutUs: AboutUs? = null
            private set
        var vipInfo: VipInfo? = null
            private set

        fun saveGenres(data:List<Genre>) {
            genres = data
        }

        fun saveVipInfo(data:VipInfo) {
            vipInfo = data
        }

        fun saveAboutUs(data:AboutUs) {
            aboutUs = data
        }
    }
}