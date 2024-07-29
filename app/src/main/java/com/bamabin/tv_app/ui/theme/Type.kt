package com.bamabin.tv_app.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Typography
import com.bamabin.tv_app.R

// Set of Material typography styles to start with
@OptIn(ExperimentalTvMaterial3Api::class)
val Typography = Typography(
    displayLarge = Typography().displayLarge.copy(fontFamily = AppFont.yekanBakh),
    displayMedium = Typography().displayMedium.copy(fontFamily = AppFont.yekanBakh),
    displaySmall = Typography().displaySmall.copy(fontFamily = AppFont.yekanBakh),

    headlineLarge = Typography().headlineLarge.copy(fontFamily = AppFont.yekanBakh),
    headlineMedium = Typography().headlineMedium.copy(fontFamily = AppFont.yekanBakh),
    headlineSmall = Typography().headlineSmall.copy(fontFamily = AppFont.yekanBakh),

    titleLarge = Typography().titleLarge.copy(fontFamily = AppFont.yekanBakh),
    titleMedium = Typography().titleMedium.copy(fontFamily = AppFont.yekanBakh),
    titleSmall = Typography().titleSmall.copy(fontFamily = AppFont.yekanBakh),

    bodyLarge = Typography().bodyLarge.copy(fontFamily = AppFont.yekanBakh),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = AppFont.yekanBakh),
    bodySmall = Typography().bodySmall.copy(fontFamily = AppFont.yekanBakh),

    labelLarge = Typography().labelLarge.copy(fontFamily = AppFont.yekanBakh),
    labelMedium = Typography().labelMedium.copy(fontFamily = AppFont.yekanBakh),
    labelSmall = Typography().labelSmall.copy(fontFamily = AppFont.yekanBakh),

//    bodyLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

object AppFont {
    val yekanBakh = FontFamily(
        Font(R.font.yekanbakh_regular),
        Font(R.font.yekanbakh_thin, FontWeight.Thin),
        Font(R.font.yekanbakh_light, FontWeight.Light),
        Font(R.font.yekanbakh_semibold, FontWeight.SemiBold),
        Font(R.font.yekanbakh_bold, FontWeight.Bold),
    )
}