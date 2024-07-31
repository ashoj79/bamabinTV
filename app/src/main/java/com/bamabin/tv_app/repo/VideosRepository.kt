package com.bamabin.tv_app.repo

import android.util.Log
import com.bamabin.tv_app.data.local.PostType
import com.bamabin.tv_app.data.local.TempDB
import com.bamabin.tv_app.data.remote.api_service.VideosApiService
import com.bamabin.tv_app.data.remote.model.videos.Genre
import com.bamabin.tv_app.data.remote.model.videos.HomeSection
import com.bamabin.tv_app.data.remote.model.videos.Post
import com.bamabin.tv_app.utils.ConnectionChecker
import com.bamabin.tv_app.utils.DataResult
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class VideosRepository @Inject constructor(
    private val connectionChecker: ConnectionChecker,
    private val videosApiService: VideosApiService
) {

    suspend fun getHomeSections(): DataResult<List<HomeSection>> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = videosApiService.getHomeSections()
            val responseText = response.charStream().readText()
            var genresData = JSONObject(responseText).getJSONArray("result")
            genresData = genresData.getJSONObject(genresData.length() - 1).getJSONArray("genres")
            val genres = mutableListOf<Genre>()
            for (i in 0 until genresData.length()) {
                genres.add(Gson().fromJson(genresData.getJSONObject(i).toString(), Genre::class.java))
            }
            TempDB.saveGenres(genres)

            val data=HomeSection.createFromJson(responseText)
            DataResult.DataSuccess(data)
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (e: Exception){
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }

    suspend fun getPosts(type: PostType, genre: Int, orderBy: String, page: Int): DataResult<List<Post>> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = videosApiService.getPosts(type.typeName, orderBy, genre, page)
            DataResult.DataSuccess(response.results ?: emptyList())
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (e: Exception){
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }
}