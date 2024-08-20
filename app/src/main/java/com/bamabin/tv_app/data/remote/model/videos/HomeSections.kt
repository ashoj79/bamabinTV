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
                val genresId = mutableListOf<Int>()
                val years = mutableListOf<Int>()
                for (j in 0 until postData.getJSONArray("genres_id").length())
                    genresId.add(postData.getJSONArray("genres_id").getInt(j))

                for (j in 0 until postData.getJSONArray("years").length())
                    years.add(postData.getJSONArray("years").getInt(j))

                posts.add(
                    Post(
                        postData.getInt("id"),
                        postData.getString("title"),
                        postData.getString("thumbnail"),
                        postData.getString("imdb_rate"),
                        postData.getBoolean("has_dubbed"),
                        genresId,
                        years
                    )
                )
            }

            return HomeSection(data.getString("name"), data.getString("see_more_link"), posts)
        }
    }
}