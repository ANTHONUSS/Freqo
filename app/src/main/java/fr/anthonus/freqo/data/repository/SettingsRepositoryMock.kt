package fr.anthonus.freqo.data.repository

import fr.anthonus.freqo.data.model.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface SettingsRepositoryInterface {

    val settings: Flow<Settings>
    suspend fun saveSettings(settings: Settings): Result<Unit>
}

class SettingsRepositoryMock: SettingsRepositoryInterface {
    private val _fakeSettings = MutableStateFlow(Settings())

    override val settings: Flow<Settings> = _fakeSettings

    override suspend fun saveSettings(settings: Settings): Result<Unit> {
        return try {
            _fakeSettings.value = settings
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}