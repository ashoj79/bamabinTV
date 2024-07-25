package com.bamabin.tv_app.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.VideoCameraBack
import androidx.compose.material.icons.outlined.Window
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {

    val menuItems = mapOf(
        "خانه" to Icons.Outlined.Home,
        "لیست ها" to Icons.Outlined.Menu,
        "ژانر ها" to Icons.Outlined.Window,
        "جستجو" to Icons.Outlined.Search,
        "پنل کاربری" to Icons.Outlined.PersonOutline,
        "فیلم ها" to Icons.Outlined.Movie,
        "سریال ها" to Icons.Outlined.VideoCameraBack,
        "خرید اشتراک" to Icons.Outlined.ShoppingCart,
    )

    private val _selectedMenuIndex = MutableStateFlow(0)
    val selectedMenuIndex:StateFlow<Int> = _selectedMenuIndex

    fun updateSelectedMenu(index: Int) {
        _selectedMenuIndex.value = index
    }
}