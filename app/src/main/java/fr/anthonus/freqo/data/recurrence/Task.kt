package fr.anthonus.freqo.data.recurrence

import kotlinx.serialization.json.Json

enum class Priority {
    NONE,
    LOW,
    MEDIUM,
    HIGH
}

data class Task(
    val id: String = "",
    val spaceId: String = "",
    val title: String = "",
    val description: String? = null,
    val locationLabel: String? = null,
    val priority: Priority = Priority.NONE,
    val recurrenceJson: String = "",
    val lastCompletedAt: Long? = null,
) {
    val recurrence: RecurrenceConfig
        get() = if (recurrenceJson.isBlank()) {
            IntervalDaysRecurrence(interval = 1)
        } else {
            runCatching {
                Json.decodeFromString<RecurrenceConfig>(recurrenceJson)
            }.getOrDefault(IntervalDaysRecurrence(interval = 1))
        }
}