package ru.practicum.android.diploma.search.presentation.industry

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.domain.models.IndustryFilter
import ru.practicum.android.diploma.search.presentation.models.IndustriesState
import ru.practicum.android.diploma.search.presentation.models.IndustriesViewModel
import ru.practicum.android.diploma.ui.theme.Black
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.Grey
import ru.practicum.android.diploma.ui.theme.LightGrey
import ru.practicum.android.diploma.ui.theme.White

@Composable
fun IndustrySelectionScreen(
    viewModel: IndustriesViewModel,
    onNavigateBack: () -> Unit,
    onIndustryClick: (IndustryFilter) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchText.collectAsStateWithLifecycle()
    val selectedIndustry by viewModel.selectedIndustry.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        IndustrySelectionTopBar(
            onNavigateBack = onNavigateBack
        )

        IndustrySearchField(
            value = searchQuery,
            onValueChange = viewModel::onSearchTextChanged,
            onClearClick = {
                viewModel.onSearchTextChanged("")
            }
        )

        val selectedIndustryItem = when (val currentState = state) {
            is IndustriesState.Content -> {
                IndustryList(
                    industries = currentState.industries,
                    selectedIndustry = selectedIndustry,
                    onClick = viewModel::onIndustryClick,
                    modifier = Modifier.weight(1f)
                )

                currentState.industries.firstOrNull { it.name == selectedIndustry }
            }

            IndustriesState.Empty -> {
                IndustryEmptyPlaceholder(
                    modifier = Modifier.weight(1f)
                )
                null
            }

            IndustriesState.Error,
            IndustriesState.NoInternet -> {
                IndustryErrorPlaceholder(
                    modifier = Modifier.weight(1f)
                )
                null
            }

            IndustriesState.Initial,
            IndustriesState.Loading -> {
                IndustryLoadingPlaceholder(
                    modifier = Modifier.weight(1f)
                )
                null
            }
        }

        if (selectedIndustryItem != null) {
            Button(
                onClick = {
                    onIndustryClick(selectedIndustryItem)
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
private fun IndustryList(
    industries: List<IndustryFilter>,
    selectedIndustry: String?,
    onClick: (IndustryFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(industries) { industry ->
            IndustryItem(
                title = industry.name,
                selected = selectedIndustry == industry.name,
                onClick = {
                    onClick(industry)
                }
            )
        }
    }
}

@Composable
private fun IndustrySelectionTopBar(
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
            text = stringResource(R.string.industry_choice_title),
            style = MaterialTheme.typography.headlineMedium,
            color = Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun IndustrySearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(48.dp)
            .background(
                color = LightGrey,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(start = 16.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isBlank()) {
                Text(
                    text = stringResource(R.string.region_search_placeholder_text),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Grey
                )
            }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = Black
                ),
                singleLine = true,
                cursorBrush = SolidColor(Blue),
                modifier = Modifier.fillMaxWidth()
            )
        }

        IconButton(
            onClick = {
                if (value.isNotBlank()) {
                    onClearClick()
                }
            }
        ) {
            Icon(
                imageVector = if (value.isBlank()) {
                    Icons.Default.Search
                } else {
                    Icons.Default.Close
                },
                contentDescription = null,
                tint = Black
            )
        }
    }
}

@Composable
private fun IndustryLoadingPlaceholder(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun IndustryEmptyPlaceholder(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 82.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            text = stringResource(R.string.industry_not_found),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun IndustryErrorPlaceholder(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 82.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.img_regions_error),
            contentDescription = null,
            modifier = Modifier.size(
                width = 328.dp,
                height = 223.dp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.industry_server_error),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun IndustryItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = Black,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        IndustryRadioButton(
            selected = selected
        )
    }
}

@Composable
private fun IndustryRadioButton(
    selected: Boolean
) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .border(
                width = 1.dp,
                color = if (selected) Blue else Grey,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(
                        color = Blue,
                        shape = CircleShape
                    )
            )
        }
    }
}
