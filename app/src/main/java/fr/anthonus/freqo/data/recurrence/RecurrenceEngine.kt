package fr.anthonus.freqo.data.recurrence

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

object RecurrenceEngine {

    fun occurrences(
        rule: RecurrenceRule,
        from: LocalDateTime,
        to: LocalDateTime
    ): List<LocalDateTime> {
        val ruleErrors = rule.validate()
        if (
            ruleErrors.isNotEmpty()
            || from.isAfter(to)
        ) {
            return emptyList()
        }

        val start = parseDate(rule.startDate)!! // validated by rule.validate()
        val until = rule.until?.let { parseDate(it) }

        val time = if (rule.byHour != null) {
            LocalTime.of(rule.byHour, rule.byMinute ?: 0)
        } else {
            start.toLocalTime()
        }


        return when (rule.frequency) {
            Frequency.HOURLY -> hourlyOccurrences(rule, from, to, start, until)
            Frequency.DAILY -> dailyOccurrences(rule, from, to, start, until, time)
            Frequency.WEEKLY -> weeklyOccurrences(rule, from, to, start, until, time)
            else -> emptyList() //TODO: temporary, remove when all other frequencies are implemented
        }
    }

    private fun hourlyOccurrences(
        rule: RecurrenceRule,
        from: LocalDateTime,
        to: LocalDateTime,
        start: LocalDateTime,
        until: LocalDateTime?
    ): List<LocalDateTime> {
        val firstPeriodIndex: Long = if (rule.count != null) {
            0L
        } else {
            val hoursSinceStart = ChronoUnit.HOURS.between(start, from)
            maxOf(0L, hoursSinceStart / rule.interval)
        }

        var candidate = start.plusHours(firstPeriodIndex * rule.interval)
        var occurrenceCount = 0
        val occurrencesList = mutableListOf<LocalDateTime>()
        while (!candidate.isAfter(to)) {
            if (until != null && candidate.isAfter(until)) return occurrencesList
            if (rule.count != null) {
                occurrenceCount++
                if (occurrenceCount > rule.count) {
                    return occurrencesList
                }
            }
            if (!candidate.isBefore(from)) {
                occurrencesList.add(candidate)
            }

            candidate = candidate.plusHours(rule.interval.toLong())
        }

        return occurrencesList
    }

    private fun dailyOccurrences(
        rule: RecurrenceRule,
        from: LocalDateTime,
        to: LocalDateTime,
        start: LocalDateTime,
        until: LocalDateTime?,
        time: LocalTime
    ): List<LocalDateTime> {
        val startDay = start.toLocalDate()
        val firstPeriodIndex: Long = if (rule.count != null) {
            0L
        } else {
            val daysSinceStart = ChronoUnit.DAYS.between(startDay, from.toLocalDate())
            maxOf(0L, daysSinceStart / rule.interval)
        }

        var currentDay = startDay.plusDays(firstPeriodIndex * rule.interval)
        var occurrenceCount = 0
        val windowEndDay = to.toLocalDate()
        val occurrencesList = mutableListOf<LocalDateTime>()
        while (!currentDay.isAfter(windowEndDay)) {
            val candidate = currentDay.atTime(time)
            currentDay = currentDay.plusDays(rule.interval.toLong())

            if (candidate.isBefore(start)) continue
            if (until != null && candidate.isAfter(until)) break
            if (candidate.isAfter(to)) break
            if (rule.count != null) {
                occurrenceCount++
                if (occurrenceCount > rule.count) {
                    break
                }
            }
            if (candidate.isBefore(from)) continue

            occurrencesList.add(candidate)
        }

        return occurrencesList
    }

    private fun weeklyOccurrences(
        rule: RecurrenceRule,
        from: LocalDateTime,
        to: LocalDateTime,
        start: LocalDateTime,
        until: LocalDateTime?,
        time: LocalTime
    ): List<LocalDateTime> {
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

        val startMonday = start.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val firstPeriodIndex = if (rule.count != null) {
            0L
        } else {
            val fromMonday = from.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val weeksSinceStart = ChronoUnit.WEEKS.between(startMonday, fromMonday)
            maxOf(0, weeksSinceStart / rule.interval)
        }

        var currentMonday = startMonday.plusWeeks(firstPeriodIndex * rule.interval)
        val windowEndDay = to.toLocalDate()
        var occurrenceCount = 0
        val occurrencesList = mutableListOf<LocalDateTime>()
        while (!currentMonday.isAfter(windowEndDay)) {
            for (weekday in selectedDays) {
                val candidate = currentMonday.plusDays((weekday.value - 1).toLong()).atTime(time)

                if (candidate.isBefore(start)) continue
                if (until != null && candidate.isAfter(until)) return occurrencesList
                if (candidate.isAfter(to)) return occurrencesList
                if (rule.count != null) {
                    occurrenceCount++
                    if (occurrenceCount > rule.count) {
                        return occurrencesList
                    }
                }
                if (candidate.isBefore(from)) continue

                occurrencesList.add(candidate)
            }

            currentMonday = currentMonday.plusWeeks(rule.interval.toLong())
        }

        return occurrencesList

    }

}