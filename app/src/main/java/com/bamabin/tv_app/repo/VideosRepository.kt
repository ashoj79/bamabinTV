package com.bamabin.tv_app.repo

import com.bamabin.tv_app.data.local.PostType
import com.bamabin.tv_app.data.local.database.WatchDao
import com.bamabin.tv_app.data.local.database.model.WatchData
import com.bamabin.tv_app.data.remote.api_service.VideosApiService
import com.bamabin.tv_app.data.remote.model.videos.HomeSection
import com.bamabin.tv_app.data.remote.model.videos.Post
import com.bamabin.tv_app.data.remote.model.videos.PostDetails
import com.bamabin.tv_app.utils.ConnectionChecker
import com.bamabin.tv_app.utils.DataResult
import retrofit2.HttpException
import javax.inject.Inject

class VideosRepository @Inject constructor(
    private val connectionChecker: ConnectionChecker,
    private val videosApiService: VideosApiService,
    private val watchDao: WatchDao
) {

    suspend fun getWatchData(id: Int)= watchDao.getData(id)
    suspend fun saveWatchData(data: WatchData)= watchDao.saveOrUpdate(data)
    suspend fun deleteWatchData(data: WatchData)= watchDao.delete(data)

    suspend fun getHomeSections(): DataResult<List<HomeSection>> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = videosApiService.getHomeSections()
            val data=HomeSection.createFromJson(response.charStream().readText())
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
            if (!response.status)
                return DataResult.DataError(response.message ?: "")

            DataResult.DataSuccess(response.results ?: emptyList())
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (e: Exception){
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }

    suspend fun search(s: String): DataResult<List<Post>> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = videosApiService.search(s)
            if (!response.status)
                return DataResult.DataError(response.message ?: "")

            DataResult.DataSuccess(response.getMainResult()!!)
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (e: Exception){
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }

    suspend fun getPostsWithTaxonomy(taxonomy: String, id: Int, orderBy: String, type: PostType?, page: Int): DataResult<List<Post>> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = videosApiService.getPostWithTaxonomy(taxonomy, id, orderBy, type?.typeName ?: "", page)
            if (!response.status)
                return DataResult.DataError(response.message ?: "")

            DataResult.DataSuccess(response.getMainResult()!!)
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (e: Exception){
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }

    suspend fun getPostDetails(id: Int): DataResult<PostDetails> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = videosApiService.getPostDetails(id)
            if (!response.status)
                return DataResult.DataError(response.message ?: "")

            DataResult.DataSuccess(response.getMainResult()!!)
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (e: Exception){
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }
}