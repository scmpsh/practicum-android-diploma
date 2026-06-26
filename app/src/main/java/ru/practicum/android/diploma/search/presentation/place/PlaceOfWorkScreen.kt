package ru.practicum.android.diploma.search.presentation.place

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.Black
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.Grey
import ru.practicum.android.diploma.ui.theme.White

@Composable
fun PlaceOfWorkScreen(
    viewModel: PlaceOfWorkViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCountry: () -> Unit,
    onNavigateToRegion: () -> Unit,
    onApplyClick: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PlaceOfWorkTopBar(
            onNavigateBack = onNavigateBack
        )

        CountryMenuItem(
            country = state.country,
            onClick = onNavigateToCountry,
            onClearClick = viewModel::onCountryClearClick
        )

        PlaceOfWorkMenuItem(
            title = stringResource(R.string.place_of_work_region),
            value = state.region,
            onClick = onNavigateToRegion
        )

        Spacer(modifier = Modifier.weight(1f))

        if (state.hasSelectedPlace) {
            Button(
                onClick = {
                    onApplyClick(state.placeTitle)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = White
                )
            ) {
                Text(
                    text = stringResource(R.string.choose_button_text),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PlaceOfWorkTopBar(
    onNavigateBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(72.dp)
            .padding(start = 4.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onNavigateBack
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = Black
            )
        }

        Text(
            text = stringResource(R.string.place_of_work),
            style = MaterialTheme.typography.headlineMedium,
            color = Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun CountryMenuItem(
    country: String?,
    onClick: () -> Unit,
    onClearClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() }
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            if (country.isNullOrBlank()) {
                Text(
                    text = stringResource(R.string.place_of_work_country),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Grey,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Text(
                    text = stringResource(R.string.place_of_work_country),
                    style = MaterialTheme.typography.bodySmall,
                    color = Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = country,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (!country.isNullOrBlank()) {
            IconButton(
                onClick = onClearClick
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.search_clear_description),
                    tint = Black
                )
            }
        } else {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Black
            )
        }
    }
}

@Composable
private fun PlaceOfWorkMenuItem(
    title: String,
    value: String?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() }
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            if (value.isNullOrBlank()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Grey,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Black
        )
    }
}
