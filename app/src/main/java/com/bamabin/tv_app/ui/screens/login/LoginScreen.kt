package com.bamabin.tv_app.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import com.bamabin.tv_app.R
import com.bamabin.tv_app.ui.widgeta.ErrorDialog
import com.bamabin.tv_app.ui.widgeta.LoadingDialog
import com.bamabin.tv_app.ui.widgeta.LoadingWidget
import com.bamabin.tv_app.utils.DataResult

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navHostController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val qrCode by viewModel.qrCode.collectAsState()
    val loginStatus by viewModel.loginState.collectAsState()

    val screenHeight = LocalConfiguration.current.screenHeightDp

    val usernameInteractionSource = remember { MutableInteractionSource() }
    val passwordInteractionSource = remember { MutableInteractionSource() }
    val usernameFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val homeFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        homeFocusRequester.requestFocus()
    }

    LaunchedEffect(loginStatus) {
        loginStatus?.let {
            if (it is DataResult.DataSuccess)
                navHostController.popBackStack()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.disconnect()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.focusRequester(homeFocusRequester),
                onClick = { navHostController.popBackStack() }
            ) {
                Icon(imageVector = Icons.Filled.Home, contentDescription = "", tint = Color.White)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                "ورود به حساب کاربری بامابین",
                style = MaterialTheme.typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color.Gray, fontSize = TextUnit(14f, TextUnitType.Sp))) {
                            append("روش اول   ")
                        }
                        append("ورود با اسکن کد QR:")
                    },
                    style = MaterialTheme.typography.titleMedium.copy(Color.White)
                )

                Spacer(modifier = Modifier.height(16.dp))

                qrCode?.let {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .height(minOf((screenHeight * 0.4).dp, 400.dp))
                    ) {
                        Image(
                            bitmap = it,
                            contentDescription = "",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .align(Alignment.Center)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "کد بالا رو با دوربین تلفن همراهتون یا یک برنامه‌ی بارکدخوان اسکن کنین و از این طریق وارد بشین.\n\n" +
                                "یا نشانی زیر رو توی مرورگر گوشی یا رایانه‌تون وارد کنین و اینتر رو بزنین. چند لحظه صبر کنین تا تلویزیونتون هم لاگین بشه.",
                        style = MaterialTheme.typography.titleMedium.copy(Color.White)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = viewModel.link,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = TextUnit(24f, TextUnitType.Sp),
                            fontFamily = FontFamily(Font(R.font.vazir, weight = FontWeight.SemiBold))
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                } ?: LoadingWidget(showText = false)
            }

            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight(0.5f)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.White)
                        )
                    )
                )

                Text(
                    text = "یا",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                )

                Spacer(modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.White, Color.Transparent)
                        )
                    )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color.Gray, fontSize = TextUnit(14f, TextUnitType.Sp))) {
                            append("روش دوم   ")
                        }
                        append("ورود با نام کاربری و رمز عبور:")
                    },
                    style = MaterialTheme.typography.titleMedium.copy(Color.White)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "نام کاربری و رمز عبور حساب کاربری خودتون را وارد کنین",
                    style = MaterialTheme.typography.titleMedium.copy(Color.White)
                )

                Spacer(modifier = Modifier.fillMaxHeight(0.1f))

                Card(
                    colors = CardDefaults.colors(
                        containerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    border = CardDefaults.border(
                        border = Border.None,
                        focusedBorder = Border.None
                    ),
                    onClick = { usernameFocusRequester.requestFocus() }) {

                    OutlinedTextField(
                        value = username,
                        onValueChange = { viewModel.username.value = it },
                        label = {
                            Text(
                                text = "نام کاربری",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                            )
                        },
                        singleLine = true,
                        minLines = 1,
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
                            .padding(horizontal = 40.dp)
                            .padding(top = 8.dp)
                            .align(Alignment.CenterHorizontally)
                            .focusRequester(usernameFocusRequester)
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
                    onClick = { passwordFocusRequester.requestFocus() }) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { viewModel.password.value = it },
                        label = {
                            Text(
                                text = "رمز عبور",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                            )
                        },
                        singleLine = true,
                        minLines = 1,
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
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp)
                            .padding(top = 8.dp)
                            .align(Alignment.CenterHorizontally)
                            .focusRequester(passwordFocusRequester)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    colors = ButtonDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        focusedContainerColor = MaterialTheme.colorScheme.primary,
                    ),
                    shape = ButtonDefaults.shape(
                        shape = RoundedCornerShape(8.dp)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    onClick = { viewModel.login() }) {
                    Text(
                        text = "ورود",
                        style = MaterialTheme.typography.bodyLarge.copy(Color.White),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.fillMaxHeight())
            }
        }
    }

    if (loginStatus is DataResult.DataLoading)
        LoadingDialog()

    if (loginStatus is DataResult.DataError)
        ErrorDialog(title = loginStatus?.message ?: "", message = "", onRetryClick = {viewModel.retry()}, onCloseClick = {viewModel.retry()}, showTelegramChannel = false, showSecondBtn = false)
}