package fr.anthonus.freqo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.anthonus.freqo.data.repository.PublicHolidayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CalendarViewModel(
    private val publicHolidayRepository: PublicHolidayRepository = PublicHolidayRepository()
) : ViewModel() {

    private val _calendarUiState = MutableStateFlow(CalendarUiState())
    val calendarUiState: StateFlow<CalendarUiState> = _calendarUiState.asStateFlow()

    fun fetchPublicHolidays(year: Int, zone: String = "metropole") {
        viewModelScope.launch {
            _calendarUiState.update { currentState ->
                currentState.copy(isLoading = true)
            }

            val result = publicHolidayRepository.getPublicHolidays(zone, year)
            result.fold(
                onSuccess = { data ->
                    _calendarUiState.update { currentState ->
                        currentState.copy(
                            publicHolidays = data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    Log.e(
                        "CalendarViewModel.fetchPublicHolidays",
                        error.toString()
                    )
                    _calendarUiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            errorMessage = "Data is unavailable"
                        )
                    }
                }
            )
        }
    }

}