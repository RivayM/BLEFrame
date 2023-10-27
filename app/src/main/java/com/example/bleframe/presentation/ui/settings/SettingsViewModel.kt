package com.example.bleframe.presentation.ui.settings

import androidx.lifecycle.ViewModel
import dagger.assisted.AssistedFactory
import javax.inject.Inject

class SettingsViewModel  @Inject constructor(): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun build(): SettingsViewModel
    }
}