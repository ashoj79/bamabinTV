package com.bamabin.tv_app.data.remote.model.videos

import com.google.gson.annotations.SerializedName

data class Post(
    val id: Int,
    val title: String,
    val thumbnail: String,
    @SerializedName("imdb_rate_movie")
    val imdbRate: String,
    val hasAudio: Boolean,
    val genres: List<Genre>
) {
    fun getClearTitle() = title.replace("دانلود فیلم", "")
        .replace("دانلود سریال", "").replace("دانلود سریال", "")
        .replace("دانلود انیمیشن سریالی", "").replace("دانلود انیمیشن", "")
        .replace("دانلود انیمه", "").replace("دانلود مستند", "")
}