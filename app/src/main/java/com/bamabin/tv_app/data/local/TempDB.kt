package com.bamabin.tv_app.data.local

import com.bamabin.tv_app.data.remote.model.videos.Genre

class TempDB {
    companion object {
        var genres: List<Genre> = emptyList()
            private set

        fun saveGenres(data:List<Genre>) {
            genres = data
        }
    }
}