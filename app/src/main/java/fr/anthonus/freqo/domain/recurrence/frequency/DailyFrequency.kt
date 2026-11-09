package fr.anthonus.freqo.domain.recurrence.frequency

import fr.anthonus.freqo.data.model.RecurrenceRule
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class DailyFrequency : FrequencyInterface {
    override fun firstPeriod(
        rule: RecurrenceRule,
        start: LocalDateTime,
        from: LocalDateTime
    ): LocalDateTime {
        val startDay = start.toLocalDate()
        val skippedPeriods: Long = if (rule.count != null) {
            0L
        } else {
            val daysSinceStart = ChronoUnit.DAYS.between(startDay, from.toLocalDate())
            maxOf(0L, daysSinceStart / rule.interval)
        }

        return startDay.plusDays(skippedPeriods * rule.interval).atStartOfDay()
    }

    override fun nextPeriod(
        rule: RecurrenceRule,
        period: LocalDateTime
    ): LocalDateTime =
        period.plusDays(rule.interval.toLong())

    override fun getCandidates(
        rule: RecurrenceRule,
        start: LocalDateTime,
        time: LocalTime,
        period: LocalDateTime
    ): List<LocalDateTime> =
        listOf(period.toLocalDate().atTime(time))
}