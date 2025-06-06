package io.fresco.zenzapassignment.data

import android.util.Log
import io.fresco.zenzapassignment.data.searchcache.SearchCacheService
import io.fresco.zenzapassignment.data.searchcache.StockDataCacheService
import io.fresco.zenzapassignment.data.source.StockInfoSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val stockSource: StockInfoSource,
    private val searchCacheService: SearchCacheService,
    private val stockDataCacheService: StockDataCacheService,
) : StockRepository {

    companion object {
        private const val TAG = "StockRepositoryImpl"
    }

    override val quotesData: StateFlow<List<GlobalQuoteData>>
        get() = stockDataCacheService.getAllQuotesFlow()

    private val _errorMessages = MutableStateFlow<String?>(null)
    override val errorMessages: StateFlow<String?> = _errorMessages.asStateFlow()

    private val _inProgress = MutableStateFlow(false)
    override val inProgress: StateFlow<Boolean> = _inProgress.asStateFlow()

    override suspend fun searchStocks(keyword: String): Result<SearchResponseData> {
        return searchCacheService.getCacheForSearch(keyword)?.let { cachedResponse ->
            Log.d(TAG, "Returning cached response for keyword: $keyword")
            Result.success(cachedResponse)
        } ?: run {
            Log.d(TAG, "No cache found for keyword: $keyword, fetching from source")
            val result = stockSource.searchStocks(keyword)
            if (result.isSuccess) {
                result.getOrNull()?.let { response ->
                    searchCacheService.insertSearchCacheEntry(keyword, response)
                }
            }
            result
        }
    }

    override suspend fun addSelectedQuote(symbols: String, name: String): Boolean {
        _inProgress.value = true
        val result = stockSource.getQuote(symbols, name)
        return try {
            if (result.isSuccess) {
                result.getOrNull()?.let { quote ->
                    Log.d(TAG, "Adding quote for symbols: $symbols")
                    withContext(Dispatchers.IO) {
                        stockDataCacheService.insertQuote(quote)
                    }
                    true
                } ?: run {
                    Log.e(TAG, "Received null GlobalQuoteData for symbols: $symbols")
                    false
                }
            } else {
                Log.e(TAG, "Error fetching quote for symbols: $symbols", result.exceptionOrNull())
                false
            }
        } finally {
            _inProgress.value = false
        }
    }

    override suspend fun updateAllQuotes(): Boolean {
        return try {
            coroutineScope {
                _inProgress.value = true
                Log.d(TAG, "Updating all quotes in the repository. Count: ${stockDataCacheService.getAllQuotes().size}")
                val updateJobs = stockDataCacheService.getAllQuotes().map { quote ->
                    Log.d(TAG, "Scheduling update for quote: ${quote.quote.symbol}")
                    async<Boolean> { updateDataForSymbol(quote.quote.symbol, quote.name) }
                }
                val results: List<Boolean> = updateJobs.awaitAll()
                val success = results.all { it }
                Log.d(TAG, "All quotes updated successfully: $success")
                if (!success) {
                    onFailedUpdateQuotes()
                }
                success
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error updating all quotes: ${ex.message}", ex)
            onFailedUpdateQuotes()
            false
        } finally {
            _inProgress.value = false
        }
    }

    private suspend fun onFailedUpdateQuotes() {
        withContext(Dispatchers.Main) {
            _errorMessages.value = "Error updating quotes. Please try again later."
        }
    }

    private suspend fun updateDataForSymbol(symbol: String, name: String): Boolean {
        Log.d(TAG, "Updating quote for symbol: $symbol")
        val result = stockSource.getQuote(symbol, name)
        if (result.isSuccess) {
            result.getOrNull()?.let { quote ->
                Log.d(TAG, "Updating quote for symbol: $symbol")
                stockDataCacheService.insertQuote(quote)
                return true
            } ?: Log.e(TAG, "Received null GlobalQuoteData for symbol: $symbol")
        } else {
            Log.e(TAG, "Error fetching quote for symbol: $symbol", result.exceptionOrNull())
        }
        return false
    }
}
