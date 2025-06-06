package io.fresco.zenzapassignment.data.searchcache

import io.fresco.zenzapassignment.data.SearchResponseData
import io.fresco.zenzapassignment.data.searchcache.db.SearchCacheDatabase
import io.fresco.zenzapassignment.data.searchcache.db.SearchCacheEntry
import jakarta.inject.Inject
import kotlinx.serialization.json.Json

class SearchCacheService @Inject constructor(private val searchCacheDB: SearchCacheDatabase) {

    companion object {
        const val CACHE_EXPIRATION_TIME = 60 * 60 * 1000
    }

    fun getCacheForSearch(symbol: String): SearchResponseData? {
        return searchCacheDB.searchCacheDao().getCacheForSearch(
            symbol,
            System.currentTimeMillis(),
        )?.let { entry ->
            return deserializeResponse(entry.response)
        }
    }

    fun insertSearchCacheEntry(symbol: String, searchResponse: SearchResponseData) {
        val entry = SearchCacheEntry(
            symbol = symbol,
            response = serializeResponse(searchResponse),
            expire = System.currentTimeMillis() + CACHE_EXPIRATION_TIME,
        )
        searchCacheDB.searchCacheDao().insertSearchCacheEntry(entry)
    }

    private fun serializeResponse(response: SearchResponseData): String {
        return Json.encodeToString(SearchResponseData.serializer(), response)
    }

    private fun deserializeResponse(response: String): SearchResponseData {
        return Json.decodeFromString(SearchResponseData.serializer(), response)
    }
}
