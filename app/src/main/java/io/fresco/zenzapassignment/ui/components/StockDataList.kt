package io.fresco.zenzapassignment.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.fresco.zenzapassignment.ui.screens.mainscreen.MainViewModel
import io.fresco.zenzapassignment.ui.theme.DeepGreen
import io.fresco.zenzapassignment.ui.utils.TextFormatter

@Composable
fun StockDataList(viewModel: MainViewModel) {
    val listState = rememberLazyListState()
    val stocks by viewModel.selectedStocks.collectAsState()

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp),
    ) {
        items(items = stocks, key = { it.quote.symbol }) {
            StockDataEntryContent(
                symbol = it.quote.symbol,
                name = it.name,
                price = it.quote.price,
                change = it.quote.change,
                changePercents = it.quote.changePercent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, top = 1.dp, end = 5.dp, bottom = 1.dp),
            )
        }
    }
}

@Composable
fun StockDataEntryContent(
    symbol: String,
    name: String,
    price: String,
    change: String,
    changePercents: String,
    modifier: Modifier = Modifier,
) {
    val formattedChange = TextFormatter.getFormattedChange(change, changePercents)
    val color = if (formattedChange.positive) DeepGreen else Color.Red
    val formattedPrice = TextFormatter.formatPrice(price)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 3.dp),
    ) {
        Text(
            text = symbol.uppercase(),
            color = Color.Black,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(0.15f),

        )
        Text(
            text = name,
            color = Color.Black,
            maxLines = 1,
            fontSize = 13.sp,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(0.25f),
        )
        Text(
            text = formattedPrice,
            color = Color.Black,
            maxLines = 1,
            fontSize = 12.sp,
            textAlign = TextAlign.End,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(0.10f),
        )
        Text(
            text = formattedChange.text,
            color = color,
            fontSize = 12.sp,
            maxLines = 1,
            textAlign = TextAlign.End,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(0.17f),
        )
    }
}
