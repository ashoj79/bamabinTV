package com.bamabin.tv_app.repo

import com.bamabin.tv_app.data.local.TempDB
import com.bamabin.tv_app.data.local.datastore.AppDatastore
import com.bamabin.tv_app.data.remote.api_service.UserApiService
import com.bamabin.tv_app.data.remote.model.user.InviteInfo
import com.bamabin.tv_app.data.remote.model.user.Request
import com.bamabin.tv_app.data.remote.model.user.Transaction
import com.bamabin.tv_app.data.remote.model.user.UserData
import com.bamabin.tv_app.utils.ConnectionChecker
import com.bamabin.tv_app.utils.DataResult
import org.json.JSONObject
import retrofit2.HttpException
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

            if (!getUserInfo()){
                return DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
            }

            TempDB.isLogin.value = true

            DataResult.DataSuccess("")
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (e: Exception){
            return DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }

    suspend fun loginWithApiKey(apiKey: String): DataResult<Any> {
        return try {
            TempDB.saveToken(apiKey)
            if (!getUserInfo())
                return DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")

            appDatastore.setToken(apiKey)
            TempDB.isLogin.value = true
            DataResult.DataSuccess("")
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (_: Exception) {
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }

    suspend fun getTransactions(): DataResult<List<Transaction>> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = userApiService.getPayments()
            DataResult.DataSuccess(response.getMainResult()!!)
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (_: Exception) {
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }

    suspend fun getRequests(): DataResult<List<Request>> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = userApiService.getRequests()
            if (!response.status)
                return DataResult.DataError(response.message?:"")

            DataResult.DataSuccess(response.getMainResult()!!)
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (_: Exception) {
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }

    suspend fun sendRequests(title: String, release: String, type: String): DataResult<Any> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = userApiService.sendRequest(title, release, type)
            if (!response.status)
                return DataResult.DataError(response.message?: "اطلاعات غلط است")

            DataResult.DataSuccess("")
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (_: Exception) {
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }

    suspend fun getInviteInfo(): DataResult<InviteInfo> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = userApiService.getInviteInfo()
            if (!response.status)
                return DataResult.DataError(response.message?: "اطلاعات غلط است")

            DataResult.DataSuccess(response.getMainResult()!!)
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (_: Exception) {
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }

    suspend fun saveInviteCode(code: String): DataResult<Any> {
        return try {
            if (!connectionChecker.isConnect())
                return DataResult.DataError("لطفا اتصال اینترنت خود را بررسی کنید")

            val response = userApiService.saveInviteCode(code)
            if (!response.status)
                return DataResult.DataError(response.message?: "اطلاعات غلط است")

            TempDB.saveVipInfo(response.getMainResult())
            DataResult.DataSuccess("")
        } catch (e: HttpException){
            DataResult.DataError(e.response()?.errorBody()?.charStream()?.readText() ?: "")
        } catch (_: Exception) {
            DataResult.DataError("مشکلی پیش آمد لطفا مجدد امتحان کنید")
        }
    }

    suspend fun logout() {
        TempDB.saveVipInfo(null)
        appDatastore.setToken("")
        TempDB.isLogin.value = false
    }

    suspend fun getUserData(): UserData {
        return UserData(
            appDatastore.getAvatar(),
            appDatastore.getUsername(),
            appDatastore.getEmail()
        )
    }

    private suspend fun getUserInfo(): Boolean{
        return try {
            val userData = userApiService.getUserData().getMainResult()!!
            TempDB.saveVipInfo(userData.vipInfo)
            appDatastore.setAvatar(userData.avatar)
            appDatastore.setUsername(userData.username)
            appDatastore.setEmail(userData.email)
            true
        }catch (_:Exception){ false }
    }
}