package com.bamabin.tv_app.data.remote.model.videos

import org.json.JSONObject

data class HomeSection(
    val name: String,
    val slug: String,
    val posts: List<Post>
) {
    companion object{
        fun createFromJson(jsonString: String): List<HomeSection>{
            val jsonObject = JSONObject(jsonString)
            val resultList = jsonObject.getJSONArray("result")
            val homeSections = mutableListOf<HomeSection>()

            for (i in 0 until resultList.length()){
                val type: String = resultList.getJSONObject(i).getString("type")
                when(type){
                    "last_data"-> homeSections.add(decodeSection(resultList.getJSONObject(i)))
                }
            }

            return homeSections
        }

        private fun decodeSection(data: JSONObject): HomeSection {
            val posts = mutableListOf<Post>()
            for (i in 0 until data.getJSONArray("posts").length()){
                val postData = data.getJSONArray("posts").getJSONObject(i)
                posts.add(Post(postData.getInt("id"), postData.getString("title"), postData.getString("thumbnail")))
            }

            return HomeSection(data.getString("name"), data.getString("see_more_link"), posts)
        }
    }
}

data class Post(
    val id: Int,
    val title: String,
    val thumbnail: String
)