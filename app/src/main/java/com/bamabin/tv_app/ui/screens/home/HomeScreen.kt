package com.bamabin.tv_app.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.VideoCameraFront
import androidx.compose.material.icons.outlined.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.bamabin.tv_app.R
import com.bamabin.tv_app.ui.screens.main.MainScreen

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel()
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val selectedMenuIndex by homeViewModel.selectedMenuIndex.collectAsState()

    Row {
        MenuList(
            modifier = Modifier
                .fillMaxHeight()
                .width((screenWidth * 0.2).dp),
            menus = homeViewModel.menuItems,
            selectedIndex = selectedMenuIndex,
        ) { menuIndex ->
            homeViewModel.updateSelectedMenu(menuIndex)
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width((screenWidth * 0.8).dp)
        ){
            MainScreen()
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MenuList(
    modifier: Modifier = Modifier,
    menus: Map<String, ImageVector>,
    selectedIndex: Int,
    onClick: (index: Int) -> Unit
) {
    var focusedItem by remember { mutableStateOf(-1) }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Image(painter = painterResource(id = R.drawable.logo_dark), contentDescription = "", modifier = Modifier.height(80.dp), contentScale = ContentScale.FillWidth)
        
        menus.keys.forEachIndexed { index, s ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(if (focusedItem == index) Color.Gray.copy(alpha = 0.5f) else Color.Transparent)
                .onFocusChanged {
                    if (it.isFocused) focusedItem = index
                    else if (focusedItem == index) focusedItem = -1
                }
                .focusRequester(focusRequester)
                .focusable()
                .clickable { onClick(index) }
                .padding(top = 12.dp, bottom = 12.dp, start = 32.dp)
            ) {
                Icon(
                    imageVector = menus[s]!!,
                    contentDescription = "",
                    tint = if (selectedIndex == index) Color.White else Color.Gray
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = s,
                    style = TextStyle(
                        color = if (selectedIndex == index) Color.White else Color.Gray,
                        fontWeight = if (selectedIndex == index) FontWeight.W600 else FontWeight.W500,
                        fontSize = TextUnit(
                            if (selectedIndex == index) 16f else 14f,
                            TextUnitType.Sp
                        )
                    )
                )
            }
        }
    }
}