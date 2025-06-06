package io.fresco.zenzapassignment.data

import kotlinx.coroutines.flow.StateFlow

interface StockRepository {

    suspend fun searchStocks(keyword: String): Result<SearchResponseData>

    suspend fun addSelectedQuote(symbols: String, name: String): Boolean
    suspend fun updateAllQuotes(): Boolean

    val quotesData: StateFlow<List<GlobalQuoteData>>
    val errorMessages: StateFlow<String?>
    val inProgress: StateFlow<Boolean>
}
