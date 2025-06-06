package io.fresco.zenzapassignment.data.searchcache

import io.fresco.zenzapassignment.data.GlobalQuoteData
import io.fresco.zenzapassignment.data.Quote
import io.fresco.zenzapassignment.data.searchcache.db.SearchCacheDatabase
import io.fresco.zenzapassignment.data.searchcache.db.StockQuoteEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json
import javax.inject.Inject

class StockDataCacheService @Inject constructor(
    private val stockCacheDB: SearchCacheDatabase,
) {

    fun getAllQuotesFlow(): StateFlow<List<GlobalQuoteData>> {
        return stockCacheDB.stockQuotesDAO().getAllQuotesFlow()
            .map { quotes ->
                quotes.map { quoteEntry ->
                    GlobalQuoteData(
                        name = quoteEntry.name,
                        quote = deserializeQuote(quoteEntry.quote),
                    )
                }
            }
            .stateIn(
                scope = CoroutineScope(Dispatchers.IO),
                started = SharingStarted.Lazily,
                initialValue = emptyList(),
            )
    }

    fun getAllQuotes(): List<GlobalQuoteData> {
        return stockCacheDB.stockQuotesDAO().getAllQuotes().map { quoteEntry ->
            GlobalQuoteData(
                name = quoteEntry.name,
                quote = deserializeQuote(quoteEntry.quote),
            )
        }
    }

    fun insertQuote(quote: GlobalQuoteData) {
        val quoteEntry = StockQuoteEntry(
            symbol = quote.quote.symbol,
            name = quote.name,
            quote = serializeQuote(quote.quote),
        )
        stockCacheDB.stockQuotesDAO().insertQuote(quoteEntry)
    }
}

private fun serializeQuote(quote: Quote): String {
    return Json.encodeToString(Quote.serializer(), quote)
}

private fun deserializeQuote(quote: String): Quote {
    return Json.decodeFromString(Quote.serializer(), quote)
}
