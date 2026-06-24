package ru.practicum.android.diploma.details.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.search.domain.models.Phone
import ru.practicum.android.diploma.ui.theme.Blue

@Composable
fun ContactsSection(
    contactName: String?,
    contactEmail: String?,
    contactPhones: List<Phone>,
    onEmailClick: (String) -> Unit,
    onPhoneClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val hasName = !contactName.isNullOrBlank()
    val hasEmail = !contactEmail.isNullOrBlank()
    val hasPhones = contactPhones.isNotEmpty()

    if (!hasName && !hasEmail && !hasPhones) return

    Column(modifier = modifier) {
        Text(
            text = "Контакты",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (hasName) {
            Text(
                text = "Контактное лицо",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = contactName.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (hasEmail) {
            Text(
                text = "E-mail",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = contactEmail.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = Blue,
                modifier = Modifier.clickable { onEmailClick(contactEmail.orEmpty()) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (hasPhones) {
            contactPhones.forEach { phone ->
                Text(
                    text = "Телефон",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = phone.formatted,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Blue,
                    modifier = Modifier.clickable { onPhoneClick(phone.formatted) }
                )
                if (!phone.comment.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Комментарий",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = phone.comment,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

