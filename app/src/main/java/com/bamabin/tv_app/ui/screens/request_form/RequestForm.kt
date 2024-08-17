package com.bamabin.tv_app.ui.screens.request_form

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
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
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import com.bamabin.tv_app.ui.widgeta.ErrorDialog
import com.bamabin.tv_app.ui.widgeta.LoadingDialog
import com.bamabin.tv_app.utils.DataResult
import com.bamabin.tv_app.utils.POST_TYPES

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun RequestForm(
    navHostController: NavHostController,
    viewModel: RequestFormViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    val titleFocusRequester = remember { FocusRequester() }
    var year by remember { mutableStateOf("") }
    val yearFocusRequester = remember { FocusRequester() }

    var selectedType by remember { mutableIntStateOf(0) }

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
                text = "ثبت درخواست جدید",
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.TopCenter),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(35f, TextUnitType.Sp)
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
            onClick = { titleFocusRequester.requestFocus() }) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(text = "عنوان فیلم / سریال / انیمیشن / انیمه", style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)) },
                singleLine = true,
                maxLines = 1,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontSize = TextUnit(20f, TextUnitType.Sp),
                    textAlign = TextAlign.Center
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
                    .focusRequester(titleFocusRequester)
            )
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
            onClick = { yearFocusRequester.requestFocus() }) {
            OutlinedTextField(
                value = year,
                onValueChange = { year = if (year.length < 4) it else year },
                label = { Text(text = "سال تولید", style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)) },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontSize = TextUnit(20f, TextUnitType.Sp),
                    textAlign = TextAlign.Center
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
                    .focusRequester(yearFocusRequester)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.padding(horizontal = 160.dp)
        ) {
            Text(
                text = "نوع اثر",
                style = MaterialTheme.typography.bodyLarge.copy(Color.White)
            )
            Spacer(modifier = Modifier.weight(1f))
            for (i in POST_TYPES.indices) {
                Button(
                    colors = ButtonDefaults.colors(
                        containerColor = if (selectedType == i) MaterialTheme.colorScheme.primary else Color.Black.copy(
                            alpha = 0.2f
                        ),
                        focusedContainerColor = if (selectedType == i) MaterialTheme.colorScheme.primary else Color.Black.copy(
                            alpha = 0.2f
                        ),
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
                    modifier = Modifier.padding(horizontal = 4.dp),
                    onClick = { selectedType = i }) {
                    Text(
                        POST_TYPES[i],
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White
                        )
                    )
                }
            }
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
            onClick = { viewModel.sendRequest(title, year, selectedType) }) {
            Text(
                text = "ثبت درخواست",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (result is DataResult.DataLoading)
        LoadingDialog()

    if (result is DataResult.DataError)
        ErrorDialog(message = result?.message?:"") {
            viewModel.retry()
        }
}