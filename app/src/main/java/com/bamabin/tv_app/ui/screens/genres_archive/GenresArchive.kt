package com.bamabin.tv_app.ui.screens.genres_archive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.bamabin.tv_app.data.local.TempDB
import com.bamabin.tv_app.data.remote.model.videos.Genre
import com.bamabin.tv_app.utils.Routes

@Composable
fun GenresArchive(
    navHostController: NavHostController
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "دسته بندی ها",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        TvLazyVerticalGrid(
            columns = TvGridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(TempDB.genres.size) {
                GenreItem(TempDB.genres[it]){
                    val id = TempDB.genres[it].id
                    val title = TempDB.genres[it].name

                    navHostController.navigate("${Routes.TAXONOMY_POSTS.name}/genres/$id/$title")
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun GenreItem(genre: Genre, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.colors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier.padding(8.dp),
        onClick = onClick) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .height(160.dp)
        ) {
            AsyncImage(
                model = genre.backgroundUrl,
                contentDescription = "",
                modifier = Modifier.fillMaxSize().blur(16.dp)
            )

            AsyncImage(
                model = genre.icon,
                contentDescription = "",
                modifier = Modifier
                    .padding(top = 32.dp)
                    .size(48.dp)
                    .align(Alignment.TopCenter)
            )

            Text(
                text = genre.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W700),
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 48.dp)
            )

            Text(
                text = "${genre.count} فیلم",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W700),
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp)
            )
        }
    }
}