package com.bamabin.tv_app.data.remote.model.videos

import com.google.gson.Gson
import org.json.JSONObject

data class HomeSection(
    val name: String,
    val slug: String,
    val posts: List<Post>
) {
    companion object {
        fun createFromJson(jsonString: String): List<HomeSection> {
            val jsonObject = JSONObject(jsonString)
            val resultList = jsonObject.getJSONArray("result")
            val homeSections = mutableListOf<HomeSection>()

            for (i in 0 until resultList.length()) {
                val type: String = resultList.getJSONObject(i).getString("type")
                when (type) {
                    "last_data" -> homeSections.add(decodeSection(resultList.getJSONObject(i)))
                }
            }

            return homeSections
        }

        private fun decodeSection(data: JSONObject): HomeSection {
            val posts = mutableListOf<Post>()
            for (i in 0 until data.getJSONArray("posts").length()) {
                val postData = data.getJSONArray("posts").getJSONObject(i)
                val hasAudio = postData.getString("dlbox_audio").replace("\"", "").isNotEmpty()
                val genres = mutableListOf<Genre>()
                for (j in 0 until postData.getJSONArray("genres").length()) {
                    genres.add(
                        Gson().fromJson(
                            postData.getJSONArray("genres").getJSONObject(j).toString(),
                            Genre::class.java
                        )
                    )
                }
                val title = postData.getString("title").replace("دانلود فیلم", "")
                    .replace("دانلود سریال", "").replace("دانلود سریال", "")
                    .replace("دانلود انیمیشن سریالی", "").replace("دانلود انیمیشن", "")
                    .replace("دانلود انیمه", "").replace("دانلود مستند", "")
                var imdbRate = postData.getString("imdb_rate_movie")
                if (imdbRate.isEmpty()) imdbRate = " - "

                posts.add(
                    Post(
                        postData.getInt("id"),
                        title,
                        postData.getString("thumbnail"),
                        imdbRate,
                        hasAudio,
                        genres
                    )
                )
            }

            return HomeSection(data.getString("name"), data.getString("see_more_link"), posts)
        }
    }
}

data class Post(
    val id: Int,
    val title: String,
    val thumbnail: String,
    val imdbRate: String,
    val hasAudio: Boolean,
    val genres: List<Genre>
)