package fr.anthonus.freqo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TaskPage(id: Int) {
    Column {
        Text("Task #$id")
    }
}