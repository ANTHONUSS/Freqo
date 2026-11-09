package fr.anthonus.freqo.domain.recurrence.frequency

import fr.anthonus.freqo.data.model.RecurrenceRule
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class HourlyFrequency : FrequencyInterface {
    override fun firstPeriod(
        rule: RecurrenceRule,
        start: LocalDateTime,
        from: LocalDateTime
    ): LocalDateTime {
        val skippedPeriods = if (rule.count != null) {
            0L
        } else {
            val hoursSinceStart = ChronoUnit.HOURS.between(start, from)
            maxOf(0L, hoursSinceStart / rule.interval)
        }

        return start.plusHours(skippedPeriods * rule.interval)
    }

    override fun nextPeriod(
        rule: RecurrenceRule,
        period: LocalDateTime
    ): LocalDateTime =
        period.plusHours(rule.interval.toLong())

    override fun getCandidates(
        rule: RecurrenceRule,
        start: LocalDateTime,
        time: LocalTime,
        period: LocalDateTime
    ): List<LocalDateTime> =
        listOf(period)
}