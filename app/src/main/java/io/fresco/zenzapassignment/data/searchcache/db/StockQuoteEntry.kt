package io.fresco.zenzapassignment.data.searchcache.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StockQuoteEntry(
    @PrimaryKey val symbol: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "quote") val quote: String,
)
