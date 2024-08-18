package com.bamabin.tv_app.ui.screens.subscribe

import android.graphics.Bitmap
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SubscribeViewModel @Inject constructor(
    private val repository: UserRepository,
    private val urlHelper: UrlHelper
): ViewModel(){
    private var username: String

    private val _result = MutableStateFlow<DataResult<Any>?>(null)
    val result: StateFlow<DataResult<Any>?> = _result

    private val _urlResult = MutableStateFlow<DataResult<String>>(DataResult.DataLoading())
    val urlResult: StateFlow<DataResult<String>> = _urlResult

    init {
        runBlocking { username = repository.getUserData().username }
        getUrl()
        connectSocket()
    }

    fun getUrl() = viewModelScope.launch {
        _urlResult.value = DataResult.DataLoading()
        _urlResult.value = repository.getPayLink()
    }

    fun getQRCode(width: Int = 400, height: Int = 400): ImageBitmap {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(_urlResult.value.data, BarcodeFormat.QR_CODE, width, height)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        return bitmap.asImageBitmap()
    }

    fun retry(){
        _result.value = null
    }

    private fun connectSocket() {
        SocketHelper.connect(urlHelper.getDecryptedSocketUrl())
        SocketHelper.socket.on(Socket.EVENT_CONNECT) {
            SocketHelper.socket.emit("wait_for_pay", username)
        }

        SocketHelper.socket.on("pay_done"){
            _result.value = DataResult.DataLoading()
            viewModelScope.launch { _result.value = repository.updateVipInfo()}
        }
    }
}