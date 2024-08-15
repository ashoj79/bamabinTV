package com.bamabin.tv_app.ui.screens.panel

import android.content.Context
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
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bamabin.tv_app.R
import com.bamabin.tv_app.data.remote.model.user.Request
import com.bamabin.tv_app.data.remote.model.user.Transaction
import com.bamabin.tv_app.data.remote.model.user.UserData
import com.bamabin.tv_app.repo.AppRepository
import com.bamabin.tv_app.repo.UserRepository
import com.bamabin.tv_app.utils.DataResult
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

    private val _transactions = mutableStateListOf<Transaction>()
    val transactions: SnapshotStateList<Transaction> = _transactions

    private val _requests = mutableStateListOf<Request>()
    val requests: SnapshotStateList<Request> = _requests

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
        _selectedMenu.value = index
        when(index){
            1 -> getRequests()
            2 -> getTransactions()
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

    private fun getTransactions()= viewModelScope.launch {
        _isLoading.value = true
        val result = repository.getTransactions()
        _isLoading.value = false
        if (result is DataResult.DataSuccess){
            _transactions.addAll(result.data!!)
        }
    }
}