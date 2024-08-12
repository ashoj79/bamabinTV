package com.bamabin.tv_app.data.remote.model.videos

import com.google.gson.annotations.SerializedName

data class Post(
    val id: Int,
    @SerializedName("en_title")
    val title: String,
    val thumbnail: String,
    @SerializedName("imdb_rate_movie")
    val imdbRate: String,
    val hasAudio: Boolean,
    val genres: List<Genre>,
    @SerializedName("release_movie")
    val releaseYear: String = ""
)