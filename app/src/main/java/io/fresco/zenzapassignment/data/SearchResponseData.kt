package io.fresco.zenzapassignment.data

import io.fresco.zenzapassignment.data.net.schemas.SearchResponse
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseData(val matches: List<Match>) {
    companion object {
        fun createFromSearchResponse(response: SearchResponse): SearchResponseData {
            return SearchResponseData(
                response.bestMatches.map {
                    Match(
                        symbol = it.symbol,
                        name = it.name,
                    )
                },
            )
        }
    }
}

@Serializable
data class Match(
    val symbol: String,
    val name: String,
)
