package fr.anthonus.freqo.ui.viewmodel

import fr.anthonus.freqo.data.model.publicholiday.PublicHoliday

data class CalendarUiState(
    val publicHolidays: List<PublicHoliday>,
    val isLoading: Boolean
)