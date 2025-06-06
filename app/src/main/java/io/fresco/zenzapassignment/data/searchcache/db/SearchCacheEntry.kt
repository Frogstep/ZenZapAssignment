package io.fresco.zenzapassignment.data.searchcache.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchCacheEntry(
    @PrimaryKey val symbol: String,
    @ColumnInfo(name = "response") val response: String,
    @ColumnInfo(name = "expire") val expire: Long,
)
