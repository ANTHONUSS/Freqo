package fr.anthonus.freqo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomePage(
    onSpaceClick: (Int) -> Unit
) {
    Column {
        Button(onClick = { onSpaceClick(1) }) {
            Text("Go to Space 1")
        }
        Button(onClick = { onSpaceClick(2) }) {
            Text("Go to Space 2")
        }
    }
}