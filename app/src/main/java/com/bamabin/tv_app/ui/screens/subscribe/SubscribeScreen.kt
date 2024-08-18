package com.bamabin.tv_app.ui.screens.subscribe

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import com.bamabin.tv_app.ui.widgeta.ErrorDialog
import com.bamabin.tv_app.ui.widgeta.LoadingDialog
import com.bamabin.tv_app.ui.widgeta.LoadingWidget
import com.bamabin.tv_app.utils.DataResult

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SubscribeScreen(
    navHostController: NavHostController,
    viewModel: SubscribeViewModel = hiltViewModel()
) {
    val result by viewModel.result.collectAsState()
    val urlResult by viewModel.urlResult.collectAsState()

    LaunchedEffect(result) {
        if (result is DataResult.DataSuccess)
            navHostController.popBackStack()
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "خرید اشتراک",
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
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (urlResult is DataResult.DataLoading){
            LoadingWidget()
        } else if (urlResult is DataResult.DataSuccess) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    bitmap = viewModel.getQRCode(), contentDescription = "", modifier = Modifier.clip(
                        RoundedCornerShape(12.dp)
                    )
                )

                Spacer(modifier = Modifier.width(32.dp))

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "برای خرید اشتراک تصویر را اسکن کنید یا لینک زیر را در مرورگر خود باز کنید که وارد صفحه خرید اشتراک شوید یا از طریق اپلیکیشن موبایل یا سایت بامابین وارد اکانت خود شده و سپس اقدام به خرید اشتراک کنید. پس از خرید اشتراک منتظر بمانید تا مدت اشتراک شما به روز شود",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = urlResult.data?:"",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = TextUnit(22f, TextUnitType.Sp)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }

    if (result is DataResult.DataError)
        ErrorDialog(message = result!!.message) {
            viewModel.retry()
        }

    if (urlResult is DataResult.DataError)
        ErrorDialog(message = urlResult.message) {
            viewModel.getUrl()
        }

    if (result is DataResult.DataSuccess)
        LoadingDialog()
}