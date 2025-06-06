package io.fresco.zenzapassignment.data.source

import android.util.Log
import io.fresco.zenzapassignment.data.GlobalQuoteData
import io.fresco.zenzapassignment.data.SearchResponseData
import io.fresco.zenzapassignment.data.net.ApiService
import javax.inject.Inject

class StockInfoSourceImpl @Inject constructor(private val api: ApiService) : StockInfoSource {

    companion object {
        private const val TAG = "StockInfoSourceImpl"
    }

    override suspend fun searchStocks(keyword: String): Result<SearchResponseData> {
        return try {
            val response = api.query(searchString = keyword)
            Result.success(
                SearchResponseData.Companion.createFromSearchResponse(response),
            )
        } catch (ex: Exception) {
            Log.e(TAG, "Error fetching search results for keyword: $keyword")
            Result.failure(ex)
        }
    }

    override suspend fun getQuote(symbols: String, name: String): Result<GlobalQuoteData> {
        return try {
            val response = api.quote(symbol = symbols)
            Result.success(
                GlobalQuoteData.createFromGlobalQuoteResponse(response, name),
            )
        } catch (ex: Exception) {
            Log.e(TAG, "Error fetching quote for symbols: $symbols")
            Result.failure(ex)
        }
    }
}
