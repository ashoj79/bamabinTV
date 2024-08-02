package com.bamabin.tv_app.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.VideoCameraBack
import androidx.compose.material.icons.filled.Window
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.VideoCameraBack
import androidx.compose.material.icons.outlined.Window
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import com.bamabin.tv_app.R
import com.bamabin.tv_app.data.local.MenuItem
import com.bamabin.tv_app.utils.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {

    val menuItems = listOf(
        MenuItem("ورود", Icons.Outlined.Login, Icons.Filled.Login),
        MenuItem("خانه", Icons.Outlined.Home, Icons.Filled.Home, true),
        MenuItem("لیست ها", Icons.Outlined.Menu, Icons.Filled.Menu),
        MenuItem("دسته بندی ها", Icons.Outlined.Window, Icons.Filled.Window),
        MenuItem("جستجو", Icons.Outlined.Search, Icons.Filled.Search),
        MenuItem("پنل کاربری", Icons.Outlined.PersonOutline, Icons.Filled.Person),
        MenuItem("فیلم ها", Icons.Outlined.Movie, Icons.Filled.Movie),
        MenuItem("سریال ها", Icons.Outlined.VideoCameraBack, Icons.Filled.VideoCameraBack),
        MenuItem("انیمیشن ها", R.drawable.animation),
        MenuItem("انیمه ها", R.drawable.anime),
        MenuItem("خرید اشتراک", Icons.Outlined.ShoppingCart, Icons.Filled.ShoppingCart),
    )

    private val _selectedMenuIndex = MutableStateFlow(1)
    val selectedMenuIndex:StateFlow<Int> = _selectedMenuIndex

    fun updateSelectedMenu(index: Int) {
        _selectedMenuIndex.value = index
    }
}