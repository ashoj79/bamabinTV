package com.bamabin.tv_app.ui.widgeta

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import com.bamabin.tv_app.data.remote.model.app.AppVersion
import com.bamabin.tv_app.utils.SITE_BASE_URL

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun UpdateDialog(appVersion: AppVersion) {
    val uriHandler = LocalUriHandler.current

    AlertDialog(
        containerColor = Color(0xFF2B2B2B),
        onDismissRequest = {},
        confirmButton = {},
        text = {
            Column {
                Text(
                    text = "بروزرسانی",
                    style = MaterialTheme.typography.titleLarge.copy(Color.White, fontWeight = FontWeight.W700),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "نسخه جدیدی از برنامه منتشر شده است. برای استفاده از آخرین امکانات لطفا برنامه را بروزرسانی کنید",
                    style = MaterialTheme.typography.bodyMedium.copy(Color.White, fontWeight = FontWeight.W700),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.colors(
                        containerColor = Color.White
                    ),
                    onClick = { uriHandler.openUri(appVersion.directLink) }) {
                    Text(text = "دانلود مستقیم", textAlign = TextAlign.Center, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), modifier = Modifier.fillMaxWidth())
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.colors(
                        containerColor = Color.White
                    ),
                    onClick = { uriHandler.openUri(appVersion.playStoreLink) }) {
                    Text(text = "دانلود پلی استور", textAlign = TextAlign.Center, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), modifier = Modifier.fillMaxWidth())
                }
            }
        }
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ErrorDialog(
    title: String = "مشکلی رخ داد",
    message: String,
    onRetryClick: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    AlertDialog(
        containerColor = Color(0xFF2B2B2B),
        onDismissRequest = {},
        confirmButton = {},
        text = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        Color.White,
                        fontWeight = FontWeight.W700
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
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
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.colors(
                        containerColor = Color.White
                    ),
                    onClick = { onRetryClick.invoke() }) {
                    Text(
                        text = "تلاش مجدد",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = { uriHandler.openUri(SITE_BASE_URL) }) {
                    Text(
                        text = "ارتباط با پشتیبانی",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    )
}