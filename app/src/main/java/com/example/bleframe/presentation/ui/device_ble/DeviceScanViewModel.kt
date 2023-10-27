package com.example.bleframe.presentation.ui.device_ble

import androidx.lifecycle.*
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeviceScanViewModel @Inject constructor(
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun build(): DeviceScanViewModel
    }
}