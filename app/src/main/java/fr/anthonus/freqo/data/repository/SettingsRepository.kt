package fr.anthonus.freqo.data.repository

import fr.anthonus.freqo.data.datasource.settings.SettingsDataSource
import fr.anthonus.freqo.data.model.Settings
import kotlinx.coroutines.flow.Flow

object SettingsRepository {
    private val settingsDataSource = SettingsDataSource

    val settings: Flow<Settings> = settingsDataSource.settings

    suspend fun saveSettings(settings: Settings): Result<Unit> {
        return try {
            settingsDataSource.settings.value = settings
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}