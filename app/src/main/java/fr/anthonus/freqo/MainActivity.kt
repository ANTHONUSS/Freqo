package fr.anthonus.freqo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import fr.anthonus.freqo.data.Home
import fr.anthonus.freqo.data.MainTab
import fr.anthonus.freqo.data.Profile
import fr.anthonus.freqo.data.Search
import fr.anthonus.freqo.navdisplay.HomeTabNavDisplay
import fr.anthonus.freqo.presentation.ProfilePage
import fr.anthonus.freqo.presentation.SearchPage
import fr.anthonus.freqo.ui.theme.FreqoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FreqoTheme {
                Freqo()
            }
        }
    }
}

@Composable
fun Freqo() {
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.HOME) }

    val homeBackStack = rememberNavBackStack(Home)
    val searchBackStack = rememberNavBackStack(Search)
    val profileBackStack = rememberNavBackStack(Profile)

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        bottomBar = {
            NavigationBar {
                MainTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                painterResource(tab.icon),
                                contentDescription = tab.iconDescription
                            )
                        },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            Modifier.padding(innerPadding)
        ) {
            when (selectedTab) {
                MainTab.HOME -> HomeTabNavDisplay(homeBackStack)

                MainTab.SEARCH -> NavDisplay(
                    backStack = searchBackStack,
                    entryProvider = entryProvider {
                        entry<Search> { SearchPage() }
                    }
                )

                MainTab.PROFILE -> NavDisplay(
                    backStack = profileBackStack,
                    entryProvider = entryProvider {
                        entry<Profile> { ProfilePage() }
                    }
                )
            }
        }
    }
}