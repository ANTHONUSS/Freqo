package fr.anthonus.freqo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.anthonus.freqo.data.publicholiday.PublicHoliday
import fr.anthonus.freqo.datasource.publicholiday.Resource
import fr.anthonus.freqo.repository.PublicHolidayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CalendarUiState(
    val publicHolidays: List<PublicHoliday>
)

class CalendarViewModel(
    private val publicHolidayRepository: PublicHolidayRepository = PublicHolidayRepository
) : ViewModel() {

    private val _calendarUiState = MutableStateFlow(CalendarUiState(listOf()))
    val calendarUiState: StateFlow<CalendarUiState> = _calendarUiState.asStateFlow()

    fun fetchPublicHolidays(year: Int, zone: String) {
        viewModelScope.launch {
            when (val result = publicHolidayRepository.getPublicHolidays(year, zone)) {
                is Resource.Success -> {
                    val publicHolidaysResult = result.data
                    _calendarUiState.update { currentState ->
                        currentState.copy(publicHolidays = publicHolidaysResult)
                    }
                }

                is Resource.Error -> {
                    Log.e(
                        "CalendarViewModel.fetchPublicHolidays",
                        "Error fetching data: ${result.error}"
                    )
                }
            }
        }
    }

}