package com.example.bleframe.presentation.ui.logs

import androidx.lifecycle.ViewModel
import com.example.bleframe.data.ble.BleDataSources
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogsVM @Inject constructor(
    bleDataSources: BleDataSources
) : ViewModel() {

}