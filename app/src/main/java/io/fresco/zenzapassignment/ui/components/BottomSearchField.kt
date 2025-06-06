package io.fresco.zenzapassignment.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.fresco.zenzapassignment.ui.screens.mainscreen.MainViewModel

@Composable
fun BottomSearchField(viewModel: MainViewModel){
    Column(modifier = Modifier.fillMaxWidth()){
        val listState = rememberLazyListState()
        val suggestions by viewModel.suggestedStocks.collectAsState()

        LazyColumn(state = listState,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(items = suggestions.matches, key = { suggestion -> suggestion.symbol }) { suggestion ->
                val line = "${suggestion.symbol} - ${suggestion.name}"
                Text(text = line, modifier = Modifier.fillMaxWidth())
            }
        }

        var searchQuery by remember { mutableStateOf("") }
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            label = { Text(text = "Search") }
        )

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomSearchField(){
    BottomSearchField(hiltViewModel())
}
