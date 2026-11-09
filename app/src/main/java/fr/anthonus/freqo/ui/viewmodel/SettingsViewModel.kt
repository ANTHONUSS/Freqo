package fr.anthonus.freqo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.anthonus.freqo.data.model.Settings
import fr.anthonus.freqo.data.repository.SettingsRepositoryInterface
import fr.anthonus.freqo.data.repository.SettingsRepositoryMock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepositoryInterface = SettingsRepositoryMock()
): ViewModel() {
    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState.asStateFlow()

    init { observeSettings() }

    private fun observeSettings() {
        viewModelScope.launch {
            settingsRepository.settings
                .catch { error ->
                    Log.e("SettingsViewModel.observeSettings", error.toString())
                    _settingsUiState.update {
                        it.copy(errorMessage = "Error while gathering data")
                    }
                }
                .collect { data ->
                    _settingsUiState.update {
                        it.copy(settings = data, errorMessage = null)
                    }
                }
        }
    }

    private fun saveSettings(newSettings: Settings) {
        viewModelScope.launch {
            settingsRepository.saveSettings(newSettings).onFailure { error ->
                Log.e("SettingsViewModel.saveSettings", error.toString())
                _settingsUiState.update { it.copy(errorMessage = "Error while saving data") }
            }
        }
    }

    fun setShowBottomBarLabels(show: Boolean) =
        saveSettings(_settingsUiState.value.settings.copy(showBottomBarLabels = show))
}