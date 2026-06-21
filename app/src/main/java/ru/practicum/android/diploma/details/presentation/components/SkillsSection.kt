package ru.practicum.android.diploma.details.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.FlowRow

@Composable
fun SkillsSection(skills: List<String>) {
    if (skills.isEmpty()) return

    Column {
        Text(
            text = "Ключевые навыки",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            skills.forEach { skill ->
                AssistChip(
                    onClick = {},
                    label = { Text(skill) }
                )
            }
        }
    }
}
