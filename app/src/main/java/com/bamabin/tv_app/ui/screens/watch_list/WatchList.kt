package com.bamabin.tv_app.ui.screens.watch_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvGridItemSpan
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import com.bamabin.tv_app.ui.theme.Failed
import com.bamabin.tv_app.ui.widgeta.LoadingWidget
import com.bamabin.tv_app.ui.widgeta.MovieCard
import com.bamabin.tv_app.utils.Routes

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun WatchListsScreen(
    navHostController: NavHostController,
    viewModel: WatchListViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            androidx.compose.material3.Text(
                text = "علاقه‌مندی‌ها",
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.TopCenter),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(24f, TextUnitType.Sp)
                )
            )

            IconButton(
                colors = ButtonDefaults.colors(
                    containerColor = Color.Transparent
                ),
                onClick = { navHostController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        }

        if (viewModel.posts.isNotEmpty()){
            TvLazyVerticalGrid(
                columns = TvGridCells.Fixed(6),
                modifier = Modifier.padding(top = 24.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(viewModel.posts.size) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        MovieCard(post = viewModel.posts[it], height = 280.dp) {
                            navHostController.navigate("${Routes.POST_DETAILS.name}/${viewModel.posts[it].id}")
                        }

                        Button(
                            colors = ButtonDefaults.colors(
                                containerColor = Color.White,
                                focusedContainerColor = Failed,
                            ),
                            border = ButtonDefaults.border(
                                border = Border.None
                            ),
                            shape = ButtonDefaults.shape(shape = RoundedCornerShape(8.dp)),
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                            onClick = { viewModel.remove(it) }) {
                            androidx.compose.material3.Text(
                                text = "حذف",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    if (it == viewModel.posts.lastIndex){
                        viewModel.fetchData()
                    }
                }
                item(
                    span = { TvGridItemSpan(6) }
                ){
                    if (isLoading)
                        LoadingWidget(showText = false)
                }
            }
        } else if (isLoading) {
            LoadingWidget()
        }
    }
}