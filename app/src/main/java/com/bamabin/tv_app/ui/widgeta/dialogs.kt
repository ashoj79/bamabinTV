package com.bamabin.tv_app.ui.widgeta

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import com.bamabin.tv_app.data.local.TempDB
import com.bamabin.tv_app.data.remote.model.app.AppVersion
import com.bamabin.tv_app.ui.theme.Failed
import kotlin.system.exitProcess

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun UpdateDialog(appVersion: AppVersion) {
    val uriHandler = LocalUriHandler.current
    var focusedButton by remember { mutableIntStateOf(0) }

    AlertDialog(
        shape = RoundedCornerShape(8.dp),
        containerColor = Color(0xFF2B2B2B),
        onDismissRequest = {},
        confirmButton = {},
        text = {
            Column {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "به‌روزرسانی",
                        style = MaterialTheme.typography.titleLarge.copy(Color.White, fontWeight = FontWeight.W700),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    IconButton(
                        colors = ButtonDefaults.colors(
                            containerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        ),
                        border = ButtonDefaults.border(
                            border = Border.None,
                            focusedBorder = Border(
                                border = BorderStroke(1.dp, Color.White)
                            )
                        ),
                        modifier = Modifier.align(Alignment.TopEnd).onFocusChanged { if (it.isFocused) focusedButton = 0 },
                        onClick = { exitProcess(0) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Cancel,
                            contentDescription = "",
                            tint = Failed
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "نسخه‌ی جدیدی از برنامه منتشر شده است. برای استفاده از تمامی امکانات، لطفاً برنامه را به‌‌روزرسانی بفرمایید.",
                    style = MaterialTheme.typography.bodyMedium.copy(Color.White, fontWeight = FontWeight.W700),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    modifier = Modifier.fillMaxWidth().onFocusChanged { if (it.isFocused) focusedButton = 1 },
                    colors = ButtonDefaults.colors(
                        containerColor = Color.White,
                        focusedContainerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = { uriHandler.openUri(appVersion.directLink) }) {
                    Text(text = "دانلود مستقیم", textAlign = TextAlign.Center, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, color = if (focusedButton == 1) Color.White else Color.Black), modifier = Modifier.fillMaxWidth())
                }
                if (appVersion.playStoreLink.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth().onFocusChanged { if (it.isFocused) focusedButton = 2 },
                        colors = ButtonDefaults.colors(
                            containerColor = Color.White,
                            focusedContainerColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = { uriHandler.openUri(appVersion.playStoreLink) }) {
                        Text(text = "دانلود از پلی استور", textAlign = TextAlign.Center, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, color = if (focusedButton == 2) Color.White else Color.Black), modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ErrorDialog(
    title: String = "مشکلی پیش آمده",
    message: String,
    showTelegramChannel: Boolean = true,
    showSecondBtn: Boolean = true,
    firstBtnText: String = "تلاش مجدد",
    secondBtnText: String = "ارتباط با پشتیبانی",
    onCloseClick: () -> Unit,
    onRetryClick: () -> Unit,
    onSecondBtnClicked: (() -> Unit)? = null
) {
    val uriHandler = LocalUriHandler.current
    var focusedButton by remember { mutableIntStateOf(0) }

    AlertDialog(
        shape = RoundedCornerShape(8.dp),
        containerColor = Color(0xFF2B2B2B),
        onDismissRequest = { onCloseClick() },
        confirmButton = {},
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            Color.White,
                            fontWeight = FontWeight.W700
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    IconButton(
                        colors = ButtonDefaults.colors(
                            containerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        ),
                        border = ButtonDefaults.border(
                            border = Border.None,
                            focusedBorder = Border(
                                border = BorderStroke(1.dp, Color.White)
                            )
                        ),
                        modifier = Modifier.align(Alignment.TopEnd).onFocusChanged { if (it.isFocused) focusedButton = 0 },
                        onClick = onCloseClick
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Cancel,
                            contentDescription = "",
                            tint = Failed
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        Color.White,
                        fontWeight = FontWeight.W700
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    modifier = Modifier.fillMaxWidth().onFocusChanged { if (it.isFocused) focusedButton = 1 },
                    colors = ButtonDefaults.colors(
                        containerColor = Color.White,
                        focusedContainerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = { onRetryClick.invoke() }) {
                    Text(
                        text = firstBtnText,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, color = if (focusedButton == 1) Color.White else Color.Black),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (showSecondBtn) {
                    Button(
                        modifier = Modifier.fillMaxWidth().onFocusChanged { if (it.isFocused) focusedButton = 2 },
                        colors = ButtonDefaults.colors(
                            containerColor = Color.White,
                            focusedContainerColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
                            if (onSecondBtnClicked != null){
                                onSecondBtnClicked.invoke()
                            } else {
                                uriHandler.openUri(TempDB.supportLink)
                            }
                        }) {
                        Text(
                            text = secondBtnText,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = if (focusedButton == 2) Color.White else Color.Black
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                if (showTelegramChannel) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "در صورت بروز مشکل با پشتیبانی تلگرام در ارتباط باشید",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Text(
                            text = "@Bamabin_Support",
                            style = MaterialTheme.typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun LoadingDialog() {
    AlertDialog(
        containerColor = Color(0xFF2B2B2B),
        onDismissRequest = {},
        confirmButton = {},
        modifier = Modifier.size(300.dp),
        text = {
            LoadingWidget()
        }
    )
}