package com.example.bleframe.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bleframe.data.ble.BleDataSources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    bleDataSources: BleDataSources
) : ViewModel() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope( job + viewModelScope.coroutineContext + Dispatchers.Default)

    val stateBle = flow<Boolean> {
        //bleDataSources.getBleState()
    }.stateIn(
        scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.WhileSubscribed(DELAY),
        initialValue = bleDataSources.getBleState()
    )


    companion object {
        const val DELAY = 100L
    }
}