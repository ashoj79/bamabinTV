package com.bamabin.tv_app.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
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
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.VideoCameraBack
import androidx.compose.material.icons.outlined.Window
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bamabin.tv_app.R
import com.bamabin.tv_app.data.local.MenuItem
import com.bamabin.tv_app.data.local.MenuPage
import com.bamabin.tv_app.data.local.TempDB
import com.bamabin.tv_app.repo.UserRepository
import com.bamabin.tv_app.utils.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {

    private val notLoggedInMenuItems = listOf(
        MenuItem("ورود", Icons.Outlined.Login, Icons.Filled.Login, route = Routes.LOGIN.name),
        MenuItem("خانه", Icons.Outlined.Home, Icons.Filled.Home, true, page = MenuPage.HOME),
        MenuItem("دسته‌بندی‌ها", Icons.Outlined.Window, Icons.Filled.Window, page = MenuPage.GENRES),
        MenuItem("جستجو", Icons.Outlined.Search, Icons.Filled.Search, page = MenuPage.SEARCH),
        MenuItem("فیلم‌ها", Icons.Outlined.Movie, Icons.Filled.Movie, page = MenuPage.MOVIES),
        MenuItem("سریال‌ها", Icons.Outlined.VideoCameraBack, Icons.Filled.VideoCameraBack, page = MenuPage.SERIES),
        MenuItem("انیمیشن‌ها", R.drawable.animation, page = MenuPage.ANIMATIONS),
        MenuItem("انیمه‌ها", R.drawable.anime, page = MenuPage.ANIME),
    )

    private val loggedInMenuItems = listOf(
        MenuItem("خانه", Icons.Outlined.Home, Icons.Filled.Home, true, page = MenuPage.HOME),
        MenuItem("خرید اشتراک", Icons.Outlined.ShoppingCart, Icons.Filled.ShoppingCart, route = Routes.SUBSCRIBE.name),
        MenuItem("دسته‌بندی‌ها", Icons.Outlined.Window, Icons.Filled.Window, page = MenuPage.GENRES),
        MenuItem("جستجو", Icons.Outlined.Search, Icons.Filled.Search, page = MenuPage.SEARCH),
        MenuItem("پنل کاربری", Icons.Outlined.PersonOutline, Icons.Filled.Person, page = MenuPage.PANEL),
        MenuItem("فیلم ها", Icons.Outlined.Movie, Icons.Filled.Movie, page = MenuPage.MOVIES),
        MenuItem("سریال‌ها", Icons.Outlined.VideoCameraBack, Icons.Filled.VideoCameraBack, page = MenuPage.SERIES),
        MenuItem("انیمیشن‌ها", R.drawable.animation, page = MenuPage.ANIMATIONS),
        MenuItem("انیمه‌ها", R.drawable.anime, page = MenuPage.ANIME),
        MenuItem("لیست‌ها", Icons.Outlined.Menu, Icons.Filled.Menu)
    )

    private var loginStatus = TempDB.isLogin.value

    private val _selectedMenuIndex = MutableStateFlow(if (loginStatus) 0 else 1)
    val selectedMenuIndex:StateFlow<Int> = _selectedMenuIndex

    fun updateSelectedMenu(index: Int) {
        _selectedMenuIndex.value = index
    }

    fun getMenuItems(isLogin: Boolean) = if (isLogin) loggedInMenuItems else notLoggedInMenuItems

    fun handleLoginChange(){
        if (loginStatus == TempDB.isLogin.value)return

        loginStatus = TempDB.isLogin.value
        if (loginStatus) updateSelectedMenu(0)
        else updateSelectedMenu(1)
    }
}