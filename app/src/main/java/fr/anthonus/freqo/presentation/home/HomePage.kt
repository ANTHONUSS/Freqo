package fr.anthonus.freqo.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomePage(
    onItemClick: (id: String) -> Unit,
) {
    Column {
        Button(
            onClick = {
                onItemClick("1")
            },
        ) {
            Text("Go to details 1!")
        }

        Button(
            onClick = {
                onItemClick("2")
            },
        ) {
            Text("Go to details 2!")
        }
    }
}