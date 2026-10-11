package fr.anthonus.freqo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SpacePage(
    id: Int,
    onTaskClick: (Int) -> Unit
) {
    Column {
        Text("Space #$id")

        if (id == 1) {
            Button(onClick = { onTaskClick(1) }) {
                Text("Go to Task 1")
            }
            Button(onClick = { onTaskClick(2) }) {
                Text("Go to Task 2")
            }
        }
    }
}