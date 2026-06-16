package ru.practicum.android.diploma.search.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.Dimens

@Composable
fun SearchScreen(
    onNavigateToFilter: () -> Unit,
    onNavigateToVacancyDetails: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.Dimens.ScreenHorizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = stringResource(R.string.search_screen_title))
        Spacer(modifier = Modifier.height(Dimens.Dimens.Padding16))
        Button(onClick = onNavigateToFilter) {
            Text(text = stringResource(R.string.vacancies_filter))
        }
        Spacer(modifier = Modifier.height(Dimens.Dimens.Padding8))
        Button(onClick = onNavigateToVacancyDetails) {
            Text(text = stringResource(R.string.vacancy_screen_title))
        }
    }
}
