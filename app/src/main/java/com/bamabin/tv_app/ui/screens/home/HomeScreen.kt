package com.bamabin.tv_app.ui.screens.home

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonPin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.bamabin.tv_app.BuildConfig
import com.bamabin.tv_app.R
import com.bamabin.tv_app.data.local.MenuIconType
import com.bamabin.tv_app.data.local.MenuItem
import com.bamabin.tv_app.data.local.MenuPage
import com.bamabin.tv_app.data.local.PostType
import com.bamabin.tv_app.data.local.TempDB
import com.bamabin.tv_app.ui.screens.archive.PostTypeArchiveScreen
import com.bamabin.tv_app.ui.screens.genres_archive.GenresArchive
import com.bamabin.tv_app.ui.screens.main.MainScreen
import com.bamabin.tv_app.ui.screens.panel.PanelScreen
import com.bamabin.tv_app.ui.screens.search.SearchScreen
import com.bamabin.tv_app.utils.Routes

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val isLogin by TempDB.isLogin.collectAsState()
    val menuItems = homeViewModel.getMenuItems(isLogin)

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    var padding = (screenHeight - 80 - 24 - menuItems.size * 40) / (menuItems.size - 1) / 2
    padding = maxOf(8, padding)

    val selectedMenuIndex by homeViewModel.selectedMenuIndex.collectAsState()

    LaunchedEffect(isLogin) {
        homeViewModel.handleLoginChange()
    }

    Row {
        MenuList(
            width = (screenWidth * 0.2).dp,
            menus = menuItems,
            selectedIndex = selectedMenuIndex,
            padding = padding,
            isLogin = isLogin
        ) { menuIndex ->
            menuItems[menuIndex].route?.let {
                navHostController.navigate(it)
            } ?: homeViewModel.updateSelectedMenu(menuIndex)
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when (homeViewModel.getMenuPage()) {
                MenuPage.GENRES -> GenresArchive(navHostController = navHostController)
                MenuPage.SEARCH -> SearchScreen(navHostController = navHostController)
                MenuPage.MOVIES -> PostTypeArchiveScreen(
                    postType = PostType.MOVIE,
                    navHostController = navHostController
                )

                MenuPage.SERIES -> PostTypeArchiveScreen(
                    postType = PostType.SERIES,
                    navHostController = navHostController
                )

                MenuPage.ANIMATIONS -> PostTypeArchiveScreen(
                    postType = PostType.ANIMATION,
                    navHostController = navHostController
                )

                MenuPage.ANIME -> PostTypeArchiveScreen(
                    postType = PostType.ANIME,
                    navHostController = navHostController
                )

                MenuPage.PANEL -> PanelScreen(navHostController)

                else -> MainScreen(navHostController)
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MenuList(
    width: Dp,
    menus: List<MenuItem>,
    selectedIndex: Int,
    padding: Int,
    isLogin: Boolean,
    onClick: (index: Int) -> Unit
) {
    val days by TempDB.vipDays.collectAsState()

    var focusedIndex by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()
    val firstMenuFocusRequester = FocusRequester()
    val anotherMenuFocusRequester = FocusRequester()

    val widthAnim by animateDpAsState(
        targetValue = if (focusedIndex > -1) width else 80.dp,
        animationSpec = TweenSpec(durationMillis = 100)
    )

    LaunchedEffect(focusedIndex) {
        if (focusedIndex == 0)
            scrollState.animateScrollTo(0)
        else if (focusedIndex == menus.size - 1)
            scrollState.animateScrollTo(scrollState.maxValue)
    }

    LaunchedEffect(Unit) {
        firstMenuFocusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .width(widthAnim)
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(fraction = if (isLogin) 0.85f else 1f)
                .verticalScroll(scrollState)
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo_dark),
                contentDescription = "",
                modifier = Modifier.height(if (focusedIndex > -1) 80.dp else 0.dp),
                contentScale = ContentScale.FillWidth
            )

            Image(
                painter = painterResource(id = R.drawable.small_logo),
                contentDescription = "",
                modifier = Modifier
                    .height(if (focusedIndex == -1) 80.dp else 0.dp)
                    .width(80.dp)
            )

            menus.forEachIndexed { index, menu ->
                Card(
                    onClick = { onClick(index) },
                    colors = CardDefaults.colors(
                        containerColor = Color.Transparent,
                        focusedContainerColor = Color.White
                    ),
                    border = CardDefaults.border(
                        focusedBorder = Border.None
                    ),
                    modifier = Modifier
                        .padding(vertical = padding.dp)
                        .onFocusChanged {
                            if (it.isFocused) focusedIndex = index
                            else if (focusedIndex > -1) focusedIndex = -1
                        }
                        .focusRequester(if (menu.isPrimary) firstMenuFocusRequester else anotherMenuFocusRequester)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (menu.type == MenuIconType.ICON) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = menu.getIcon(selectedIndex == index),
                                contentDescription = "",
                                tint = if (focusedIndex == index) Color.Black else if (selectedIndex == index) Color.White else Color.Gray
                            )
                        } else if (menu.type == MenuIconType.SVG) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = menu.getImage()),
                                contentDescription = "",
                                tint = if (focusedIndex == index) Color.Black else if (selectedIndex == index) Color.White else Color.Gray
                            )
                        } else {
                            Image(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = menu.getImage(selectedIndex == index)),
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(
                                    color = if (focusedIndex == index) Color.Black else if (selectedIndex == index) Color.White else Color.Gray
                                ),
                                alpha = 1f
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = menu.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (focusedIndex == index) Color.Black else if (selectedIndex == index) Color.White else Color.Gray,
                                fontWeight = if (selectedIndex == index) FontWeight.W700 else FontWeight.W600,
                                fontSize = TextUnit(
                                    14f,
                                    TextUnitType.Sp
                                )
                            ),
                            maxLines = 1
                        )
                    }
                }
            }
        }

        if (isLogin) {
            Spacer(modifier = Modifier.height(16.dp))
            if (focusedIndex > -1){
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(
                            color = Color.White.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(all = 8.dp)
                ) {
                    Icon(imageVector = Icons.Outlined.PersonPin, contentDescription = "")
                    Text(text = " ${TempDB.vipInfo?.days} ", textAlign = TextAlign.Right)
                    Text(text = "روز", textAlign = TextAlign.Right)
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(all = 8.dp)
                ) {
                    Icon(imageVector = Icons.Outlined.PersonPin, contentDescription = "")
                    Text(text = " $days ", textAlign = TextAlign.Right)
                }
            }
        }
    }
}