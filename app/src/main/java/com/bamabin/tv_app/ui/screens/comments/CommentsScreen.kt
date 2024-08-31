package com.bamabin.tv_app.ui.screens.comments

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import coil.compose.AsyncImage
import com.bamabin.tv_app.data.local.TempDB
import com.bamabin.tv_app.data.remote.model.comment.Comment
import com.bamabin.tv_app.ui.widgeta.ErrorDialog
import com.bamabin.tv_app.ui.widgeta.LoadingWidget
import com.bamabin.tv_app.utils.DataResult
import com.bamabin.tv_app.utils.Routes

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun CommentsScreen(
    navHostController: NavHostController,
    viewModel: CommentsViewModel = hiltViewModel()
) {
    val result by viewModel.result.collectAsState()
    val isLogin by TempDB.isLogin.collectAsState()

    val defaultFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (isLogin)
            defaultFocusRequester.requestFocus()
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = viewModel.title,
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

        Spacer(modifier = Modifier.height(16.dp))

        if (isLogin) {
            Button(
                colors = ButtonDefaults.colors(
                    containerColor = Color.White,
                    focusedContainerColor = MaterialTheme.colorScheme.primary,
                    focusedContentColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
                border = ButtonDefaults.border(
                    focusedBorder = Border(
                        border = BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ),
                    border = Border.None
                ),
                shape = ButtonDefaults.shape(shape = RoundedCornerShape(8.dp)),
                modifier = Modifier.align(Alignment.End).focusRequester(defaultFocusRequester),
                onClick = { navHostController.navigate("${Routes.COMMENT_FORM.name}/${viewModel.id}/0/null") }) {
                Text(
                    text = "ثبت نظر",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (result is DataResult.DataSuccess){
            TvLazyColumn {
                items(result.data!!.size) {
                    CommentRow(
                        comment = result.data!![it],
                        onLikeClick = { viewModel.like(result.data!![it].id) },
                        onDislikeClick = { viewModel.dislike(result.data!![it].id) },
                        onReplyClick = {
                            navHostController.navigate("${Routes.COMMENT_FORM.name}/${viewModel.id}/${result.data!![it].id}/${result.data!![it].author}")
                        }
                    )
                }
            }
        }
    }

    if (result is DataResult.DataLoading)
        LoadingWidget()

    if (result is DataResult.DataError)
        ErrorDialog(message = result.message, onCloseClick = {viewModel.closeError()}, onRetryClick = {viewModel.getComments()})
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun CommentRow(
    comment: Comment,
    onLikeClick: () -> Unit,
    onDislikeClick: () -> Unit,
    onReplyClick: () -> Unit
) {
    var showText by remember { mutableStateOf(!comment.hasSpoil) }
    var isLiked by remember { mutableStateOf(false) }
    var isDisliked by remember { mutableStateOf(false) }
    var likesCount by remember { mutableIntStateOf(comment.likeInfo.likes) }
    var dislikesCount by remember { mutableIntStateOf(comment.likeInfo.dislikes) }

    Row(
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Card(
            colors = CardDefaults.colors(
                containerColor = Color(0xFF222327),
                focusedContainerColor = Color(0xFF222327),
            ),
            scale = CardDefaults.scale(focusedScale = 1f),
            shape = CardDefaults.shape(
                shape = RoundedCornerShape(8.dp),
                focusedShape = RoundedCornerShape(8.dp),
            ),
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = if (comment.prentId > 0) 32.dp else 0.dp
                ),
            onClick = {}
        ) {
            Row (
                modifier = Modifier.padding(16.dp)
            ) {
                AsyncImage(
                    model = comment.avatar,
                    contentDescription = "",
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = comment.author,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (showText) {
                        Text(
                            text = comment.content,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.White
                            )
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = "",
                                tint = Color.Yellow,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "این نظر حاوی اسپویلر است و داستان فیلم را لو می‌دهد",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(24.dp))

        Button(
            colors = ButtonDefaults.colors(
                containerColor = Color.Gray,
                focusedContainerColor = MaterialTheme.colorScheme.primary,
            ),
            border = ButtonDefaults.border(
                border = Border.None
            ),
            shape = ButtonDefaults.shape(shape = RoundedCornerShape(8.dp)),
            onClick = onReplyClick) {
            Text(
                text = "پاسخ",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        if (showText) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    colors = ButtonDefaults.colors(
                        containerColor = Color.Black.copy(alpha = .5f),
                        focusedContainerColor = Color.Black.copy(alpha = .5f),
                    ),
                    border = ButtonDefaults.border(
                        border = Border.None,
                        focusedBorder = Border(
                            border = BorderStroke(1.dp, Color.White),
                            shape = CircleShape
                        )
                    ),
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        if (isLiked || isDisliked) return@IconButton

                        isLiked = true
                        likesCount += 1
                        onLikeClick()
                    }
                ){
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                        contentDescription = "",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = likesCount.toString(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    colors = ButtonDefaults.colors(
                        containerColor = Color.Black.copy(alpha = .5f),
                        focusedContainerColor = Color.Black.copy(alpha = .5f),
                    ),
                    border = ButtonDefaults.border(
                        border = Border.None,
                        focusedBorder = Border(
                            border = BorderStroke(1.dp, Color.White),
                            shape = CircleShape
                        )
                    ),
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        if (isLiked || isDisliked) return@IconButton

                        isDisliked = true
                        dislikesCount += 1
                        onDislikeClick()
                    }
                ){
                    Icon(
                        imageVector = if (isDisliked) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
                        contentDescription = "",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = dislikesCount.toString(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White
                    )
                )
            }
        } else {
            Button(
                colors = ButtonDefaults.colors(
                    containerColor = Color.Gray,
                    focusedContainerColor = MaterialTheme.colorScheme.primary,
                ),
                border = ButtonDefaults.border(
                    border = Border.None
                ),
                shape = ButtonDefaults.shape(shape = RoundedCornerShape(8.dp)),
                onClick = { showText = true }) {
                Text(
                    text = "مشاهده نظر",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                )
            }
        }
    }
}