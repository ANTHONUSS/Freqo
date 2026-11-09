package fr.anthonus.freqo.domain.recurrence

import fr.anthonus.freqo.data.model.RecurrenceRule
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeParseException
import java.time.temporal.TemporalAdjusters

val END_OF_DAY: LocalTime = LocalTime.of(23, 59, 59)

fun parseDate(dateText: String, defaultTime: LocalTime = LocalTime.MIDNIGHT): LocalDateTime? {
    return try {
        LocalDateTime.parse(dateText)
    } catch (_: DateTimeParseException) {
        try {
            LocalDate.parse(dateText).atTime(defaultTime)
        } catch (_: DateTimeParseException) {
            null
        }
    }
}

fun datesInMonth(
    rule: RecurrenceRule,
    month: YearMonth,
    start: LocalDateTime
): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()

    if (rule.byMonthDay != null) {
        val dayOfMonth = if (rule.byMonthDay == -1) month.lengthOfMonth() else rule.byMonthDay
        if (dayOfMonth <= month.lengthOfMonth()) {
            dates.add(month.atDay(dayOfMonth))
        }
    } else if (rule.byDays.isNotEmpty()) {
        for (code in rule.byDays) {
            val ordinal = code.dropLast(2).toInt()
            val weekday = DayOfWeek.entries.first { it.name.take(2) == code.takeLast(2) }
            val date = month.atDay(1).with(TemporalAdjusters.dayOfWeekInMonth(ordinal, weekday))
            if (YearMonth.from(date) == month && !dates.contains(date)) {
                dates.add(date)
            }
        }
    } else if (start.dayOfMonth <= month.lengthOfMonth()) {
        dates.add(month.atDay(start.dayOfMonth))
    }

    dates.sortBy { date -> date.dayOfMonth }
    return dates
}