package com.bamabin.tv_app.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import com.bamabin.tv_app.BuildConfig
import com.bamabin.tv_app.R
import com.bamabin.tv_app.ui.widgeta.ErrorDialog
import com.bamabin.tv_app.ui.widgeta.LoadingWidget
import com.bamabin.tv_app.ui.widgeta.UpdateDialog
import com.bamabin.tv_app.utils.DataResult
import com.bamabin.tv_app.utils.Routes

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val result by viewModel.result.collectAsState()

    var showUpdateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(result) {
        if (result is DataResult.DataSuccess) {
            if (!result.data!!.needUpdate) {
                navHostController.navigate(
                    Routes.START.name,
                    NavOptions.Builder().setPopUpTo(Routes.SPLASH.name, true).build()
                )
            } else {
                showUpdateDialog = true
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.logo_dark),
            contentDescription = "",
            modifier = Modifier.align(Alignment.Center)
        )

        Box(modifier = Modifier
            .size(164.dp)
            .align(Alignment.BottomCenter)
            .padding(bottom = 48.dp)) {

            if (result is DataResult.DataLoading) {
                LoadingWidget(showText = false)
            } else if (result is DataResult.DataError) {
                ErrorDialog(message = result.message) {
                    viewModel.fetchData()
                }
            }
        }

        Text(
            text = BuildConfig.VERSION_NAME,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
        )
    }

    if (showUpdateDialog) {
        UpdateDialog(result.data!!)
    }
}