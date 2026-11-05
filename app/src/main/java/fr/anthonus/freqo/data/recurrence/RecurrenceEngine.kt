package fr.anthonus.freqo.data.recurrence

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

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
            Frequency.DAILY -> dailyOccurrences(rule, from, to, start, until, time)
            else -> emptyList() //TODO: temporary, remove when all other frequencies are implemented
        }
    }

    private fun dailyOccurrences(
        rule: RecurrenceRule,
        from: LocalDateTime,
        to: LocalDateTime,
        start: LocalDateTime,
        until: LocalDateTime?,
        time: LocalTime
    ): List<LocalDateTime> {
        var counter = 0
        val occurrencesList = mutableListOf<LocalDateTime>()

        val startDay = start.toLocalDate()
        val endDate = to.toLocalDate()

        val firstPeriod: Long = if (rule.count != null) {
            0L
        } else {
            val gap = ChronoUnit.DAYS.between(startDay, from.toLocalDate())
            maxOf(0L, gap / rule.interval)
        }

        var day = startDay.plusDays(firstPeriod * rule.interval)
        while (!day.isAfter(endDate)) {
            val candidate = day.atTime(time)
            day = day.plusDays(rule.interval.toLong())

            if (candidate.isBefore(start)) continue
            if (until != null && candidate.isAfter(until)) break
            if (candidate.isAfter(to)) break
            if (rule.count != null) {
                counter++
                if (counter > rule.count) {
                    break
                }
            }
            if (candidate.isBefore(from)) continue

            occurrencesList.add(candidate)
        }

        return occurrencesList
    }

}