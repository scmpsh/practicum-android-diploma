package ru.practicum.android.diploma.details.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ContactsSection(contacts: String?) {
    if (contacts.isNullOrBlank()) return

    Column {
        Text(
            text = "Контакты",
            style = MaterialTheme.typography.titleMedium
        )

        Text(text = contacts)
    }
}
