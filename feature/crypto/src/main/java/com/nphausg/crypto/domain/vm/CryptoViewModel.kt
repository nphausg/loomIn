package com.nphausg.crypto.domain.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nphausg.crypto.data.repository.CryptoRepository
import com.nphausg.crypto.domain.model.CryptoPrice
import com.nphausg.loom.LoomState
import com.nphausg.loom.loomIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val repository: CryptoRepository
) : ViewModel() {

    val uiState: StateFlow<LoomState<CryptoListUiState>> = loomIn(
        scope = viewModelScope,
        execute = { CryptoListUiState(repository.fetchCryptoPrices()) }
    ).state
}

data class CryptoListUiState(
    val items: List<CryptoPrice> = emptyList()
)