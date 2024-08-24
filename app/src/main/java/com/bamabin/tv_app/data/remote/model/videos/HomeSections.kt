package com.bamabin.tv_app.data.remote.model.videos

import com.google.gson.Gson
import org.json.JSONObject

data class HomeSection(
    val name: String,
    val taxonomy: String?,
    val taxonomyId: String?,
    val miniSerial: Boolean,
    val broadcastStatuses: List<String>,
    val dlboxType: String,
    val postTypes: List<String>,
    val orderBy: String,
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

            val broadcastStatuses = mutableListOf<String>()
            val postTypes = mutableListOf<String>()

            if (data.has("broadcast_status")) {
                for (i in 0 until data.getJSONArray("broadcast_status").length()){
                    broadcastStatuses.add(data.getJSONArray("broadcast_status").getString(i))
                }
            }

            if (data.has("post_type")) {
                for (i in 0 until data.getJSONArray("post_type").length()){
                    postTypes.add(data.getJSONArray("post_type").getString(i))
                }
            }

            val taxonomy = if (data.has("taxonomy")) data.getString("taxonomy") else null
            val taxonomyId = if (data.has("taxonomy_id")) data.getString("taxonomy_id") else null
            val miniSerial = if (data.has("mini_serial") && taxonomy == null) data.getBoolean("mini_serial") else false
            val dlboxType = if (data.has("dlbox_type")) data.getString("dlbox_type") else ""
            val orderBy = data.getString("order_by")
            return HomeSection(
                data.getString("name"),
                taxonomy,
                taxonomyId,
                miniSerial,
                broadcastStatuses,
                dlboxType,
                postTypes,
                orderBy,
                posts
            )
        }
    }
}