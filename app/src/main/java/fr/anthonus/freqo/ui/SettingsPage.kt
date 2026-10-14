package fr.anthonus.freqo.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import fr.anthonus.freqo.data.Settings

@Composable
fun SettingsPage() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(
                value = Settings.showBottomBarLabels,
                onValueChange = { Settings.showBottomBarLabels = it },
                role = Role.Checkbox
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = Settings.showBottomBarLabels,
            onCheckedChange = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Afficher les labels de la barre de navigation",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}