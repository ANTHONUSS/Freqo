package fr.anthonus.freqo.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.anthonus.freqo.viewmodel.CalendarViewModel

@Composable
fun CalendarPage(
    calendarViewModel: CalendarViewModel = viewModel()
) {
    val uiState = calendarViewModel.calendarUiState.collectAsStateWithLifecycle()

    //TODO: temporary, only to test if it works
    val years = (2024..2030).toList()
    val zones = listOf(
        "metropole", "alsace-moselle", "guadeloupe", "guyane", "martinique",
        "mayotte", "nouvelle-caledonie", "polynesie-francaise", "la-reunion",
        "saint-barthelemy", "saint-martin", "saint-pierre-et-miquelon", "wallis-et-futuna"
    )
    val selectedYear = remember { mutableStateOf(2026) }
    val selectedZone = remember { mutableStateOf("metropole") }
    val yearExpanded = remember { mutableStateOf(false) }
    val zoneExpanded = remember { mutableStateOf(false) }

    LaunchedEffect(selectedYear.value, selectedZone.value) {
        calendarViewModel.fetchPublicHolidays(selectedYear.value, selectedZone.value)
    }

    Column {
        Row {
            Box {
                TextButton(onClick = { yearExpanded.value = true }) {
                    Text(selectedYear.value.toString())
                }
                DropdownMenu(
                    expanded = yearExpanded.value,
                    onDismissRequest = { yearExpanded.value = false }
                ) {
                    years.forEach { year ->
                        DropdownMenuItem(
                            text = { Text(year.toString()) },
                            onClick = {
                                selectedYear.value = year
                                yearExpanded.value = false
                            }
                        )
                    }
                }
            }

            Box {
                TextButton(onClick = { zoneExpanded.value = true }) {
                    Text(selectedZone.value)
                }
                DropdownMenu(
                    expanded = zoneExpanded.value,
                    onDismissRequest = { zoneExpanded.value = false }
                ) {
                    zones.forEach { zone ->
                        DropdownMenuItem(
                            text = { Text(zone) },
                            onClick = {
                                selectedZone.value = zone
                                zoneExpanded.value = false
                            }
                        )
                    }
                }
            }
        }

        if (uiState.value.isLoading) {
            CircularProgressIndicator()
        } else {
            uiState.value.publicHolidays.forEach {
                Row {
                    Text(it.name)
                    Text(it.date.toString())
                }
            }
        }
    }

}