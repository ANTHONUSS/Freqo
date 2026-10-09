package fr.anthonus.freqo.data

import androidx.navigation3.runtime.NavKey
import fr.anthonus.freqo.R
import kotlinx.serialization.Serializable

enum class MainTab(
    val label: String,
    val icon: Int,
    val iconDescription: String,
) {
    HOME("Accueil", R.drawable.ic_home, "Bouton accueil"),
    SEARCH("Rechercher", R.drawable.ic_search, "Bouton rechercher"),
    PROFILE("Profil", R.drawable.ic_profile_24dp, "Bouton profil"),
}

@Serializable
sealed interface HomeKey : NavKey

@Serializable
data object Home : HomeKey

@Serializable
data class HomeDetail(val id: String) : HomeKey

@Serializable
data object Search : NavKey

@Serializable
data object Profile : NavKey