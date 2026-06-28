package ru.practicum.android.diploma.details.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R

@Composable
fun ContactsSection(
    email: String?,
    phone: String?,
    comment: String?,
    onEmailClick: () -> Unit,
    onPhoneClick: () -> Unit
) {
    if (email.isNullOrBlank() && phone.isNullOrBlank()) return

    Column {
        Text(
            text = stringResource(R.string.vacancy_contacts_title),
            style = MaterialTheme.typography.titleMedium
        )

        if (!email.isNullOrBlank()) {
            TextButton(onClick = onEmailClick) {
                Text(text = email)
            }
        }

        if (!phone.isNullOrBlank()) {
            TextButton(onClick = onPhoneClick) {
                Text(text = phone)
            }
        }

        if (!comment.isNullOrBlank()) {
            Text(text = comment)
        }
    }
}
