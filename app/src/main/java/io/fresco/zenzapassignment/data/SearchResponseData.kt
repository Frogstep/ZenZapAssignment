package io.fresco.zenzapassignment.data

import io.fresco.zenzapassignment.data.net.schemas.SearchResponse

data class SearchResponseData(val matches: List<Match>) {
    companion object {
        fun createFromSearchResponse(response: SearchResponse): SearchResponseData {
            return SearchResponseData(response.bestMatches.map {
                Match(
                    symbol = it.symbol,
                    name = it.name,
                    type = it.type,
                    region = it.region,
                    marketOpen = it.marketOpen,
                    marketClose = it.marketClose,
                    timezone = it.timezone,
                    currency = it.currency,
                    matchScore = it.matchScore
                )
            })
        }
    }
}

data class Match(
    val symbol: String,
    val name: String,
    val type: String,
    val region: String,
    val marketOpen: String,
    val marketClose: String,
    val timezone: String,
    val currency: String,
    val matchScore: String
)