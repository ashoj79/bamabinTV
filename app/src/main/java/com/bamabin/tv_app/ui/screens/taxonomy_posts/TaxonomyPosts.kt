package com.bamabin.tv_app.ui.screens.taxonomy_posts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.bamabin.tv_app.ui.widgeta.LoadingWidget
import com.bamabin.tv_app.ui.widgeta.MovieCard
import com.bamabin.tv_app.utils.Routes

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TaxonomyPosts(
    navHostController: NavHostController,
    taxonomy: String,
    id: Int,
    title: String,
    viewModel: TaxonomyPostsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.setData(taxonomy, id)
    }

    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.TopCenter),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
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
            ) {
                item (
                    span = { TvGridItemSpan(6) }
                ) {
                    Filters()
                }

                items(viewModel.posts.size) {
                    MovieCard(viewModel.posts[it], Modifier.padding(bottom = 20.dp)) {
                        val id = viewModel.posts[it].id
                        navHostController.navigate("${Routes.POST_DETAILS.name}/$id")
                    }

                    if (it == viewModel.posts.lastIndex) viewModel.getPosts()
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

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun Filters(
    viewModel: TaxonomyPostsViewModel = hiltViewModel()
) {
    val selectedOrder by viewModel.orderBy.collectAsState()
    val selectedType by viewModel.postType.collectAsState()

    Row(
        modifier = Modifier.padding(bottom = 32.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(end = 16.dp, top = 16.dp)
                .background(
                    color = Color(0xFF333333),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(vertical = 4.dp)
        ) {
            Button(
                colors = ButtonDefaults.colors(
                    containerColor = if (selectedType == 1) MaterialTheme.colorScheme.primary else Color(0xFF212121),
                    focusedContainerColor = if (selectedType == 1) MaterialTheme.colorScheme.primary else Color(0xFF212121)
                ),
                border = ButtonDefaults.border(
                    border = Border.None,
                    focusedBorder = Border(
                        border = BorderStroke(2.dp, Color.White),
                        shape = RoundedCornerShape(12.dp)
                    )
                ),
                shape = ButtonDefaults.shape(
                    shape = RoundedCornerShape(12.dp),
                    focusedShape = RoundedCornerShape(12.dp)
                ),
                modifier = Modifier.padding(horizontal = 4.dp),
                onClick = { viewModel.setType(1) }) {
                androidx.tv.material3.Text(
                    text = "فیلم",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (selectedType == 1) Color(0xFF3F310E) else Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Button(
                colors = ButtonDefaults.colors(
                    containerColor = if (selectedType == 2) MaterialTheme.colorScheme.primary else Color(0xFF212121),
                    focusedContainerColor = if (selectedType == 2) MaterialTheme.colorScheme.primary else Color(0xFF212121)
                ),
                border = ButtonDefaults.border(
                    border = Border.None,
                    focusedBorder = Border(
                        border = BorderStroke(2.dp, Color.White),
                        shape = RoundedCornerShape(12.dp)
                    )
                ),
                shape = ButtonDefaults.shape(
                    shape = RoundedCornerShape(12.dp),
                    focusedShape = RoundedCornerShape(12.dp)
                ),
                modifier = Modifier.padding(horizontal = 4.dp),
                onClick = { viewModel.setType(2) }) {
                androidx.tv.material3.Text(
                    text = "سریال",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (selectedType == 2) Color(0xFF3F310E) else Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Button(
                colors = ButtonDefaults.colors(
                    containerColor = if (selectedType == 3) MaterialTheme.colorScheme.primary else Color(0xFF212121),
                    focusedContainerColor = if (selectedType == 3) MaterialTheme.colorScheme.primary else Color(0xFF212121)
                ),
                border = ButtonDefaults.border(
                    border = Border.None,
                    focusedBorder = Border(
                        border = BorderStroke(2.dp, Color.White),
                        shape = RoundedCornerShape(12.dp)
                    )
                ),
                shape = ButtonDefaults.shape(
                    shape = RoundedCornerShape(12.dp),
                    focusedShape = RoundedCornerShape(12.dp)
                ),
                modifier = Modifier.padding(horizontal = 4.dp),
                onClick = { viewModel.setType(3) }) {
                androidx.tv.material3.Text(
                    text = "انیمیشن",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (selectedType == 3) Color(0xFF3F310E) else Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Button(
                colors = ButtonDefaults.colors(
                    containerColor = if (selectedType == 4) MaterialTheme.colorScheme.primary else Color(0xFF212121),
                    focusedContainerColor = if (selectedType == 4) MaterialTheme.colorScheme.primary else Color(0xFF212121)
                ),
                border = ButtonDefaults.border(
                    border = Border.None,
                    focusedBorder = Border(
                        border = BorderStroke(2.dp, Color.White),
                        shape = RoundedCornerShape(12.dp)
                    )
                ),
                shape = ButtonDefaults.shape(
                    shape = RoundedCornerShape(12.dp),
                    focusedShape = RoundedCornerShape(12.dp)
                ),
                modifier = Modifier.padding(horizontal = 4.dp),
                onClick = { viewModel.setType(4) }) {
                androidx.tv.material3.Text(
                    text = "انیمه",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (selectedType == 4) Color(0xFF3F310E) else Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .padding(end = 16.dp, top = 16.dp)
                .background(
                    color = Color(0xFF333333),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(vertical = 4.dp)
        ) {
            Button(
                colors = ButtonDefaults.colors(
                    containerColor = if (selectedOrder == 1) MaterialTheme.colorScheme.primary else Color(0xFF212121),
                    focusedContainerColor = if (selectedOrder == 1) MaterialTheme.colorScheme.primary else Color(0xFF212121)
                ),
                border = ButtonDefaults.border(
                    border = Border.None,
                    focusedBorder = Border(
                        border = BorderStroke(2.dp, Color.White),
                        shape = RoundedCornerShape(12.dp)
                    )
                ),
                shape = ButtonDefaults.shape(
                    shape = RoundedCornerShape(12.dp),
                    focusedShape = RoundedCornerShape(12.dp)
                ),
                modifier = Modifier.padding(horizontal = 4.dp),
                onClick = { viewModel.setOrder(1) }) {
                androidx.tv.material3.Text(
                    text = "سال انتشار",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (selectedOrder == 1) Color(0xFF3F310E) else Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Button(
                colors = ButtonDefaults.colors(
                    containerColor = if (selectedOrder == 2) MaterialTheme.colorScheme.primary else Color(0xFF212121),
                    focusedContainerColor = if (selectedOrder == 2) MaterialTheme.colorScheme.primary else Color(0xFF212121)
                ),
                border = ButtonDefaults.border(
                    border = Border.None,
                    focusedBorder = Border(
                        border = BorderStroke(2.dp, Color.White),
                        shape = RoundedCornerShape(12.dp)
                    )
                ),
                shape = ButtonDefaults.shape(
                    shape = RoundedCornerShape(12.dp),
                    focusedShape = RoundedCornerShape(12.dp)
                ),
                modifier = Modifier.padding(horizontal = 4.dp),
                onClick = { viewModel.setOrder(2) }) {
                androidx.tv.material3.Text(
                    text = "بروزترین ها",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (selectedOrder == 2) Color(0xFF3F310E) else Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Button(
                colors = ButtonDefaults.colors(
                    containerColor = if (selectedOrder == 3) MaterialTheme.colorScheme.primary else Color(0xFF212121),
                    focusedContainerColor = if (selectedOrder == 3) MaterialTheme.colorScheme.primary else Color(0xFF212121)
                ),
                border = ButtonDefaults.border(
                    border = Border.None,
                    focusedBorder = Border(
                        border = BorderStroke(2.dp, Color.White),
                        shape = RoundedCornerShape(12.dp)
                    )
                ),
                shape = ButtonDefaults.shape(
                    shape = RoundedCornerShape(12.dp),
                    focusedShape = RoundedCornerShape(12.dp)
                ),
                modifier = Modifier.padding(horizontal = 4.dp),
                onClick = { viewModel.setOrder(3) }) {
                androidx.tv.material3.Text(
                    text = "امتیاز IMDB",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (selectedOrder == 3) Color(0xFF3F310E) else Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}