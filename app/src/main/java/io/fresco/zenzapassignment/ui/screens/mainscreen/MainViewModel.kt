package io.fresco.zenzapassignment.ui.screens.mainscreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.fresco.zenzapassignment.R
import io.fresco.zenzapassignment.data.SearchResponseData
import io.fresco.zenzapassignment.data.StockRepository
import io.fresco.zenzapassignment.data.update.StockDataUpdateService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application,
    private val stockRepository: StockRepository,
    private val stockDataUpdateService: StockDataUpdateService,
) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
        private const val SEARCH_PREFIX = "$"
        private const val MAX_SUGGESTIONS = 5
        private const val SEARCH_DELAY = 800L // Delay in millis before sending search.
        // We need to wait to the user to stop typing
    }

    val selectedStocks = stockRepository.quotesData.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList(),
    )

    val inProgress = stockRepository.inProgress.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false,
    )

    private val _suggestedStocks = MutableStateFlow<SearchResponseData>(SearchResponseData(emptyList()))
    val suggestedStocks = _suggestedStocks.asStateFlow()

    private val _suggestionsLoading = MutableStateFlow(false)
    val suggestionsLoading = _suggestionsLoading.asStateFlow()

    private val _suggestionsError = MutableStateFlow<String?>(null)
    val suggestionsError = _suggestionsError.asStateFlow()

    private val _commonErrorMessage = MutableStateFlow<String?>(null)
    val commonErrorMessage = _commonErrorMessage.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            stockRepository.errorMessages.collect { error ->
                error?.let {
                    Log.e(TAG, "Error from repository: $it")
                    _commonErrorMessage.value = it
                }
            }
        }
    }

    /**
     * Handles changes in the search query. We need to wait fo the user to stop typing first
     */
    fun onSearchQueryChanged(keywords: String) {
        val normalizedKeywords = keywords.trim().lowercase()
        Log.d(TAG, "onSearchQueryChanged()-> Keywords: [$normalizedKeywords]")
        _suggestionsError.value = null
        if (!normalizedKeywords.startsWith(SEARCH_PREFIX) ||
            normalizedKeywords.length == SEARCH_PREFIX.length
        ) {
            Log.d(TAG, "The keywords do not start with the search prefix '$SEARCH_PREFIX' or empty. Ignoring.")
            return
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY)
            doSearch(normalizedKeywords.substring(SEARCH_PREFIX.length))
        }
    }

    /**
     * Performs the search operation using the normalized keywords.
     * It fetches stock suggestions from the repository and updates the UI state accordingly.
     */
    private fun doSearch(normalizedKeywords: String) {
        _suggestionsLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "onSearchQueryChanged()-> Query: [$normalizedKeywords]")
                val result = stockRepository.searchStocks(normalizedKeywords)
                if (result.isSuccess) {
                    val response = result.getOrNull() ?: SearchResponseData(emptyList())
                    Log.d(TAG, "onSearchQueryChanged()-> Response: $response")
                    handleResults(response)
                } else {
                    Log.e(TAG, "Error fetching search suggestions: ${result.exceptionOrNull()?.message}")
                    _suggestionsError.value = application.getString(R.string.network_error)
                    return@launch
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching search suggestions", e)
                _suggestionsError.value =
                    application.getString(R.string.can_t_fetch_suggestions_at_the_moment_please_try_again_later)
            } finally {
                _suggestionsLoading.value = false
                searchJob = null
            }
        }
    }

    private suspend fun handleResults(result: SearchResponseData) {
        val finalResults = result.matches.sortedBy { it.symbol }.take(MAX_SUGGESTIONS)
        withContext(Dispatchers.Main) {
            _suggestedStocks.value = SearchResponseData(finalResults)
        }
    }

    /**
     * Handles the selection of a stock symbol.
     * If the stock is already selected, it shows an error message.
     * Otherwise, it adds the stock to the selected list and clears the suggestions.
     */
    suspend fun onStockSelected(symbol: String, name: String) {
        Log.d(TAG, "onStockSelected()-> Symbol: [$symbol]")
        if (isAlreadySelected(symbol)) {
            Log.d(TAG, "Stock $symbol is already selected. Ignoring.")
            _commonErrorMessage.value = application.getString(R.string.already_added, symbol)
            return
        }
        _suggestedStocks.value = SearchResponseData(emptyList())
        if (stockRepository.addSelectedQuote(symbol, name)) {
            Log.d(TAG, "Stock $symbol added successfully.")
        } else {
            Log.e(TAG, "Failed to add stock $symbol.")
            withContext(Dispatchers.Main) {
                _commonErrorMessage.value = application.getString(R.string.failed_to_add_stock, symbol)
            }
        }
    }

    private fun isAlreadySelected(symbol: String): Boolean {
        return selectedStocks.value.any { it.quote.symbol == symbol }
    }

    fun onActivityVisible() {
        viewModelScope.launch {
            try {
                stockDataUpdateService.startUpdateService(viewModelScope)
                Log.d(TAG, "Update service started.")
            } catch (e: Exception) {
                Log.e(TAG, "Error starting update service", e)
            }
        }
    }

    fun onActivityNotVisible() {
        viewModelScope.launch {
            try {
                stockDataUpdateService.pauseUpdateService()
                Log.d(TAG, "Update service paused.")
            } catch (e: Exception) {
                Log.e(TAG, "Error pausing update service", e)
            }
        }
    }

    fun onCommonErrorHandled() {
        _commonErrorMessage.value = null
    }
}
