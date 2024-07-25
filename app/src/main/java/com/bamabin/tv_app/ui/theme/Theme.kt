package com.bamabin.tv_app.ui.theme

import androidx.compose.runtime.Composable
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun BamabinTVTheme(
    isInDarkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = darkColorScheme(
        primary = Blue,
        secondary = PurpleGrey80,
        tertiary = Pink80
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}