package fr.anthonus.freqo.ui.viewmodel

import fr.anthonus.freqo.data.model.Settings

data class SettingsUiState(
    val settings: Settings = Settings(),
    val errorMessage: String? = null
)