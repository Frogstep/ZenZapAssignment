package io.fresco.zenzapassignment.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.fresco.zenzapassignment.data.StockRepository
import io.fresco.zenzapassignment.data.StockRepositoryImpl
import io.fresco.zenzapassignment.data.net.ApiService
import io.fresco.zenzapassignment.data.searchcache.SearchCacheService
import io.fresco.zenzapassignment.data.searchcache.StockDataCacheService
import io.fresco.zenzapassignment.data.searchcache.db.SearchCacheDatabase
import io.fresco.zenzapassignment.data.source.StockInfoSource
import io.fresco.zenzapassignment.data.source.StockInfoSourceImpl
import io.fresco.zenzapassignment.data.update.StockDataUpdateService
import io.fresco.zenzapassignment.data.update.StockDataUpdateServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideAlphaVantageApi(): ApiService = ApiService.create()

    @Singleton
    @Provides
    fun provideStockInfoSource(api: ApiService): StockInfoSource {
        return StockInfoSourceImpl(api)
    }

    @Singleton
    @Provides
    fun provideStockRepository(
        source: StockInfoSource,
        searchCacheService: SearchCacheService,
        stockDataCacheService: StockDataCacheService,
    ): StockRepository {
        return StockRepositoryImpl(source, searchCacheService, stockDataCacheService)
    }

    @Singleton
    @Provides
    fun provideSearchCacheDatabase(@ApplicationContext context: Context): SearchCacheDatabase {
        return SearchCacheDatabase.createDatabase(context)
    }

    @Singleton
    @Provides
    fun provideUpdateService(
        stockRepository: StockRepository,
    ): StockDataUpdateService {
        return StockDataUpdateServiceImpl(stockRepository)
    }

    @Singleton
    @Provides
    fun provideSearchCacheService(
        searchCacheDatabase: SearchCacheDatabase,
    ): SearchCacheService {
        return SearchCacheService(searchCacheDatabase)
    }

    @Singleton
    @Provides
    fun provideStockDataCacheService(
        searchCacheDatabase: SearchCacheDatabase,
    ): StockDataCacheService {
        return StockDataCacheService(searchCacheDatabase)
    }
}
