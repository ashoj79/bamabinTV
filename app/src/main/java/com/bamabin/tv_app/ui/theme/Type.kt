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
    displayLarge = Typography().displayLarge.copy(fontFamily = AppFont.dana),
    displayMedium = Typography().displayMedium.copy(fontFamily = AppFont.dana),
    displaySmall = Typography().displaySmall.copy(fontFamily = AppFont.dana),

    headlineLarge = Typography().headlineLarge.copy(fontFamily = AppFont.dana),
    headlineMedium = Typography().headlineMedium.copy(fontFamily = AppFont.dana),
    headlineSmall = Typography().headlineSmall.copy(fontFamily = AppFont.dana),

    titleLarge = Typography().titleLarge.copy(fontFamily = AppFont.dana),
    titleMedium = Typography().titleMedium.copy(fontFamily = AppFont.dana),
    titleSmall = Typography().titleSmall.copy(fontFamily = AppFont.dana),

    bodyLarge = Typography().bodyLarge.copy(fontFamily = AppFont.dana),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = AppFont.dana),
    bodySmall = Typography().bodySmall.copy(fontFamily = AppFont.dana),

    labelLarge = Typography().labelLarge.copy(fontFamily = AppFont.dana),
    labelMedium = Typography().labelMedium.copy(fontFamily = AppFont.dana),
    labelSmall = Typography().labelSmall.copy(fontFamily = AppFont.dana),

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
    val dana = FontFamily(
        Font(R.font.dana_regular),
        Font(R.font.dana_thin, FontWeight.Thin),
        Font(R.font.dana_light, FontWeight.Light),
        Font(R.font.dana_demi_bold, FontWeight.SemiBold),
        Font(R.font.dana_bold, FontWeight.Bold),
    )
}