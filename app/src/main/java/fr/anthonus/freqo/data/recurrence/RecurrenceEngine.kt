package fr.anthonus.freqo.data.recurrence

import fr.anthonus.freqo.data.recurrence.frequency.DailyFrequency
import fr.anthonus.freqo.data.recurrence.frequency.FrequencyInterface
import fr.anthonus.freqo.data.recurrence.frequency.HourlyFrequency
import fr.anthonus.freqo.data.recurrence.frequency.MonthlyFrequency
import fr.anthonus.freqo.data.recurrence.frequency.WeeklyFrequency
import fr.anthonus.freqo.data.recurrence.frequency.YearlyFrequency
import java.time.LocalDateTime
import java.time.LocalTime

object RecurrenceEngine {

    fun getOccurrences(
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

        val frequency: FrequencyInterface = when (rule.frequency) {
            Frequency.HOURLY -> HourlyFrequency()
            Frequency.DAILY -> DailyFrequency()
            Frequency.WEEKLY -> WeeklyFrequency()
            Frequency.MONTHLY -> MonthlyFrequency()
            Frequency.YEARLY -> YearlyFrequency()
        }

        var occurrenceCount = 0
        val occurrencesList = mutableListOf<LocalDateTime>()

        var period = frequency.firstPeriod(rule, start, from)
        while (!period.isAfter(to)) {
            for (candidate in frequency.getCandidates(rule, start, time, period)) {
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

            period = frequency.nextPeriod(rule, period)
        }

        return occurrencesList
    }

}
