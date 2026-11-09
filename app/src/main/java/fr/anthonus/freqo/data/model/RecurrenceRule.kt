package fr.anthonus.freqo.data.model

enum class Frequency {
    HOURLY,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}

/**
 * Recurrence rule, inspired by iCal RRULE. All dates use the ISO format: "2026-11-05" or "2026-11-05T08:30"
 *
 * ## Fields
 * - [frequency]: the unit of the period (hour, day, week, month, year)
 * - [interval]: "every N" units. 1 = every period, 2 = every other one, etc (Example: every other week)
 * - [startDate]: starting point
 * - [byDays]: days of the week, in RRULE format:
 *     "MO" "TU" "WE" "TH" "FR" "SA" "SU" for each day,
 *     with an ordinal in front for a position within the month:
 *     "1SA" = 1st Saturday, "2MO" = 2nd Monday, "-1FR" = last Friday
 *     The ordinal only makes sense with MONTHLY or YEARLY.
 * - [byMonthDay]: day of the month, from 1 to 31. -1 = last day of the month
 *     A day that does not exist in the month (the 31st in April) is skipped,
 *     except with -1, which always lands on the right day
 * - [byMonth]: month from 1 to 12. Mostly used with YEARLY
 * - [byHour], [byMinute]: hour and minute of the occurrence
 *     Without them, the occurrence is a plain date (no time)
 * - [fromCompletion]: if true, the next occurrence is computed from
 *     the last completion of the task, instead of the fixed calendar.
 * - [until]: inclusive end date. No occurrence after it. A date without time means the end of that day
 * - [count]: total number of occurrences, then stop
 *     [until] and [count] are mutually exclusive. Without both, the recurrence is infinite.
 *
 * ## Valid combinations
 * - HOURLY: [interval] only
 * - DAILY: [interval], optionally [byHour] and [byMinute]
 * - WEEKLY: [interval] + [byDays] (days without ordinal)
 *     If [byDays] is empty, the weekday of [startDate] is used
 * - MONTHLY: [byMonthDay] OR [byDays] with ordinal, never both
 * - YEARLY: [byMonth] + ([byMonthDay] OR [byDays] with ordinal)
 *
 * ## Examples
 * - Every 3 days: DAILY, interval=3
 * - Monday and Thursday: WEEKLY, byDays=["MO","TH"]
 * - The last Friday of the month: MONTHLY, byDays=["-1FR"]
 * - November 15th every year: YEARLY, byMonth=11, byMonthDay=15
 * - Every day at 8:30: DAILY, byHour=8, byMinute=30
 */
data class RecurrenceRule(
    val frequency: Frequency = Frequency.DAILY,
    val interval: Int = 1,
    val startDate: String = "",
    val byDays: List<String> = listOf(),
    val byMonth: Int? = null,
    val byMonthDay: Int? = null,
    val byHour: Int? = null,
    val byMinute: Int? = null,
    val fromCompletion: Boolean = false,
    val until: String? = null,
    val count: Int? = null,
)
