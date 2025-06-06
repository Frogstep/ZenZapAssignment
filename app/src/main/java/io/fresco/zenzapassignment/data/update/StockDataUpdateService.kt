package io.fresco.zenzapassignment.data.update

import kotlinx.coroutines.CoroutineScope

interface StockDataUpdateService {
    suspend fun startUpdateService(scope: CoroutineScope)
    suspend fun pauseUpdateService()
}
