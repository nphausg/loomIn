package com.nphausg.loom.app.domain.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nphausg.loom.LoomState
import com.nphausg.loom.loomIn
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val refresher = loomIn(viewModelScope) { getData() }
    val loomState: StateFlow<LoomState<String>> = refresher.state
    
    private suspend fun getData(): String {
        delay(1_000)
        return "nphausg"
    }
}