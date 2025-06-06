package io.fresco.zenzapassignment.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import io.fresco.zenzapassignment.ui.screens.mainscreen.MainViewModel

@Composable
fun MainScreenComponent(modifier: Modifier) {
    val viewModel: MainViewModel = hiltViewModel()
    MainScreenComponentContent(viewModel, modifier)
}

@Composable
fun MainScreenComponentContent(viewModel: MainViewModel, modifier: Modifier) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> viewModel.onActivityVisible()
                Lifecycle.Event.ON_PAUSE -> viewModel.onActivityNotVisible()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val context = LocalContext.current
    val toastMessage = viewModel.commonErrorMessage.collectAsState()

    LaunchedEffect(toastMessage.value) {
        toastMessage.value?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.onCommonErrorHandled()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
            .background(Color.White),
    ) {
        StockDataList(viewModel = viewModel)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color.Blue),
        ) {
            BottomSearchField(viewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreenComponentContent() {
    MainScreenComponent(modifier = Modifier.fillMaxSize())
}
