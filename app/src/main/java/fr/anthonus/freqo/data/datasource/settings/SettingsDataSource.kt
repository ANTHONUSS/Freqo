package fr.anthonus.freqo.data.datasource.settings

import fr.anthonus.freqo.data.model.Settings
import kotlinx.coroutines.flow.MutableStateFlow

//TODO: temporary, waiting for firebase implementation
object SettingsDataSource {
    val settings = MutableStateFlow(Settings())
}