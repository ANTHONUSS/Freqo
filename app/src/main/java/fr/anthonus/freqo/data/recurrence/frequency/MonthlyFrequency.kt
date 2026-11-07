package fr.anthonus.freqo.data.recurrence.frequency

import fr.anthonus.freqo.data.recurrence.RecurrenceRule
import fr.anthonus.freqo.data.recurrence.datesInMonth
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.temporal.ChronoUnit

class MonthlyFrequency : FrequencyInterface {
    override fun firstPeriod(
        rule: RecurrenceRule,
        start: LocalDateTime,
        from: LocalDateTime
    ): LocalDateTime {
        val startMonth = YearMonth.from(start)
        val skippedPeriods: Long = if (rule.count != null) {
            0L
        } else {
            val monthsSinceStart = ChronoUnit.MONTHS.between(startMonth, YearMonth.from(from))
            maxOf(0L, monthsSinceStart / rule.interval)
        }

        return startMonth.plusMonths(skippedPeriods * rule.interval).atDay(1).atStartOfDay()
    }

    override fun nextPeriod(
        rule: RecurrenceRule,
        period: LocalDateTime
    ): LocalDateTime =
        period.plusMonths(rule.interval.toLong())

    override fun getCandidates(
        rule: RecurrenceRule,
        start: LocalDateTime,
        time: LocalTime,
        period: LocalDateTime
    ): List<LocalDateTime> =
        datesInMonth(rule, YearMonth.from(period), start).map { date -> date.atTime(time) }
}
