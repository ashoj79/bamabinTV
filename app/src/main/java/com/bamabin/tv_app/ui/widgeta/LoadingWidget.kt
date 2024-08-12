package com.bamabin.tv_app.ui.widgeta

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LoadingWidget(
    showText:Boolean = true
) {
    val lottieComposition by rememberLottieComposition(spec = LottieCompositionSpec.Asset("app_loading.json"))
    val lottieProcess by animateLottieCompositionAsState(lottieComposition, iterations = 100)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(
                    color = Color.White,
                    shape = CircleShape
                )
                .padding(all = 0.dp)
                .clipToBounds(),
        ) {
            LottieAnimation(
                composition = lottieComposition,
                progress = { lottieProcess },
                modifier = Modifier.scale(1.5f)
            )
        }

        if (showText) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "در حال دریافت اطلاعات",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = TextUnit(20f, TextUnitType.Sp)
                )
            )
        }
    }
}