package com.nphausg.crypto.domain.vm

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nphausg.crypto.data.repository.CryptoRepository
import com.nphausg.crypto.domain.model.CryptoPrice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class CryptoViewModel(private val cryptoRepository: CryptoRepository) : ViewModel() {

    val cryptoPrices = mutableStateOf<List<CryptoPrice>>(emptyList())

    init {
        // Start fetching data periodically
        fetchPricesPeriodically()
    }

    private fun fetchPricesPeriodically() {
        viewModelScope.launch {
            while (true) {
                try {
                    // cryptoPrices.value = cryptoRepository.fetchCryptoPrices()
                    cryptoPrices.value = emptyList()
                } catch (e: Exception) {
                    // Handle error appropriately (e.g., log, show error message)
                }
                delay(5000) // Fetch every 5 seconds
            }
        }
    }
}