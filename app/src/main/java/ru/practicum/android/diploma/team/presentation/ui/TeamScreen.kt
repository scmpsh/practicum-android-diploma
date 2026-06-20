package ru.practicum.android.diploma.team.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R

@Composable

fun TeamScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
            .padding(horizontal = 14.dp),
    ) {
        Spacer(modifier = Modifier.height(58.dp))

        Text(
            text = stringResource(R.string.team),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(62.dp))

        Text(
            text = stringResource(R.string.team_screen_subtitle),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(36.dp))

        TeamMemberName(
            name = stringResource(R.string.team_member_1),
        )

        TeamMemberName(
            name = stringResource(R.string.team_member_2),
        )

        TeamMemberName(
            name = stringResource(R.string.team_member_3),
        )

        TeamMemberName(
            name = stringResource(R.string.team_member_4),
        )

        TeamMemberName(
            name = stringResource(R.string.team_member_5),
        )
    }
}

@Composable
private fun TeamMemberName(
    name: String,
) {
    Text(
        text = name,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground,
    )

    Spacer(modifier = Modifier.height(20.dp))
}
