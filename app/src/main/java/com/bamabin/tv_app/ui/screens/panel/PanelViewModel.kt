package com.bamabin.tv_app.ui.screens.panel

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Redeem
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bamabin.tv_app.R
import com.bamabin.tv_app.data.local.TempDB
import com.bamabin.tv_app.data.remote.model.user.InviteInfo
import com.bamabin.tv_app.data.remote.model.user.Request
import com.bamabin.tv_app.data.remote.model.user.Transaction
import com.bamabin.tv_app.data.remote.model.user.UserData
import com.bamabin.tv_app.repo.AppRepository
import com.bamabin.tv_app.repo.UserRepository
import com.bamabin.tv_app.utils.DataResult
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class PanelViewModel @Inject constructor(
    private val repository: UserRepository,
    private val appRepository: AppRepository,
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    var userData: UserData

    val menuItems = mapOf(
        "حساب کاربری" to Icons.Outlined.AccountCircle,
        "درخواست فیلم و سریال" to Icons.Outlined.ContentCopy,
        "تراکنش های من" to Icons.Outlined.Star,
        "کد دعوت" to Icons.Outlined.Redeem,
        "تنظیمات" to Icons.Outlined.Tune,
        "تماس با ما" to Icons.Filled.HeadsetMic,
        "خروج از حساب کاربری" to Icons.AutoMirrored.Filled.Logout
    )

    private val _selectedMenu = MutableStateFlow(-1)
    val selectedMenu: StateFlow<Int> = _selectedMenu

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _showLogoutAlert = MutableStateFlow(false)
    val showLogoutAlert: StateFlow<Boolean> = _showLogoutAlert

    private val _transactions = mutableStateListOf<Transaction>()
    val transactions: SnapshotStateList<Transaction> = _transactions

    private val _requests = mutableStateListOf<Request>()
    val requests: SnapshotStateList<Request> = _requests

    private val _inviteInfo = MutableStateFlow<InviteInfo?>(null)
    val inviteInfo:StateFlow<InviteInfo?> = _inviteInfo

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _currentBgColor = MutableStateFlow(0)
    val currentBgColor: StateFlow<Int> = _currentBgColor

    private val _currentTextColor = MutableStateFlow(0)
    val currentTextColor: StateFlow<Int> = _currentTextColor

    private val _currentFont = MutableStateFlow(0)
    val currentFont: StateFlow<Int> = _currentFont

    private val _currentSize = MutableStateFlow(1)
    val currentSize: StateFlow<Int> = _currentSize

    val backgroundColors = listOf("مشکی", "تیره کم‌رنگ", "بی‌رنگ")
    val textColors = listOf("سفید", "زرد", "آبی")
    val fonts = listOf("ایران‌سنس", "وزیر متن", "دانا")
    val sizes = listOf("کوچک", "متوسط", "بزرگ")

    init {
        runBlocking { userData = repository.getUserData() }

        viewModelScope.launch {
            _currentTextColor.value = appRepository.getTextColor()
            _currentBgColor.value = appRepository.getBgColor()
            _currentFont.value = appRepository.getFont()
            _currentSize.value = appRepository.getSize()
        }
    }

    fun selectMenu(index:Int){
        if (index < menuItems.size - 1)
            _selectedMenu.value = index

        when(index){
            1 -> getRequests()
            2 -> getTransactions()
            3 -> getInviteInfo()
            menuItems.size - 1 -> _showLogoutAlert.value = true
        }
    }

    fun setColor(index: Int) = viewModelScope.launch {
        _currentTextColor.value = index
        appRepository.setTextColor(index)
    }

    fun setBgColor(index: Int) = viewModelScope.launch {
        _currentBgColor.value = index
        appRepository.setBgColor(index)
    }

    fun setFont(index: Int) = viewModelScope.launch {
        _currentFont.value = index
        appRepository.setFont(index)
    }

    fun setSize(index: Int) = viewModelScope.launch {
        _currentSize.value = index
        appRepository.setSize(index)
    }

    fun getTextSize() = when(_currentSize.value) {
        2 -> 48f
        1 -> 34f
        else -> 24f
    }

    fun getTextColor() = Color(when(_currentTextColor.value) {
        2 -> 0xFF2196F3
        1 -> 0xFFFFEB3B
        else -> 0xFFFFFFFF
    })

    fun getBgColor() = Color(when(_currentBgColor.value) {
        2 -> 0x00000000
        1 -> 0x88000000
        else -> 0xFF000000
    })

    fun getFont() = ResourcesCompat.getFont(
        context,
        when(_currentFont.value) {
            2 -> R.font.dana
            1 -> R.font.vazir
            else -> R.font.iransans
        }
    )!!

    fun getRequests()= viewModelScope.launch {
        if (_selectedMenu.value != 1)return@launch

        _isLoading.value = true
        val result = repository.getRequests()
        _isLoading.value = false

        if (result is DataResult.DataSuccess) {
            _requests.clear()
            _requests.addAll(result.data!!)
        }
    }

    fun saveInviteCode(code: String)= viewModelScope.launch {
        _isLoading.value = true
        val result = userRepository.saveInviteCode(code)
        _isLoading.value = false
        if (result is DataResult.DataSuccess){
            _inviteInfo.value = _inviteInfo.value!!.copy(
                isInvited = true
            )
        } else {
            _errorMessage.value = result.message
        }
    }

    fun hideErrorDialog() {_errorMessage.value = ""}

    fun logout() = viewModelScope.launch { repository.logout() }
    fun hideLogoutAlert() {
        _showLogoutAlert.value = false
    }

    fun generateQRCode(width: Int = 400, height: Int = 400): ImageBitmap {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(TempDB.supportLink, BarcodeFormat.QR_CODE, width, height)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        return bitmap.asImageBitmap()
    }

    private fun getTransactions()= viewModelScope.launch {
        _isLoading.value = true
        val result = repository.getTransactions()
        _isLoading.value = false
        if (result is DataResult.DataSuccess){
            _transactions.addAll(result.data!!)
        }
    }

    private fun getInviteInfo()= viewModelScope.launch {
        _isLoading.value = true
        val result = userRepository.getInviteInfo()
        _isLoading.value = false
        if (result is DataResult.DataSuccess){
            _inviteInfo.value = result.data
        }
    }
}