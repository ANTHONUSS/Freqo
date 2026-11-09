package fr.anthonus.freqo.ui.navigation

import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import fr.anthonus.freqo.ui.view.CalendarPage
import fr.anthonus.freqo.ui.view.HomePage
import fr.anthonus.freqo.ui.view.SettingsPage
import fr.anthonus.freqo.ui.view.SpacePage
import fr.anthonus.freqo.ui.view.SpacesPage
import fr.anthonus.freqo.ui.view.TaskPage

fun appEntryProvider(
    onNavigationToSpace: (Int) -> Unit,
    onNavigationToTask: (Int) -> Unit
) = entryProvider<NavKey> {
    entry<Home> {
        HomePage(
            onSpaceClick = onNavigationToSpace
        )
    }
    entry<Spaces> {
        SpacesPage(
            onSpaceClick = onNavigationToSpace
        )
    }
    entry<Calendar> {
        CalendarPage()
    }
    entry<Settings> {
        SettingsPage()
    }
    entry<Space> { key ->
        SpacePage(
            id = key.id,
            onTaskClick = onNavigationToTask
        )
    }
    entry<Task> { key ->
        TaskPage(id = key.id)
    }
}