package io.fresco.zenzapassignment.ui.screens.mainscreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.fresco.zenzapassignment.data.GlobalQuoteData
import io.fresco.zenzapassignment.data.SearchResponseData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    companion object{
        private const val TAG = "MainViewModel"
    }

    private val _selectedStocks = MutableStateFlow<List<GlobalQuoteData>>(emptyList())
    val selectedStocks = _selectedStocks.asStateFlow()

    private val _suggestedStocks = MutableStateFlow<SearchResponseData>(SearchResponseData(emptyList()))
    val suggestedStocks = _suggestedStocks.asStateFlow()
}