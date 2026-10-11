package fr.anthonus.freqo.navigation

import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import fr.anthonus.freqo.ui.CalendarPage
import fr.anthonus.freqo.ui.HomePage
import fr.anthonus.freqo.ui.SettingsPage
import fr.anthonus.freqo.ui.SpacePage
import fr.anthonus.freqo.ui.SpacesPage
import fr.anthonus.freqo.ui.TaskPage

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