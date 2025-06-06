package io.fresco.zenzapassignment.data

import io.fresco.zenzapassignment.data.net.schemas.GlobalQuoteResponse

data class GlobalQuoteData(val quote: Quote) {
    companion object {
        fun createFromGlobalQuoteResponse(response: GlobalQuoteResponse): GlobalQuoteData {
            return GlobalQuoteData(
                Quote(
                    symbol = response.globalQuote.symbol,
                    open = response.globalQuote.open,
                    high = response.globalQuote.high,
                    low = response.globalQuote.low,
                    price = response.globalQuote.price,
                    volume = response.globalQuote.price,
                    latestTradingDay = response.globalQuote.latestTradingDay,
                    previousClose = response.globalQuote.previousClose,
                    change = response.globalQuote.change,
                    changePercent = response.globalQuote.changePercent
                )
            )
        }
    }
}

data class Quote(
    val symbol: String,
    val open: String,
    val high: String,
    val low: String,
    val price: String,
    val volume: String,
    val latestTradingDay: String,
    val previousClose: String,
    val change: String,
    val changePercent: String
)