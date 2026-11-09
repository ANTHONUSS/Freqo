package fr.anthonus.freqo.domain.recurrence

import fr.anthonus.freqo.data.model.Frequency
import fr.anthonus.freqo.data.model.RecurrenceRule

private val byDayRegex = Regex("(-1|[1-5])?(MO|TU|WE|TH|FR|SA|SU)")
private val byDayWithOrdinalRegex = Regex("(-1|[1-5])(MO|TU|WE|TH|FR|SA|SU)")
private val byDayWithoutOrdinalRegex = Regex("(MO|TU|WE|TH|FR|SA|SU)")

enum class RuleError(val message: String) {
    INVALID_INTERVAL("interval is lower than 1"),
    INVALID_COUNT("count is lower than 1"),
    INVALID_BY_MONTH("byMonth is not between 1 and 12"),
    INVALID_BY_HOUR("byHour is not between 0 and 23"),
    INVALID_BY_MINUTE("byMinute is not between 0 and 59"),
    INVALID_BY_MONTH_DAY("byMonthDay is neither -1 nor between 1 and 31"),
    BY_MINUTE_WITHOUT_HOUR("byMinute is set without byHour"),

    INVALID_START_DATE("startDate is blank or is not a valid ISO date"),
    INVALID_UNTIL("until is not a valid ISO date"),
    UNTIL_BEFORE_START("until is earlier than startDate"),

    INVALID_BY_DAYS("a byDays entry does not match the RRULE format (\"MO\", \"1SA\", \"-1FR\")"),

    ORDINAL_NOT_ALLOWED("byDays contains an ordinal (\"1SA\") but the frequency does not accept it (WEEKLY)"),
    ORDINAL_REQUIRED("byDays has an entry without ordinal but the frequency requires one (MONTHLY, YEARLY)"),
    BY_DAYS_AND_BY_MONTH_DAY("byDays and byMonthDay are both set, they are mutually exclusive"),
    BY_MONTH_REQUIRED("YEARLY with byDays or byMonthDay but no byMonth"),

    UNTIL_AND_COUNT("until and count are both set, they are mutually exclusive"),

    FIELD_NOT_ALLOWED_FOR_FREQUENCY("a field is set that the frequency does not accept (ex: byDays with DAILY)"),
}

fun RecurrenceRule.validate(): List<RuleError> {
    val errors = mutableListOf<RuleError>()

    if (interval < 1) errors.add(RuleError.INVALID_INTERVAL)
    if (count != null && count < 1) errors.add(RuleError.INVALID_COUNT)
    if (byMonth != null && byMonth !in 1..12) errors.add(RuleError.INVALID_BY_MONTH)
    if (byHour != null && byHour !in 0..23) errors.add(RuleError.INVALID_BY_HOUR)
    if (byMinute != null && byMinute !in 0..59) errors.add(RuleError.INVALID_BY_MINUTE)
    if (byMonthDay != null && byMonthDay != -1 && byMonthDay !in 1..31) errors.add(RuleError.INVALID_BY_MONTH_DAY)
    if (byMinute != null && byHour == null) errors.add(RuleError.BY_MINUTE_WITHOUT_HOUR)

    val start = parseDate(startDate)
    val end = until?.let { parseDate(it, END_OF_DAY) }
    if (start == null) errors.add(RuleError.INVALID_START_DATE)
    if (until != null && end == null) errors.add(RuleError.INVALID_UNTIL)
    if (start != null && end != null && end < start) errors.add(RuleError.UNTIL_BEFORE_START)

    if (!byDays.all { byDayRegex.matches(it) }) errors.add(RuleError.INVALID_BY_DAYS)

    if (until != null && count != null) errors.add(RuleError.UNTIL_AND_COUNT)

    when (frequency) {
        Frequency.HOURLY -> if (
            byDays.isNotEmpty()
            || byMonthDay != null
            || byMonth != null
            || byHour != null
            || byMinute != null
        ) {
            errors.add(RuleError.FIELD_NOT_ALLOWED_FOR_FREQUENCY)
        }

        Frequency.DAILY -> if (
            byDays.isNotEmpty()
            || byMonthDay != null
            || byMonth != null
        ) {
            errors.add(RuleError.FIELD_NOT_ALLOWED_FOR_FREQUENCY)
        }

        Frequency.WEEKLY -> {
            if (byMonthDay != null || byMonth != null) errors.add(RuleError.FIELD_NOT_ALLOWED_FOR_FREQUENCY)

            if (byDays.any { byDayWithOrdinalRegex.matches(it) }) errors.add(RuleError.ORDINAL_NOT_ALLOWED)
        }

        Frequency.MONTHLY -> {
            if (byMonth != null) errors.add(RuleError.FIELD_NOT_ALLOWED_FOR_FREQUENCY)

            if (byDays.isNotEmpty() && byMonthDay != null) errors.add(RuleError.BY_DAYS_AND_BY_MONTH_DAY)
            if (byDays.any { byDayWithoutOrdinalRegex.matches(it) }) errors.add(RuleError.ORDINAL_REQUIRED)
        }

        Frequency.YEARLY -> {
            if (byDays.isNotEmpty() && byMonthDay != null) errors.add(RuleError.BY_DAYS_AND_BY_MONTH_DAY)
            if (byDays.any { byDayWithoutOrdinalRegex.matches(it) }) errors.add(RuleError.ORDINAL_REQUIRED)
            if ((byDays.isNotEmpty() || byMonthDay != null) && byMonth == null) errors.add(RuleError.BY_MONTH_REQUIRED)
        }
    }

    return errors
}

