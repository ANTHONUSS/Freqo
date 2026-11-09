package fr.anthonus.freqo.domain.recurrence.frequency

import fr.anthonus.freqo.data.model.RecurrenceRule
import fr.anthonus.freqo.domain.recurrence.datesInMonth
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth

class YearlyFrequency : FrequencyInterface {
    override fun firstPeriod(
        rule: RecurrenceRule,
        start: LocalDateTime,
        from: LocalDateTime
    ): LocalDateTime {
        val targetMonth = rule.byMonth ?: start.monthValue
        val startYearMonth = YearMonth.of(start.year, targetMonth)
        val skippedPeriods: Long = if (rule.count != null) {
            0L
        } else {
            val yearsSinceStart = (from.year - start.year).toLong()
            maxOf(0L, yearsSinceStart / rule.interval)
        }

        return startYearMonth.plusYears(skippedPeriods * rule.interval).atDay(1).atStartOfDay()
    }

    override fun nextPeriod(
        rule: RecurrenceRule,
        period: LocalDateTime
    ): LocalDateTime =
        period.plusYears(rule.interval.toLong())

    override fun getCandidates(
        rule: RecurrenceRule,
        start: LocalDateTime,
        time: LocalTime,
        period: LocalDateTime
    ): List<LocalDateTime> =
        datesInMonth(rule, YearMonth.from(period), start).map { date -> date.atTime(time) }
}
