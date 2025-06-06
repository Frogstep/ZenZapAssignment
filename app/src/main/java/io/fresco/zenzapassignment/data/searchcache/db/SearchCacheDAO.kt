package io.fresco.zenzapassignment.data.searchcache.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchCacheDAO {

    @Query("SELECT * FROM SearchCacheEntry WHERE symbol == :symbol and expire > :currentTime")
    fun getCacheForSearch(symbol: String, currentTime: Long): SearchCacheEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearchCacheEntry(entry: SearchCacheEntry)

    @Query("DELETE FROM SearchCacheEntry WHERE expire < :currentTime")
    fun deleteExpiredEntries(currentTime: Long)
}
