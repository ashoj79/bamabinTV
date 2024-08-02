package com.bamabin.tv_app.repo

import android.content.Context
import android.util.Log
import com.bamabin.tv_app.BuildConfig
import com.bamabin.tv_app.data.local.TempDB
import com.bamabin.tv_app.data.remote.UrlHelper
import com.bamabin.tv_app.data.remote.api_service.AppApiService
import com.bamabin.tv_app.data.remote.model.app.AppVersion
import com.bamabin.tv_app.utils.ConnectionChecker
import com.bamabin.tv_app.utils.DataResult
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val connectionChecker: ConnectionChecker,
    private val appApiService: AppApiService,
    private val urlHelper: UrlHelper,
    @ApplicationContext private val context: Context,
) {

    external fun getId():String
    fun getPackageManager()=context.packageManager
    fun getPackageName()=context.packageName

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    suspend fun getBaseUrl(retryCount: Int = 1): DataResult<Any> {
        return try {
            if (retryCount == 6)
                return DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")

            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = appApiService.getBaseUrl("download", getId())
            urlHelper.setData(response.charStream().readText())

            DataResult.DataSuccess("")
        } catch (e: Exception){
            return getBaseUrl(retryCount + 1)
        }
    }

    suspend fun getStartupData(): DataResult<AppVersion> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = appApiService.getStartupData()
            if (!response.status)
                return DataResult.DataError(response.message ?: "")

            TempDB.saveGenres(response.results!!.genres)
            TempDB.saveVipInfo(response.results.vipInfo)
            TempDB.saveAboutUs(response.results.aboutUs)

            DataResult.DataSuccess(response.results.version.copy(
                needUpdate = response.results.version.version > BuildConfig.VERSION_CODE
            ))
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (e: Exception){
             DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }
}