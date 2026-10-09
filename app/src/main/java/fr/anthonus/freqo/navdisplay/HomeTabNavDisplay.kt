package fr.anthonus.freqo.navdisplay

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import fr.anthonus.freqo.data.Home
import fr.anthonus.freqo.data.HomeDetail
import fr.anthonus.freqo.presentation.home.HomeDetailPage
import fr.anthonus.freqo.presentation.home.HomePage

@Composable
fun HomeTabNavDisplay(backstack: NavBackStack<NavKey>) {
    NavDisplay(
        backStack = backstack,
        onBack = { backstack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Home> {
                HomePage(
                    onItemClick = { id -> backstack.add(HomeDetail(id)) }
                )
            }
            entry<HomeDetail> { key ->
                HomeDetailPage(
                    key.id
                )
            }
        }
    )
}