package fr.anthonus.freqo.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeDetailPage(id: String) {
    Column {
        Text("Details of page $id")
    }
}