package io.fresco.zenzapassignment.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.fresco.zenzapassignment.ui.screens.mainscreen.MainViewModel

@Composable
fun MainScreenComponent(modifier: Modifier){
    val viewModel: MainViewModel = hiltViewModel()
    MainScreenComponentContent(viewModel, modifier)
}

@Composable
fun MainScreenComponentContent(viewModel: MainViewModel, modifier: Modifier){
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 150.dp)) {
            val itemsList = (1..20).toList()
            items(itemsList) { item ->
                Text(text = "Item \$item")
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Text(text = "Bottom Fixed Component")
        }
    }
}