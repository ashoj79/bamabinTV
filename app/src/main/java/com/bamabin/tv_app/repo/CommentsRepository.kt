package com.bamabin.tv_app.repo

import com.bamabin.tv_app.data.remote.api_service.CommentApiService
import com.bamabin.tv_app.data.remote.model.comment.Comment
import com.bamabin.tv_app.data.remote.model.videos.LikeInfo
import com.bamabin.tv_app.utils.ConnectionChecker
import com.bamabin.tv_app.utils.DataResult
import retrofit2.HttpException
import javax.inject.Inject

class CommentsRepository @Inject constructor(
    private val apiService: CommentApiService,
    private val connectionChecker: ConnectionChecker
) {
    suspend fun getComments(id: Int): DataResult<List<Comment>> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = apiService.getComments(id)
            if (!response.status)
                return DataResult.DataError(response.message ?: "")

            DataResult.DataSuccess(response.getMainResult()!!.reversed())
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (e: Exception){
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }

    suspend fun likeComment(id: Int, type: String): DataResult<LikeInfo> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = apiService.like(id, type)
            if (!response.status)
                return DataResult.DataError(response.message ?: "")

            DataResult.DataSuccess(response.getMainResult()!!)
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (e: Exception){
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }

    suspend fun addComment(postId: Int, content: String, hasSpoil: Boolean, parentId: Int): DataResult<Any> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            apiService.addComment(postId, content, if (hasSpoil) 1 else 0, parentId)

            DataResult.DataSuccess("")
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (e: Exception){
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }
}