package fr.anthonus.freqo.viewmodel

import fr.anthonus.freqo.data.publicholiday.PublicHoliday

data class CalendarUiState(
    val publicHolidays: List<PublicHoliday>,
    val isLoading: Boolean
)