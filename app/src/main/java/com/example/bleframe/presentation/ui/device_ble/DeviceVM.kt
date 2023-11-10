package com.example.bleframe.presentation.ui.device_ble

import androidx.lifecycle.*
import com.example.bleframe.data.ble.BleDataSources
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeviceVM @Inject constructor(
    bleDataSources: BleDataSources
) : ViewModel() {



}