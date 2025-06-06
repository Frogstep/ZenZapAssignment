package io.fresco.zenzapassignment.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.fresco.zenzapassignment.data.StockRepository
import io.fresco.zenzapassignment.data.StockRepositoryImpl
import io.fresco.zenzapassignment.data.net.ApiService
import io.fresco.zenzapassignment.data.source.StockInfoSource
import io.fresco.zenzapassignment.data.source.StockInfoSourceImpl

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideAlphaVantageApi(): ApiService = ApiService.create()

    @Provides
    fun provideStockInfoSource(api: ApiService): StockInfoSource {
        return StockInfoSourceImpl(api)
    }

    @Provides
    fun provideStockRepository(source: StockInfoSource): StockRepository {
        return StockRepositoryImpl(source)
    }

}