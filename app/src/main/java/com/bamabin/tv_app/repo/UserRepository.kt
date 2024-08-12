package com.bamabin.tv_app.repo

import com.bamabin.tv_app.data.local.TempDB
import com.bamabin.tv_app.data.local.datastore.AppDatastore
import com.bamabin.tv_app.data.remote.api_service.UserApiService
import com.bamabin.tv_app.utils.ConnectionChecker
import com.bamabin.tv_app.utils.DataResult
import org.json.JSONObject
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val connectionChecker: ConnectionChecker,
    private val userApiService: UserApiService,
    private val appDatastore: AppDatastore,
) {
    suspend fun loginWithUsername(username: String, password: String): DataResult<Any> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = userApiService.login(username, password).charStream().readText()
            val json = JSONObject(response)
            if (!json.getBoolean("status")){
                return DataResult.DataError(json.getString("message"))
            }

            TempDB.saveToken(json.getString("api_key"))
            appDatastore.setToken(json.getString("api_key"))

            if (!getVipInfo()){
                return DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
            }

            TempDB.isLogin.value = true

            DataResult.DataSuccess("")
        } catch (e: Exception){
            return DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }

    suspend fun loginWithApiKey(apiKey: String): DataResult<Any> {
        return try {
            TempDB.saveToken(apiKey)
            if (!getVipInfo())
                return DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")

            appDatastore.setToken(apiKey)
            TempDB.isLogin.value = true
            DataResult.DataSuccess("")
        } catch (_: Exception) {
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }

    suspend fun logout() {
        TempDB.saveVipInfo(null)
        appDatastore.setToken("")
        TempDB.isLogin.value = false
    }

    private suspend fun getVipInfo(): Boolean{
        return try {
            val vipInfo = userApiService.getVipInfo()
            TempDB.saveVipInfo(vipInfo)
            true
        }catch (_:Exception){ false }
    }
}