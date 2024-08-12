package com.bamabin.tv_app.ui.widgeta

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ClosedCaption
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.bamabin.tv_app.R
import com.bamabin.tv_app.data.remote.model.videos.Post

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MovieCard(post: Post, modifier: Modifier = Modifier) {
    var isFocused by remember { mutableStateOf(false) }
    val animatedBottomMargin by animateDpAsState(
        targetValue = if (isFocused) 24.dp else 8.dp,
        animationSpec = TweenSpec(durationMillis = 100)
    )

    Card(
        onClick = {},
        colors = CardDefaults.colors(containerColor = Color.Transparent),
        border = CardDefaults.border(focusedBorder = Border.None),
        scale = CardDefaults.scale(focusedScale = 1.25f),
        modifier = modifier
            .padding(horizontal = 4.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .width(120.dp)
            .height(250.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .border(
                    width = if (isFocused) 2.dp else 0.dp,
                    color = if (isFocused) Color.White else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            AsyncImage(
                model = post.thumbnail,
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Text(
                text = post.releaseYear,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White,
                    fontSize = TextUnit(9f, TextUnitType.Sp)
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 8.dp, start = 8.dp)
                    .padding(horizontal = 2.dp)
                    .background(
                        Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(
                        bottom = animatedBottomMargin,
                        start = 8.dp
                    )
                    .background(
                        Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(all = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape),
                    painter = painterResource(id = R.drawable.imdb),
                    contentDescription = ""
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = post.imdbRate + " ",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = TextUnit(10f, TextUnitType.Sp)
                    )
                )
            }

            Icon(
                imageVector = Icons.Outlined.ClosedCaption,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        bottom = animatedBottomMargin,
                        end = 4.dp
                    )
                    .size(20.dp)
                    .background(Color.Black.copy(alpha = 0.7f), shape = CircleShape)
                    .padding(all = 3.dp)
            )

            if (post.hasAudio) {
                Icon(
                    imageVector = Icons.Outlined.Mic,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(
                            bottom = animatedBottomMargin,
                            end = 28.dp
                        )
                        .size(20.dp)
                        .background(Color.Black.copy(alpha = 0.7f), shape = CircleShape)
                        .padding(all = 3.dp)
                )
            }

            if (isFocused) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 2.dp, end = 8.dp, start = 8.dp)
                ) {
                    for (i in 0 until minOf(2, post.genres.size)) {
                        Text(
                            text = post.genres[i].name,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White,
                                fontSize = TextUnit(9f, TextUnitType.Sp)
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

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = post.title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = TextUnit(
                    12f,
                    TextUnitType.Sp
                ),
                color = if (isFocused) Color.White else Color.White.copy(alpha = 0.6f)
            ),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}