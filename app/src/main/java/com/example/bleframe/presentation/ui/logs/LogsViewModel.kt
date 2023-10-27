package com.example.bleframe.presentation.ui.logs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bleframe.presentation.ui.device_ble.DeviceScanViewModel
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogsViewModel @Inject constructor() : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun build(): LogsViewModel
    }
}