package com.nphausg.loom.app.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nphausg.loom.LoomState
import com.nphausg.loom.loomIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val refresher = loomIn(viewModelScope) { getData() }
    val uiState: StateFlow<LoomState<String>> = refresher.state

    private suspend fun getData(): String {
        delay(2_000)
        return "nphausg"
    }

    /**
     * Returns `true` if the state wasn't loaded yet and it should keep showing the splash screen.
     */
    val isLoading: Boolean
        get() = uiState.value is LoomState.Loading
}