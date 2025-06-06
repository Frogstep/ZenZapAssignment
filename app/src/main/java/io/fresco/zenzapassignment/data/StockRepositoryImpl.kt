package io.fresco.zenzapassignment.data

import io.fresco.zenzapassignment.data.source.StockInfoSource
import javax.inject.Inject

class StockRepositoryImpl  @Inject constructor(private val stockSource: StockInfoSource) : StockRepository {

    companion object{
        private const val TAG = "StockRepositoryImpl"
    }

    override suspend fun searchStocks(keyword: String): Result<SearchResponseData> {
            return stockSource.searchStocks(keyword)
    }

    override suspend fun getQuote(symbols: String): Result<GlobalQuoteData> {
        return stockSource.getQuote(symbols)
    }
}