package io.fresco.zenzapassignment.data.source

import io.fresco.zenzapassignment.data.GlobalQuoteData
import io.fresco.zenzapassignment.data.SearchResponseData
import io.fresco.zenzapassignment.data.net.schemas.GlobalQuoteResponse

interface StockInfoSource {
    suspend fun searchStocks(keyword: String): Result<SearchResponseData>
    suspend fun getQuote(symbols: String): Result<GlobalQuoteData>
}