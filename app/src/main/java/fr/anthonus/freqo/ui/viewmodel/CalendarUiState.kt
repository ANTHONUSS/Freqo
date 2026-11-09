package fr.anthonus.freqo.ui.viewmodel

import fr.anthonus.freqo.data.model.PublicHoliday

data class CalendarUiState(
    val publicHolidays: List<PublicHoliday> = listOf(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)