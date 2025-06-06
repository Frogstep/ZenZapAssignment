package io.fresco.zenzapassignment.data

import io.fresco.zenzapassignment.data.net.schemas.GlobalQuoteResponse
import io.fresco.zenzapassignment.data.net.schemas.SearchResponse

interface StockRepository {

    suspend fun searchStocks(keyword: String): Result<SearchResponseData>

    suspend fun getQuote(symbols: String): Result<GlobalQuoteData>
}