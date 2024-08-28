package com.bamabin.tv_app.ui.screens.comment_form

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Checkbox
import androidx.tv.material3.CheckboxDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import com.bamabin.tv_app.ui.widgeta.ErrorDialog
import com.bamabin.tv_app.ui.widgeta.LoadingDialog
import com.bamabin.tv_app.utils.DataResult

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun CommentFormScreen(
    navHostController: NavHostController,
    viewModel: CommentFormViewModel = hiltViewModel()
) {
    var content by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var hasSpoil by remember { mutableStateOf(false) }
    var isCheckboxFocused by remember { mutableStateOf(false) }

    val result by viewModel.result.collectAsState()

    LaunchedEffect(result) {
        result?.let {
            if (it is DataResult.DataSuccess)
                navHostController.popBackStack()
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "ثبت نظر جدید",
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
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.colors(
                containerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
            border = CardDefaults.border(
                border = Border.None,
                focusedBorder = Border.None
            ),
            onClick = { focusRequester.requestFocus() }) {
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text(text = "متن نظر", style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)) },
                minLines = 5,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontSize = TextUnit(20f, TextUnitType.Sp),
                ),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFC2C2C2),
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    cursorColor = Color(0xFFC2C2C2),
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 160.dp)
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally)
                    .focusRequester(focusRequester)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 160.dp)
        ) {
            Checkbox(
                colors = CheckboxDefaults.colors(
                    uncheckedColor = if (isCheckboxFocused) MaterialTheme.colorScheme.primary else Color.White
                ),
                checked = hasSpoil,
                onCheckedChange = { hasSpoil = it },
                modifier = Modifier
                    .onFocusChanged { isCheckboxFocused = it.isFocused }
            )
            Spacer(modifier = Modifier.padding(start = 16.dp))
            Text(
                text = "نظر شما حاوی اسپویل است؟",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            colors = ButtonDefaults.colors(
                containerColor = Color.White,
                focusedContainerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 160.dp),
            onClick = { viewModel.saveComment(content, hasSpoil) }) {
            Text(
                text = "ثبت نظر",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (result is DataResult.DataLoading)
        LoadingDialog()

    if (result is DataResult.DataError)
        ErrorDialog(
            message = result?.message?:"",
            onRetryClick = { viewModel.retry() },
            onCloseClick = { viewModel.retry() }
        )
}