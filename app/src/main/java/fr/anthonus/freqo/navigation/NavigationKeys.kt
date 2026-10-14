package fr.anthonus.freqo.navigation

import androidx.navigation3.runtime.NavKey
import fr.anthonus.freqo.R
import kotlinx.serialization.Serializable

enum class Tab(
    val label: String,
    val icon: Int,
    val iconSelected: Int,
    val iconDescription: String,
) {
    HOME(
        "Accueil",
        R.drawable.home_outlined,
        R.drawable.home_filled,
        "Bouton accueil"
    ),
    SPACES(
        "Espaces",
        R.drawable.space_outlined,
        R.drawable.space_filled,
        "Bouton espaces"
    ),
    CALENDAR(
        "Calendrier",
        R.drawable.calendar_outlined,
        R.drawable.calendar_filled,
        "Bouton calendrier"
    ),
    SETTINGS(
        "Paramètres",
        R.drawable.settings_outlined,
        R.drawable.settings_filled,
        "Bouton profil"
    ),
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