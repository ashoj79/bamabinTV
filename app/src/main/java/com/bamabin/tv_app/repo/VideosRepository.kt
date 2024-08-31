package com.bamabin.tv_app.repo

import com.bamabin.tv_app.data.local.PostType
import com.bamabin.tv_app.data.local.database.WatchDao
import com.bamabin.tv_app.data.local.database.WatchedEpisodeDao
import com.bamabin.tv_app.data.local.database.model.WatchData
import com.bamabin.tv_app.data.local.database.model.WatchedEpisode
import com.bamabin.tv_app.data.remote.api_service.VideosApiService
import com.bamabin.tv_app.data.remote.model.videos.HomeSection
import com.bamabin.tv_app.data.remote.model.videos.LikeInfo
import com.bamabin.tv_app.data.remote.model.videos.Post
import com.bamabin.tv_app.data.remote.model.videos.PostDetails
import com.bamabin.tv_app.utils.ConnectionChecker
import com.bamabin.tv_app.utils.DataResult
import retrofit2.HttpException
import javax.inject.Inject

class VideosRepository @Inject constructor(
    private val connectionChecker: ConnectionChecker,
    private val videosApiService: VideosApiService,
    private val watchDao: WatchDao,
    private val watchedEpisodeDao: WatchedEpisodeDao
) {

    suspend fun getWatchData(id: Int)= watchDao.getData(id)
    suspend fun deleteWatchData(data: WatchData)= watchDao.delete(data)
    suspend fun deleteWatchDataWithId(id: Int)= watchDao.deleteWithId(id)
    suspend fun saveWatchData(data: WatchData) {
        if (watchDao.getOtherCount(data.id) >= 50){
            watchDao.deleteWithId(watchDao.getOldestId())
        }
        watchDao.saveOrUpdate(data.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun getWatchedEpisodes(id: Int)= watchedEpisodeDao.getWatchedEpisodes(id)
    suspend fun saveWatchedEpisode(watchedEpisode: WatchedEpisode) {
        if (watchedEpisodeDao.getOtherCount(watchedEpisode.id, watchedEpisode.season, watchedEpisode.episode) >= 100){
            val oldest = watchedEpisodeDao.getOldest()
            watchedEpisodeDao.delete(oldest.id, oldest.season, oldest.episode)
        }
        watchedEpisodeDao.insert(watchedEpisode.copy(time = System.currentTimeMillis()))
    }

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

    suspend fun getPosts(type: String, orderBy: String, broadcastStatus: String, dlboxType: String, miniSerial: String, free: String, page: Int): DataResult<List<Post>> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = videosApiService.getPosts(type, orderBy, broadcastStatus, dlboxType, miniSerial, free, page)
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

    suspend fun updateWatchlist(id: Int, action: String): DataResult<Any> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            videosApiService.updateWatchList(id, action)
            DataResult.DataSuccess("")
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (e: Exception){
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }

    suspend fun getWatchlist(page: Int): DataResult<List<Post>> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = videosApiService.getWatchList(page)
            if (!response.status)
                return DataResult.DataError(response.message ?: "")

            DataResult.DataSuccess(response.getMainResult()!!)
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (e: Exception){
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }

    suspend fun like(id: Int, type: String): DataResult<LikeInfo> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = videosApiService.like(id, type)
            if (!response.status)
                return DataResult.DataError(response.message ?: "")

            DataResult.DataSuccess(response.getMainResult()!!)
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (e: Exception){
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }

    suspend fun getRecentlyViewed(): DataResult<List<Post>> {
        return try {
            val ids = watchDao.getAllIds()
            if (ids.isEmpty())
                return DataResult.DataSuccess(emptyList())

            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = videosApiService.getSpecificPosts(ids.joinToString(","))
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