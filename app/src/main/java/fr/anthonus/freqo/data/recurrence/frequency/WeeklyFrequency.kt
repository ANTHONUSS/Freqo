package fr.anthonus.freqo.data.recurrence.frequency

import fr.anthonus.freqo.data.recurrence.RecurrenceRule
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

class WeeklyFrequency : FrequencyInterface {
    override fun firstPeriod(
        rule: RecurrenceRule,
        start: LocalDateTime,
        from: LocalDateTime
    ): LocalDateTime {
        val startMonday = mondayOf(start.toLocalDate())
        val skippedPeriods: Long = if (rule.count != null) {
            0L
        } else {
            val fromMonday = mondayOf(from.toLocalDate())
            val weeksSinceStart = ChronoUnit.WEEKS.between(startMonday, fromMonday)
            maxOf(0L, weeksSinceStart / rule.interval)
        }

        return startMonday.plusWeeks(skippedPeriods * rule.interval).atStartOfDay()
    }

    override fun nextPeriod(
        rule: RecurrenceRule,
        period: LocalDateTime
    ): LocalDateTime =
        period.plusWeeks(rule.interval.toLong())

    override fun getCandidates(
        rule: RecurrenceRule,
        start: LocalDateTime,
        time: LocalTime,
        period: LocalDateTime
    ): List<LocalDateTime> {
        val monday = period.toLocalDate()

        return selectedDays(rule, start).map { weekday ->
            monday.plusDays((weekday.value - 1).toLong()).atTime(time)
        }
    }

    private fun mondayOf(date: LocalDate): LocalDate =
        date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    private fun selectedDays(
        rule: RecurrenceRule,
        start: LocalDateTime
    ): List<DayOfWeek> {
        val selectedDays = mutableListOf<DayOfWeek>()
        if (rule.byDays.isEmpty()) {
            selectedDays.add(start.dayOfWeek)
        } else {
            for (code in rule.byDays) {
                for (weekday in DayOfWeek.entries) {
                    if (weekday.name.take(2) == code && !selectedDays.contains(weekday)) {
                        selectedDays.add(weekday)
                    }
                }
            }
        }
        selectedDays.sortBy { weekday -> weekday.value }

        return selectedDays
    }
}
