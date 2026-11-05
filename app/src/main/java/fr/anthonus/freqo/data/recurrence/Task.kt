package fr.anthonus.freqo.data.recurrence

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
    val priority: Priority = Priority.NONE,
    val description: String? = null,
    val lastCompletedAt: String? = null,
    val rule: RecurrenceRule = RecurrenceRule(),
)