package fr.anthonus.freqo.domain.recurrence.frequency

import fr.anthonus.freqo.data.model.RecurrenceRule
import java.time.LocalDateTime
import java.time.LocalTime

interface FrequencyInterface {
    fun firstPeriod(
        rule: RecurrenceRule,
        start: LocalDateTime,
        from: LocalDateTime
    ): LocalDateTime

    fun nextPeriod(
        rule: RecurrenceRule,
        period: LocalDateTime
    ): LocalDateTime

    fun getCandidates(
        rule: RecurrenceRule,
        start: LocalDateTime,
        time: LocalTime,
        period: LocalDateTime
    ): List<LocalDateTime>
}