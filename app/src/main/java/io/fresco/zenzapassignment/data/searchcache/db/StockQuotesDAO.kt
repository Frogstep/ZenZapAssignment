package io.fresco.zenzapassignment.data.searchcache.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StockQuotesDAO {

    @Query("SELECT * FROM StockQuoteEntry")
    fun getAllQuotesFlow(): Flow<List<StockQuoteEntry>>

    @Query("SELECT * FROM StockQuoteEntry")
    fun getAllQuotes(): List<StockQuoteEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuote(stockQuoteEntry: StockQuoteEntry)
}
