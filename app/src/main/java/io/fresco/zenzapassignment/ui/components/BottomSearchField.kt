package io.fresco.zenzapassignment.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.fresco.zenzapassignment.R
import io.fresco.zenzapassignment.ui.screens.mainscreen.MainViewModel
import io.fresco.zenzapassignment.ui.theme.LightGray
import kotlinx.coroutines.launch

@Composable
fun BottomSearchField(viewModel: MainViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchQuery by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(LightGray)
            .padding(16.dp),
    ) {
        val listState = rememberLazyListState()
        val suggestions by viewModel.suggestedStocks.collectAsState()

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            items(
                items = suggestions.matches,
                key = { suggestion -> suggestion.symbol },
            ) { suggestion ->
                SearchEntryContent(
                    symbol = suggestion.symbol,
                    name = suggestion.name,
                    onClick = {
                        coroutineScope.launch {
                            keyboardController?.hide()
                            viewModel.onStockSelected(suggestion.symbol, suggestion.name)
                            searchQuery = ""
                        }
                    },
                )
            }
        }

        val errorText by viewModel.suggestionsError.collectAsState()
        errorText?.let {
            Text(
                text = it,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    coroutineScope.launch {
                        viewModel.onSearchQueryChanged(searchQuery)
                    }
                },
                textStyle = TextStyle(color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(id = R.string.search_hint),
                        color = Color.Black,
                    )
                },
            )

            val isLoading by viewModel.suggestionsLoading.collectAsState()
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp, top = 6.dp)
                        .size(24.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomSearchField() {
    BottomSearchField(hiltViewModel())
}
