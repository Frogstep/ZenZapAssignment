package io.fresco.zenzapassignment.data.update

import android.util.Log
import io.fresco.zenzapassignment.data.StockRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max

class StockDataUpdateServiceImpl @Inject constructor(
    private val stockRepository: StockRepository,
) : StockDataUpdateService {

    companion object {
        private const val UPDATE_INTERVAL = 30 * 1000L // 30 Seconds
        private const val TAG = "StockDataUpdateService"
    }

    private var updateScheduleJob: Job? = null
    private var updateActionJob: Job? = null
    private var isRunning: Boolean = false
    private var lastUpdateTime: Long = 0L

    /**
     * Starts the update service if it is not already running.
     * If it is running, it will reschedule the updates. Minimal update delay is calculated based on the last update time.
     */
    override suspend fun startUpdateService(scope: CoroutineScope) {
        Log.d(TAG, "Request to start update service received.")
        isRunning = true
        val initialDelay = max(0, lastUpdateTime - System.currentTimeMillis() + UPDATE_INTERVAL)
        Log.d(TAG, "Scheduling updates with initial delay: $initialDelay ms")
        updateScheduleJob?.cancel()
        updateScheduleJob = scope.launch {
            delay(initialDelay)
            while (isRunning) {
                lastUpdateTime = System.currentTimeMillis()
                doUpdateAllQuotes(scope)
                delay(UPDATE_INTERVAL)
            }
        }
    }

    private fun doUpdateAllQuotes(scope: CoroutineScope) {
        Log.d(TAG, "doUpdateAllQuotes() called.")
        if (updateActionJob != null) {
            Log.d(TAG, "Update action is already in progress. Ignoring new request.")
            return
        }
        updateActionJob = scope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "Updating stock quotes.")
                if (stockRepository.updateAllQuotes()) {
                    Log.d(TAG, "Stock quotes updated successfully.")
                } else {
                    Log.e(TAG, "Failed to update stock quotes.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating stock quotes: ${e.message}", e)
            } finally {
                updateActionJob = null
            }
        }
    }

    override suspend fun pauseUpdateService() {
        Log.d(TAG, "Request to pause update service received.")
        updateScheduleJob?.cancel()
        isRunning = false
    }
}
