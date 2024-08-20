package com.bamabin.tv_app.data.remote.model.videos

import com.bamabin.tv_app.data.local.TempDB
import com.google.gson.annotations.SerializedName

data class Post(
    val id: Int,
    val title: String,
    val thumbnail: String,
    @SerializedName("imdb_rate")
    val imdbRate: String,
    val hasAudio: Boolean,
    @SerializedName("genres_id")
    val genresId: List<Int>,
    val years: List<Int>
){
    val genres:List<Genre>
        get() {
            val g = mutableListOf<Genre>()
            TempDB.genres.forEach {
                if (genresId.contains(it.id))
                    g.add(it)
            }
            return g
        }

    val releaseYear: String
        get() = (if(years.isNotEmpty()) years[0] else 0).toString()
}