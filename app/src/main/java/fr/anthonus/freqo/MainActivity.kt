package fr.anthonus.freqo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import fr.anthonus.freqo.api.publicholiday.HolidayRepository
import fr.anthonus.freqo.api.publicholiday.Resource
import fr.anthonus.freqo.navigation.Calendar
import fr.anthonus.freqo.navigation.Home
import fr.anthonus.freqo.navigation.Settings
import fr.anthonus.freqo.navigation.Space
import fr.anthonus.freqo.navigation.Spaces
import fr.anthonus.freqo.navigation.Tab
import fr.anthonus.freqo.navigation.Task
import fr.anthonus.freqo.navigation.appEntryProvider
import fr.anthonus.freqo.ui.theme.FreqoTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ====== TESTS ====== TODO: remove this
        val repository = HolidayRepository()

        lifecycleScope.launch {
            when (val result = repository.fetchHolidays(2026)) {
                is Resource.Success -> {
                    result.data.forEach { holiday ->
                        Log.d("RETROFIT_TEST", "${holiday.date} -> ${holiday.name}")
                    }
                }

                is Resource.Error -> {
                    Log.e("RETROFIT_TEST", "Erreur : ${result.error}")
                }
            }
        }
        // ===================

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
    var selectedTab by rememberSaveable { mutableStateOf(Tab.HOME) }

    val homeBackStack = rememberNavBackStack(Home)
    val spacesBackStack = rememberNavBackStack(Spaces)
    val calendarBackStack = rememberNavBackStack(Calendar)
    val settingsBackStack = rememberNavBackStack(Settings)

    val currentBackStack = when (selectedTab) {
        Tab.HOME -> homeBackStack
        Tab.SPACES -> spacesBackStack
        Tab.CALENDAR -> calendarBackStack
        Tab.SETTINGS -> settingsBackStack
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        bottomBar = {
            FreqoBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(
            Modifier.padding(innerPadding)
        ) {
            NavDisplay(
                backStack = currentBackStack,
                onBack = { currentBackStack.removeLastOrNull() },
                entryProvider = appEntryProvider(
                    onNavigationToSpace = { id -> currentBackStack.add(Space(id)) },
                    onNavigationToTask = { id -> currentBackStack.add(Task(id)) }
                )
            )
        }
    }
}

@Composable
fun FreqoBottomBar(
    selectedTab: Tab,
    onTabSelected: (Tab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 14.dp)
            .navigationBarsPadding(),
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 6.dp,
        shadowElevation = 6.dp,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
        ) {
            Tab.entries.forEach { tab ->
                val isSelected = selectedTab == tab

                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.18f else 1.0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onTabSelected(tab) },
                    alwaysShowLabel = fr.anthonus.freqo.data.Settings.showBottomBarLabels,
                    icon = {
                        Icon(
                            painter = painterResource(
                                if (isSelected) tab.iconSelected else tab.icon
                            ),
                            contentDescription = tab.iconDescription,
                            modifier = Modifier.scale(scale)
                        )
                    },
                    label = {
                        Text(
                            text = tab.label,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                )
            }
        }
    }
}

@Preview(group = "Freqo", name = "Main", showSystemUi = true)
@Composable
private fun FreqoPreview() {
    FreqoTheme {
        Freqo()
    }
}

@Preview(group = "Freqo", name = "NavBar", showBackground = true)
@Composable
fun FreqoBottomBarPreview() {
    FreqoTheme {
        FreqoBottomBar(
            selectedTab = Tab.HOME,
            onTabSelected = {}
        )
    }
}

