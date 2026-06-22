package ru.practicum.android.diploma.search.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.presentation.components.VacancyItem
import ru.practicum.android.diploma.search.presentation.models.SearchState
import ru.practicum.android.diploma.search.presentation.models.SearchViewModel

@Composable
fun SearchScreen(
    onNavigateToFilter: () -> Unit,
    onNavigateToVacancyDetails: (String, String) -> Unit = { _, _ -> },
    viewModel: SearchViewModel = koinViewModel(),
) {
    var searchQuery by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        SearchToolbar(
            onFilterClick = onNavigateToFilter,
        )

        Spacer(modifier = Modifier.height(24.dp))

        SearchTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.searchDebounce(it)
            },
            onClearClick = {
                searchQuery = ""
                viewModel.searchDebounce("")
            },
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            when (val currentState = state) {
                SearchState.Initial -> {
                    if (searchQuery.isBlank()) {
                        Image(
                            painter = painterResource(R.drawable.ic_search_placeholder),
                            contentDescription = null,
                            modifier = Modifier.size(
                                width = 328.dp,
                                height = 223.dp,
                            ),
                        )
                    }
                }

                SearchState.Loading -> {
                    CircularProgressIndicator()
                }

                SearchState.Empty -> {
                    SearchEmptyPlaceholder()
                }

                SearchState.NoInternet -> {
                    SearchNoInternetPlaceholder()
                }

                SearchState.Error -> {
                    SearchErrorPlaceholder()
                }

                is SearchState.Content -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(
                            items = currentState.vacancies,
                            key = { _, vacancy -> vacancy.id }
                        ) { index, vacancy ->

                            VacancyItem(
                                vacancy = vacancy,
                                onClick = {
                                    onNavigateToVacancyDetails(
                                        it.id,
                                        it.logoUrl.orEmpty()
                                    )
                                }
                            )

                            if (index >= currentState.vacancies.lastIndex - 2) {
                                viewModel.onLastItemReached()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchEmptyPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Таких вакансий нет",
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 4.dp
                ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.height(72.dp))

        Image(
            painter = painterResource(R.drawable.il_empty_search_result),
            contentDescription = null,
            modifier = Modifier.size(
                width = 328.dp,
                height = 223.dp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.no_vacancies_error),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SearchNoInternetPlaceholder() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(82.dp))

        Image(
            painter = painterResource(R.drawable.il_no_internet),
            contentDescription = null,
            modifier = Modifier.size(
                width = 328.dp,
                height = 223.dp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.no_internet_error),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 22.sp,
                lineHeight = 26.sp
            ),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SearchErrorPlaceholder() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(82.dp))

        Image(
            painter = painterResource(R.drawable.il_empty_search_result),
            contentDescription = null,
            modifier = Modifier.size(
                width = 328.dp,
                height = 223.dp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.no_vacancies_error),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SearchToolbar(
    onFilterClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.search_screen_title),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        IconButton(
            onClick = onFilterClick,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_filter),
                contentDescription = stringResource(R.string.filter),
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
private fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(
                start = 8.dp,
                top = 4.dp,
                end = 4.dp,
                bottom = 4.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            if (value.isEmpty()) {
                Text(
                    text = stringResource(R.string.search_input_hint),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f),
                )
            }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        IconButton(
            modifier = Modifier.size(48.dp),
            onClick = {
                if (value.isEmpty()) {
                    return@IconButton
                }
                onClearClick()
            },
        ) {
            Icon(
                painter = painterResource(
                    if (value.isEmpty()) {
                        R.drawable.ic_search
                    } else {
                        R.drawable.ic_close
                    },
                ),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
