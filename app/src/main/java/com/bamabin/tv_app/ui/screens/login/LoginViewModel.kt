package com.bamabin.tv_app.ui.screens.login

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bamabin.tv_app.data.remote.UrlHelper
import com.bamabin.tv_app.repo.UserRepository
import com.bamabin.tv_app.utils.DataResult
import com.bamabin.tv_app.utils.SocketHelper
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository,
    private val urlHelper: UrlHelper
): ViewModel() {
    private val characters: List<Char> = ('A'..'Z') + ('a' .. 'z') + ('0' .. '9')
    private var token = characters.shuffled().slice(0..4).joinToString("")

    var link = ""
    val username = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val _qrCode = MutableStateFlow<ImageBitmap?>(null)
    val qrCode: StateFlow<ImageBitmap?> = _qrCode

    private val _loginStatus = MutableStateFlow<DataResult<Any>?>(null)
    val loginState: StateFlow<DataResult<Any>?> = _loginStatus

    init {
        connectSocket()
    }

    fun login() = viewModelScope.launch {
        if (username.value.trim().isEmpty()){
            _loginStatus.value = DataResult.DataError("نام کاربری خود را وارد کنید")
            return@launch
        }

        if (password.value.trim().isEmpty()){
            _loginStatus.value = DataResult.DataError("رمز ورود خود را وارد کنید")
            return@launch
        }

        _loginStatus.value = DataResult.DataLoading()

        _loginStatus.value = repository.loginWithUsername(username.value.trim(), password.value.trim())
    }

    fun retry(){
        _loginStatus.value = null
    }

    fun disconnect(){
        SocketHelper.disconnect()
    }

    private fun loginWithApiKey(apiKey: String) = viewModelScope.launch {
        _loginStatus.value = DataResult.DataLoading()
        _loginStatus.value = repository.loginWithApiKey(apiKey)
    }

    private fun connectSocket() {
        SocketHelper.connect(urlHelper.getDecryptedSocketUrl())
        SocketHelper.socket.on(Socket.EVENT_CONNECT) {
            SocketHelper.socket.emit("check_token", token)
        }

        SocketHelper.socket.on("new_token"){
            token = characters.shuffled().slice(0..4).joinToString("")
            SocketHelper.socket.emit("check_token", token)
        }

        SocketHelper.socket.on("api_key") {
            val apiKey = (it[0] as JSONObject).getString("api_key")
            loginWithApiKey(apiKey)
        }

        SocketHelper.socket.on("qr_code") {
            link = (it[0] as JSONObject).getString("data")
            generateQRCode()
        }
    }

    private fun generateQRCode(width: Int = 400, height: Int = 400) {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(link, BarcodeFormat.QR_CODE, width, height)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        _qrCode.value = bitmap.asImageBitmap()
    }

    override fun onCleared() {
        super.onCleared()
        try {
            SocketHelper.disconnect()
        }catch (_:Exception){}
    }
}