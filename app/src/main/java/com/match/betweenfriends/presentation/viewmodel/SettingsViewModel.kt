package com.match.betweenfriends.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.match.betweenfriends.common.SharedPref
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SettingsUiState(
    val isSoundEnabled: Boolean = false,
    val isVibrationEnabled: Boolean = false
)

class SettingsViewModel(private val sharedPref: SharedPref) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState get() = _uiState.asStateFlow()

    fun toggleSound() {
        sharedPref.sound = !sharedPref.sound
        _uiState.update {
            it.copy(isSoundEnabled = sharedPref.sound)
        }
    }

    fun toggleVibration() {
        sharedPref.vibration = !sharedPref.vibration
        _uiState.update {
            it.copy(isVibrationEnabled = sharedPref.vibration)
        }
    }

    init {
        _uiState.update {
            it.copy(
                isSoundEnabled = sharedPref.sound,
                isVibrationEnabled = sharedPref.vibration
            )
        }
    }
}

@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(SharedPref(context)) as T
    }
}