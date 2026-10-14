package fr.anthonus.freqo.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

@Serializable
sealed interface RecurrenceConfig {
    fun displayLabel(): String
    fun nextDate(fromTimestamp: Long): Long
}

// "Tous les X jours"
@Serializable
@SerialName("INTERVAL_DAYS")
data class IntervalDaysRecurrence(
    val interval: Int = 1
) : RecurrenceConfig {
    override fun displayLabel(): String = "Tous les $interval jours"
    override fun nextDate(fromTimestamp: Long): Long =
        fromTimestamp + (interval.toLong() * 24 * 60 * 60 * 1000)
}

// "Chaque semaine les jours X, Y (ex: Lundi, Jeudi)"
// DayOfWeek : 1 (Lundi) à 7 (Dimanche)
@Serializable
@SerialName("WEEKLY_DAYS")
data class WeeklyDaysRecurrence(
    val targetDays: List<Int> = listOf(1)
) : RecurrenceConfig {
    override fun displayLabel(): String = "Hebdomadaire (${targetDays.size} jour(s)/semaine)"
    override fun nextDate(fromTimestamp: Long): Long {
        val zone = ZoneId.systemDefault()
        val current = Instant.ofEpochMilli(fromTimestamp).atZone(zone)
        // Recherche du prochain jour correspondant
        for (i in 1..7) {
            val candidate = current.plusDays(i.toLong())
            if (candidate.dayOfWeek.value in targetDays) {
                return candidate.toInstant().toEpochMilli()
            }
        }
        return fromTimestamp
    }
}

// "Toutes les X semaines"
@Serializable
@SerialName("INTERVAL_WEEKS")
data class IntervalWeeksRecurrence(
    val intervalWeeks: Int = 1,
    val dayOfWeek: Int = 1
) : RecurrenceConfig {
    override fun displayLabel(): String = "Toutes les $intervalWeeks semaines"
    override fun nextDate(fromTimestamp: Long): Long {
        val zone = ZoneId.systemDefault()
        val current = Instant.ofEpochMilli(fromTimestamp).atZone(zone)
        return current.plusWeeks(intervalWeeks.toLong())
            .with(DayOfWeek.of(dayOfWeek))
            .toInstant()
            .toEpochMilli()
    }
}

// "Le X de chaque mois" (ex: le 5 du mois)
@Serializable
@SerialName("MONTHLY_DAY")
data class MonthlyDayRecurrence(
    val dayOfMonth: Int = 1
) : RecurrenceConfig {
    override fun displayLabel(): String = "Le $dayOfMonth de chaque mois"
    override fun nextDate(fromTimestamp: Long): Long {
        val zone = ZoneId.systemDefault()
        val current = Instant.ofEpochMilli(fromTimestamp).atZone(zone)
        val nextMonth = current.plusMonths(1)
        val validDay = dayOfMonth.coerceAtMost(nextMonth.toLocalDate().lengthOfMonth())
        return nextMonth.withDayOfMonth(validDay).toInstant().toEpochMilli()
    }
}

// "Tous les X mois"
@Serializable
@SerialName("INTERVAL_MONTHS")
data class IntervalMonthsRecurrence(
    val intervalMonths: Int = 1
) : RecurrenceConfig {
    override fun displayLabel(): String = "Tous les $intervalMonths mois"
    override fun nextDate(fromTimestamp: Long): Long {
        val zone = ZoneId.systemDefault()
        val current = Instant.ofEpochMilli(fromTimestamp).atZone(zone)
        return current.plusMonths(intervalMonths.toLong()).toInstant().toEpochMilli()
    }
}

// "Toutes les X heures"
@Serializable
@SerialName("INTERVAL_HOURS")
data class IntervalHoursRecurrence(
    val hours: Int = 1
) : RecurrenceConfig {
    override fun displayLabel(): String = "Toutes les $hours heures"
    override fun nextDate(fromTimestamp: Long): Long =
        fromTimestamp + (hours.toLong() * 60 * 60 * 1000)
}

// "Tous les jours à HH:mm"
@Serializable
@SerialName("DAILY_AT_TIME")
data class DailyAtTimeRecurrence(
    val hour: Int = 8,
    val minute: Int = 0
) : RecurrenceConfig {
    override fun displayLabel(): String = "Tous les jours à %02d:%02d".format(hour, minute)
    override fun nextDate(fromTimestamp: Long): Long {
        val zone = ZoneId.systemDefault()
        val current = Instant.ofEpochMilli(fromTimestamp).atZone(zone)
        val todayAtTime = current.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
        val nextTarget = if (todayAtTime.isAfter(current)) todayAtTime else todayAtTime.plusDays(1)
        return nextTarget.toInstant().toEpochMilli()
    }
}

// "Le N-ième [Jour] du mois" (ex: le 1er samedi ou le dernier vendredi)
// ordinal: 1 (1er), 2 (2ème), ... ou -1 pour le dernier
@Serializable
@SerialName("MONTHLY_RELATIVE_DAY")
data class MonthlyRelativeDayRecurrence(
    val ordinal: Int = 1,
    val dayOfWeek: Int = 6 // Samedi par défaut
) : RecurrenceConfig {
    override fun displayLabel(): String {
        val pos = if (ordinal == -1) "Dernier" else "${ordinal}e"
        return "Le $pos jour spécifique du mois"
    }

    override fun nextDate(fromTimestamp: Long): Long {
        val zone = ZoneId.systemDefault()
        val current = Instant.ofEpochMilli(fromTimestamp).atZone(zone)
        val nextMonth = current.plusMonths(1)
        val targetDow = DayOfWeek.of(dayOfWeek)
        val adjusted = if (ordinal == -1) {
            nextMonth.with(TemporalAdjusters.lastInMonth(targetDow))
        } else {
            nextMonth.with(TemporalAdjusters.dayOfWeekInMonth(ordinal, targetDow))
        }
        return adjusted.toInstant().toEpochMilli()
    }
}

// "Une fois par an" (ex: le 15 Novembre)
@Serializable
@SerialName("YEARLY_DATE")
data class YearlyDateRecurrence(
    val month: Int = 1,
    val dayOfMonth: Int = 1
) : RecurrenceConfig {
    override fun displayLabel(): String = "Chaque année le $dayOfMonth/$month"
    override fun nextDate(fromTimestamp: Long): Long {
        val zone = ZoneId.systemDefault()
        val current = Instant.ofEpochMilli(fromTimestamp).atZone(zone)
        return current.plusYears(1)
            .withMonth(month)
            .withDayOfMonth(dayOfMonth)
            .toInstant()
            .toEpochMilli()
    }
}