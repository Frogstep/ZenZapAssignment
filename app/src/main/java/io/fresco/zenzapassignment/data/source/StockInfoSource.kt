package io.fresco.zenzapassignment.data.source

import io.fresco.zenzapassignment.data.GlobalQuoteData
import io.fresco.zenzapassignment.data.SearchResponseData

interface StockInfoSource {
    suspend fun searchStocks(keyword: String): Result<SearchResponseData>
    suspend fun getQuote(symbols: String, name: String): Result<GlobalQuoteData>
}
