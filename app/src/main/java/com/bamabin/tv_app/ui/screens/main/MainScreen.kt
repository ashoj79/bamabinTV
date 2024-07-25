package com.bamabin.tv_app.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.bamabin.tv_app.data.remote.model.videos.HomeSection

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = viewModel()
) {
    val isLoading by mainViewModel.isLoading.collectAsState()

    if (isLoading){
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp), contentAlignment = Alignment.Center){
            Box(modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color.White,
                    shape = CircleShape
                )
                .padding(all = 8.dp)
            ){
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            items(mainViewModel.homeSections.size) {
                Section(homeSection = mainViewModel.homeSections[it])
            }
        }
    }
}

@Composable
fun Section(homeSection: HomeSection) {
    var selectedItem by remember { mutableIntStateOf(-1) }

    Column(
        modifier = Modifier
            .padding(bottom = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(32.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(
                            topEnd = 80.dp,
                            bottomEnd = 80.dp
                        )
                    )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = homeSection.name,
                style = TextStyle(
                    fontWeight = FontWeight.W700
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            for (it in 0 until homeSection.posts.size) {
                Column(
                    modifier = Modifier
                        .width(120.dp)
                        .height(200.dp)
                        .background(if (selectedItem == it) Color.Gray.copy(alpha = 0.3f) else Color.Transparent)
                        .border(
                            width = if (selectedItem == it) 2.dp else 0.dp,
                            color = if (selectedItem == it) MaterialTheme.colorScheme.primary else Color.Transparent
                        )
                        .onFocusChanged { f ->
                            if (f.isFocused) selectedItem = it
                            else if (selectedItem == it) selectedItem = -1
                        }
                        .focusable()
                        .clickable {}
                        .padding(
                            top = 8.dp,
                            start = 8.dp,
                            end = 8.dp
                        )
                ) {
                    SubcomposeAsyncImage(
                        model = homeSection.posts[it].thumbnail,
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds,
                        loading = {
                            CircularProgressIndicator()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = homeSection.posts[it].title,
                        style = TextStyle(
                            fontSize = TextUnit(
                                12f,
                                TextUnitType.Sp
                            )
                        ),
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}