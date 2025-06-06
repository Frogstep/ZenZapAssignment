package io.fresco.zenzapassignment.data.source

import io.fresco.zenzapassignment.data.GlobalQuoteData
import io.fresco.zenzapassignment.data.SearchResponseData
import io.fresco.zenzapassignment.data.net.ApiService
import io.fresco.zenzapassignment.data.net.schemas.GlobalQuoteResponse
import javax.inject.Inject

class StockInfoSourceImpl @Inject constructor(private val api: ApiService) : StockInfoSource {

    override suspend fun searchStocks(keyword: String): Result<SearchResponseData> {
        return try {
            val response = api.query(searchString = keyword)
            Result.success(
                SearchResponseData.Companion.createFromSearchResponse(response)
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }

    override suspend fun getQuote(symbols: String): Result<GlobalQuoteData> {
        return try {
            val response = api.quote(symbol = symbols)
            Result.success(
                GlobalQuoteData.createFromGlobalQuoteResponse(response)
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }
}