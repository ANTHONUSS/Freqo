package fr.anthonus.freqo.navigation

import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import fr.anthonus.freqo.view.CalendarPage
import fr.anthonus.freqo.view.HomePage
import fr.anthonus.freqo.view.SettingsPage
import fr.anthonus.freqo.view.SpacePage
import fr.anthonus.freqo.view.SpacesPage
import fr.anthonus.freqo.view.TaskPage

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