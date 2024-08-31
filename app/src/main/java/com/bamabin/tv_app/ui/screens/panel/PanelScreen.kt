package com.bamabin.tv_app.ui.screens.panel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
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
import androidx.tv.material3.MaterialTheme
import coil.compose.AsyncImage
import com.bamabin.tv_app.data.local.TempDB
import com.bamabin.tv_app.data.remote.model.user.UserData
import com.bamabin.tv_app.ui.theme.Failed
import com.bamabin.tv_app.ui.theme.Success
import com.bamabin.tv_app.ui.widgeta.ErrorDialog
import com.bamabin.tv_app.ui.widgeta.LoadingDialog
import com.bamabin.tv_app.ui.widgeta.LoadingWidget
import com.bamabin.tv_app.utils.Routes
import okhttp3.internal.http2.Settings

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun PanelScreen(
    navHostController: NavHostController,
    viewModel: PanelViewModel = hiltViewModel()
) {
    var focusedItem by remember { mutableIntStateOf(-1) }
    val selectedItem by viewModel.selectedMenu.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val showLogoutAlert by viewModel.showLogoutAlert.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val mainModifier = Modifier.padding(
        start = 16.dp
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        viewModel.getRequests()
    }

    Row(
        modifier = Modifier
            .padding(all = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 24.dp)
                .padding(top = 40.dp)
        ) {
            for (i in 0 until viewModel.menuItems.size) {
                var modifier = Modifier
                    .onFocusChanged {
                        focusedItem = if (it.isFocused) i else -1
                    }
                    .padding(bottom = 16.dp)
                if (i == 0)
                    modifier = modifier.focusRequester(focusRequester)

                Card(
                    colors = CardDefaults.colors(
                        containerColor = Color.Transparent,
                        focusedContainerColor = Color.White
                    ),
                    border = CardDefaults.border(
                        focusedBorder = Border.None
                    ),
                    modifier = modifier,
                    onClick = { viewModel.selectMenu(i) }) {
                    Row(
                        modifier = Modifier.padding(all = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = viewModel.menuItems[viewModel.menuItems.keys.toList()[i]]!!,
                            contentDescription = "",
                            tint = if (focusedItem == i) Color.Black else Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = viewModel.menuItems.keys.toList()[i],
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (focusedItem == i) Color.Black else Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }

        when (selectedItem) {
            1 -> Requests(mainModifier, navHostController)
            2 -> Transactions(mainModifier)
            3 -> InviteForm(mainModifier)
            4 -> Setting(mainModifier)
            5 -> Support(modifier = mainModifier, qrCode = viewModel.generateQRCode())
            else -> UserAccount(modifier = mainModifier, userData = viewModel.userData)
        }
    }

    if (isLoading)
        LoadingDialog()

    if (showLogoutAlert){
        AlertDialog(
            containerColor = Color(0xFF2B2B2B),
            onDismissRequest = {},
            confirmButton = {},
            text = {
                Column {
                    Text(
                        text = "آیا می‌خواهید از حساب کاربری خود خارج شوید؟",
                        style = MaterialTheme.typography.titleLarge.copy(
                            Color.White,
                            fontWeight = FontWeight.W700
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.colors(
                            containerColor = Color.White,
                            focusedContainerColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = { viewModel.hideLogoutAlert() }) {
                        Text(
                            text = "خیر",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.colors(
                            containerColor = Color.White,
                            focusedContainerColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = { viewModel.logout() }) {
                        Text(
                            text = "بله",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        )
    }

    if (errorMessage.isNotEmpty()){
        ErrorDialog(message = errorMessage, onRetryClick = {viewModel.hideErrorDialog()}, onCloseClick = {viewModel.hideErrorDialog()})
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun UserAccount(modifier: Modifier, userData: UserData) {
    Column(modifier = modifier) {
        Text(
            text = "حساب کاربری شما",
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = TextUnit(35f, TextUnitType.Sp)
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row {
            AsyncImage(
                model = userData.avatar,
                contentDescription = "",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(72.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = userData.username,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = userData.email,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = buildAnnotatedString {
                        append("وضعیت اشتراک: ")
                        withStyle(
                            SpanStyle(
                                color = if (TempDB.vipInfo!!.isVip) Success else Failed
                            )
                        ) {
                            append(
                                if (TempDB.vipInfo!!.isVip) "فعال" else "غیرفعال"
                            )
                        }
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildAnnotatedString {
                        append("روزهای باقیمانده: ")
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            append("${TempDB.vipInfo!!.days} روز")
                        }
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun Transactions(
    modifier: Modifier,
    viewModel: PanelViewModel = hiltViewModel()
) {
    val transactions = viewModel.transactions

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "تراکنش های من",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = TextUnit(35f, TextUnitType.Sp)
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        TvLazyColumn {
            item {
                Row(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(24.dp)
                        )
                        .padding(vertical = 8.dp)
                ) {
                    TableCell(text = "شناسه", weight = 1.3f)
                    TableCell(text = "تاریخ", weight = 2f)
                    TableCell(text = "مدت اشتراک", weight = 1.6f)
                    TableCell(text = "مبلغ", weight = 1.6f)
                    TableCell(text = "شناسه پرداخت", weight = 1.8f)
                    TableCell(text = "وضعیت", weight = 1.2f)
                }
            }

            items(transactions.size) {
                Card(
                    colors = CardDefaults.colors(
                        containerColor = Color.Transparent,
                        focusedContainerColor = Color.Gray.copy(alpha = 0.2f)
                    ),
                    scale = CardDefaults.scale(focusedScale = 1f),
                    border = CardDefaults.border(
                        border = Border.None,
                        focusedBorder = Border.None
                    ),
                    onClick = { /*TODO*/ }) {
                    Row {
                        TableCell(
                            text = "#${transactions[it].id}",
                            weight = 1.3f,
                            textColor = Color.White
                        )
                        TableCell(
                            text = transactions[it].getPersianDate(),
                            weight = 2f,
                            textColor = Color.White
                        )
                        TableCell(
                            text = transactions[it].getMonth(),
                            weight = 1.6f,
                            textColor = Color.White
                        )
                        TableCell(
                            text = transactions[it].getPriceText(),
                            weight = 1.6f,
                            textColor = Color.White
                        )
                        TableCell(
                            text = transactions[it].authority ?: "",
                            weight = 1.8f,
                            textColor = Color.White
                        )
                        TableCell(
                            text = transactions[it].getStatusLabel(),
                            weight = 1.2f,
                            textColor = transactions[it].getStatusColor()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun Requests(
    modifier: Modifier,
    navHostController: NavHostController,
    viewModel: PanelViewModel = hiltViewModel()
) {
    val requests = viewModel.requests
    var isButtonFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "درخواست‌ها",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = TextUnit(35f, TextUnitType.Sp)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

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
            modifier = Modifier.align(Alignment.End).onFocusChanged { isButtonFocused = it.isFocused },
            onClick = { navHostController.navigate(Routes.REQUEST_FORM.name) }) {
            Text(text = "ثبت درخواست جدید", style = MaterialTheme.typography.bodyMedium.copy(
                color = if (isButtonFocused) Color.White else Color.Black
            ))
        }

        TvLazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(requests.size) {
                Card(
                    colors = CardDefaults.colors(
                        containerColor = Color.Gray.copy(alpha = 0.2f),
                        focusedContentColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    onClick = {
                        if (requests[it].isSubmitted()) {
                            navHostController.navigate("${Routes.POST_DETAILS.name}/${requests[it].postId}")
                        }
                    }) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "شناسه: ${requests[it].id}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "درخواست: ${requests[it].title}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "وضعیت: ${requests[it].getStatusLabel()}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = requests[it].getStatusColor())
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "تاریخ: ${requests[it].getPersianDate()}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "پیام: ${requests[it].message}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (requests[it].isSubmitted()) {
                            Text(
                                text = "برای مشاهده کلید تائید را بزنید",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun Setting(
    modifier: Modifier,
    viewModel: PanelViewModel = hiltViewModel()
) {
    val background by viewModel.currentBgColor.collectAsState()
    val color by viewModel.currentTextColor.collectAsState()
    val font by viewModel.currentFont.collectAsState()
    val size by viewModel.currentSize.collectAsState()

    Column(
        modifier = modifier.padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "تنظیمات",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = TextUnit(35f, TextUnitType.Sp)
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "پس‌زمینه‌ی زیرنویس",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Row {
                for (i in viewModel.backgroundColors.indices) {
                    Button(
                        colors = ButtonDefaults.colors(
                            containerColor = if (background == i) MaterialTheme.colorScheme.primary else Color.Black.copy(
                                alpha = 0.2f
                            ),
                            focusedContainerColor = if (background == i) MaterialTheme.colorScheme.primary else Color.Black.copy(
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
                        onClick = { viewModel.setBgColor(i) }) {
                        Text(
                            viewModel.backgroundColors[i],
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "رنگ زیرنویس",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Row {
                for (i in viewModel.textColors.indices) {
                    Button(
                        colors = ButtonDefaults.colors(
                            containerColor = if (color == i) MaterialTheme.colorScheme.primary else Color.Black.copy(
                                alpha = 0.2f
                            ),
                            focusedContainerColor = if (color == i) MaterialTheme.colorScheme.primary else Color.Black.copy(
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
                        onClick = { viewModel.setColor(i) }) {
                        Text(
                            viewModel.textColors[i],
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "فونت",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Row {
                for (i in viewModel.fonts.indices) {
                    Button(
                        colors = ButtonDefaults.colors(
                            containerColor = if (font == i) MaterialTheme.colorScheme.primary else Color.Black.copy(
                                alpha = 0.2f
                            ),
                            focusedContainerColor = if (font == i) MaterialTheme.colorScheme.primary else Color.Black.copy(
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
                        onClick = { viewModel.setFont(i) }) {
                        Text(
                            viewModel.fonts[i],
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "اندازه‌ی زیرنویس",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Row {
                for (i in viewModel.sizes.indices) {
                    Button(
                        colors = ButtonDefaults.colors(
                            containerColor = if (size == i) MaterialTheme.colorScheme.primary else Color.Black.copy(
                                alpha = 0.2f
                            ),
                            focusedContainerColor = if (size == i) MaterialTheme.colorScheme.primary else Color.Black.copy(
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
                        onClick = { viewModel.setSize(i) }) {
                        Text(
                            viewModel.sizes[i],
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "متن زیرنویس نمونه - تنظیمات را از بالا تغییر دهید",
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = viewModel.getTextColor(),
                fontSize = TextUnit(viewModel.getTextSize(), TextUnitType.Sp),
                fontFamily = FontFamily(viewModel.getFont())
            ),
            modifier = Modifier
                .background(viewModel.getBgColor())
                .padding(all = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun InviteForm(
    modifier: Modifier,
    viewModel: PanelViewModel = hiltViewModel()
) {
    var inviteCode by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    val inviteInfo by viewModel.inviteInfo.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "کد دعوت",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = TextUnit(35f, TextUnitType.Sp)
            )
        )

        inviteInfo?.let {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = buildAnnotatedString {
                    append("کد دعوت شما: ")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)){
                        append(it.inviteCode)
                    }
                },
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!it.isInvited){
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
                        value = inviteCode,
                        onValueChange = { inviteCode = it },
                        label = {
                            Text(
                                text = "کد دعوت",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                ),
                            )
                        },
                        singleLine = true,
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
                            .padding(horizontal = 40.dp)
                            .padding(top = 8.dp)
                            .align(Alignment.CenterHorizontally)
                            .focusRequester(focusRequester)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = buildAnnotatedString {
                    append("تعداد کاربران دعوت شده توسط شما: ")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)){
                        append(it.invitesCount.toString())
                    }
                },
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (!it.isInvited){
                Button(
                    colors = ButtonDefaults.colors(
                        containerColor = Color.White,
                        focusedContainerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 160.dp),
                    onClick = { viewModel.saveInviteCode(inviteCode) }) {
                    Text(
                        text = "بررسی",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun Support(
    modifier: Modifier,
    qrCode: ImageBitmap
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "تنظیمات",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = TextUnit(35f, TextUnitType.Sp)
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "برای ارتباط با پشتیبانی کد زیر را اسکن کنید",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(32.dp))
        
        Image(bitmap = qrCode, contentDescription = "", modifier = Modifier.clip(RoundedCornerShape(12.dp)))
    }
}

@Composable
private fun RowScope.TableCell(
    text: String,
    textColor: Color = Color.Black,
    weight: Float
) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = textColor,
            fontWeight = FontWeight.W600,
            fontSize = TextUnit(14f, TextUnitType.Sp)
        ),
        modifier = Modifier
            .weight(weight)
            .padding(8.dp)
    )
}