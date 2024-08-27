package com.bamabin.tv_app.ui.screens.search

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.bamabin.tv_app.R
import com.bamabin.tv_app.data.local.TempDB
import com.bamabin.tv_app.ui.widgeta.LoadingWidget
import com.bamabin.tv_app.ui.widgeta.MovieCard
import com.bamabin.tv_app.utils.DataResult
import com.bamabin.tv_app.utils.Routes
import java.util.Locale

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navHostController: NavHostController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchResult by viewModel.searchResult.collectAsState()

    var searchText by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK){
            it.data?.let {
                val result = it.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
                result?.let {
                    searchText = it
                    viewModel.search(it)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "جستجو",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF2D2D2D))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.9f)
                    .background(color = Color(0xFF1C1C22), shape = RoundedCornerShape(12.dp))
                    .height(48.dp),
                scale = CardDefaults.scale(focusedScale = 1f),
                colors = CardDefaults.colors(
                    containerColor = Color(0xFF1C1C22)
                ),
                onClick = { focusRequester.requestFocus() }) {
                BasicTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        viewModel.search(it)
                    },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                    interactionSource = interactionSource,
                    modifier = Modifier
                        .clickable(false) {}
                        .focusRequester(focusRequester)
                ) {
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = searchText,
                        innerTextField = it,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = "") },
                        placeholder = { Text(text = "جستجوی عنوان یا شناسه IMDB...", style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF7C7C7D))) },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            cursorColor = Color.White,
                        ),
                        contentPadding = OutlinedTextFieldDefaults.contentPadding(top = 0.dp, bottom = 0.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(
                onClick = {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "دنبال چی میگردی؟")
                    try {
                        launcher.launch(intent)
                    } catch (_:Exception){}
                }) {
                Icon(imageVector = Icons.Filled.Mic, contentDescription = "", tint = Color.White, modifier = Modifier.size(24.dp))
            }
        }

        searchResult?.let {
            if (it is DataResult.DataLoading){
                Box(modifier = Modifier.fillMaxWidth().height(100.dp)){
                    LoadingWidget(showText = false)
                }
            } else if (it is DataResult.DataSuccess) {
                TvLazyVerticalGrid(
                    columns = TvGridCells.Fixed(6),
                    contentPadding = PaddingValues(top = 40.dp, end = 16.dp, start = 16.dp)
                ) {
                    items(it.data!!.size) {index ->
                        MovieCard(it.data[index], Modifier.padding(bottom = 20.dp)) {
                            val id = it.data[index].id
                            navHostController.navigate("${Routes.POST_DETAILS.name}/$id")
                        }
                    }
                }
            }
        } ?: kotlin.run {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "محبوب ترین جستجو",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.W600
                ),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TvLazyColumn {
                items (TempDB.promotions.size) {
                    val p = TempDB.promotions[it]

                    Card(
                        colors = CardDefaults.colors(
                            containerColor = Color.Transparent
                        ),
                        scale = CardDefaults.scale(focusedScale = 1f),
                        onClick = { navHostController.navigate("${Routes.POST_DETAILS.name}/${TempDB.promotions[it].id}") }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (it % 2 == 0) Color(0xFF2C2C30) else Color.Transparent)
                                .padding(all = 8.dp)
                        ) {
                            AsyncImage(
                                model = p.thumbnail,
                                contentDescription = "",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .size(100.dp, 160.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = p.title,
                                    style = MaterialTheme.typography.bodyLarge.copy(Color.White, fontWeight = FontWeight.W600)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier
                                        .background(
                                            Color.Black,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .padding(all = 2.dp)
                                        .padding(end = 22.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape),
                                        painter = painterResource(id = R.drawable.imdb),
                                        contentDescription = ""
                                    )

                                    Spacer(modifier = Modifier.width(24.dp))

                                    Text(
                                        text = p.imdbRate + " ",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = TextUnit(14f, TextUnitType.Sp)
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row {
                                    for (i in 0 until p.genres.size) {
                                        Text(
                                            text = p.genres[i].name,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = Color.White,
                                                fontSize = TextUnit(12f, TextUnitType.Sp)
                                            ),
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                            modifier = Modifier
                                                .padding(horizontal = 2.dp)
                                                .background(
                                                    Color.Black.copy(alpha = 0.7f),
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}