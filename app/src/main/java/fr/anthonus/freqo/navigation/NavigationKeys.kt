package fr.anthonus.freqo.navigation

import androidx.navigation3.runtime.NavKey
import fr.anthonus.freqo.R
import kotlinx.serialization.Serializable

enum class Tab(
    val label: String,
    val icon: Int,
    val iconDescription: String,
) {
    HOME("Accueil", R.drawable.home_24, "Bouton accueil"),
    SPACES("Espaces", R.drawable.spaces_24, "Bouton espaces"),
    CALENDAR("Calendrier", R.drawable.calendar_24, "Bouton calendrier"),
    SETTINGS("Paramètres", R.drawable.settings_24, "Bouton profil"),
}

@Serializable
sealed interface AppRoute : NavKey

@Serializable
data object Home : AppRoute

@Serializable
data object Spaces : AppRoute

@Serializable
data object Calendar : AppRoute

@Serializable
data object Settings : AppRoute

@Serializable
data class Space(val id: Int) : AppRoute

@Serializable
data class Task(val id: Int) : AppRoute