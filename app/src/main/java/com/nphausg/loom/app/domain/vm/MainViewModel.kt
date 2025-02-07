package com.nphausg.loom.app.domain.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nphausg.loom.LoomState
import com.nphausg.loom.app.domain.model.CryptoPrice
import com.nphausg.loom.app.domain.repository.CryptoRepository
import com.nphausg.loom.loomIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CryptoRepository
) : ViewModel() {

    val uiState: StateFlow<LoomState<MainUiState>> = loomIn(viewModelScope) {
        MainUiState(repository.invoke())
    }.state
}

data class MainUiState(
    val items: List<CryptoPrice> = emptyList()
)