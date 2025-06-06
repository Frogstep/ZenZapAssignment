package io.fresco.zenzapassignment.data.searchcache.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SearchCacheEntry::class, StockQuoteEntry::class], version = 1)
abstract class SearchCacheDatabase : RoomDatabase() {
    abstract fun searchCacheDao(): SearchCacheDAO
    abstract fun stockQuotesDAO(): StockQuotesDAO

    companion object {
        const val DATABASE_NAME = "search_cache.db"

        fun createDatabase(context: Context): SearchCacheDatabase {
            return Room.databaseBuilder(
                context,
                SearchCacheDatabase::class.java,
                DATABASE_NAME,
            ).fallbackToDestructiveMigration(true).build()
        }
    }
}
