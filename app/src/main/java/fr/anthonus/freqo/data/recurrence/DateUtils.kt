package fr.anthonus.freqo.data.recurrence

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

fun parseDate(dateText: String): LocalDateTime? {
    return try {
        LocalDateTime.parse(dateText)
    } catch (_: DateTimeParseException) {
        try {
            LocalDate.parse(dateText).atStartOfDay()
        } catch (_: DateTimeParseException) {
            null
        }
    }
}