package ru.practicum.android.diploma.search.presentation.regions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.domain.models.FilterArea
import ru.practicum.android.diploma.ui.theme.Black
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import ru.practicum.android.diploma.search.presentation.models.RegionsState

@Composable
fun RegionsSelectionScreen(
    onNavigateBack: () -> Unit,
    onCountryClick: (FilterArea) -> Unit,
    viewModel: RegionsViewModel
) {
    val state by viewModel.state.collectAsState()
    val searchText by viewModel.searchText.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        RegionsSelectionTopBar(
            onNavigateBack = onNavigateBack
        )

        SearchTextField(
            value = searchText,
            onValueChange = viewModel::onSearchTextChanged,
            onClearClick = {
                viewModel.onSearchTextChanged("")
            }
        )

        when (state) {
            is RegionsState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is RegionsState.Content -> {
                val contentState = state as RegionsState.Content

                RegionsList(
                    areas = contentState.data,
                    onClick = onCountryClick
                )
            }

            is RegionsState.Error -> {
                RegionErrorPlaceholder()
            }

            is RegionsState.Empty -> {
                RegionEmptyPlaceholder()
            }
        }
    }
}

@Composable
fun RegionsList(
    areas: List<FilterArea>,
    onClick: (FilterArea) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(areas) { area ->
            AreaItem(
                area = area,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun RegionsSelectionTopBar(
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
            text = stringResource(R.string.choose_region_title),
            style = MaterialTheme.typography.headlineMedium,
            color = Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun AreaItem(
    area: FilterArea,
    onClick: (FilterArea) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick(area) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = area.name,
            style = MaterialTheme.typography.bodyMedium,
            color = Black,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Black
        )
    }
}

@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit,
) {
    val isDark = isSystemInDarkTheme()

    val colors = rememberSearchFieldColors(isDark)

    SearchTextFieldContent(
        value = value,
        onValueChange = onValueChange,
        onClearClick = onClearClick,
        colors = colors
    )
}

private data class SearchFieldColors(
    val barColor: Color,
    val hintColor: Color,
    val contentColor: Color,
    val iconTint: Color
)

@Composable
private fun rememberSearchFieldColors(isDark: Boolean): SearchFieldColors {
    return if (isDark) {
        SearchFieldColors(
            barColor = MaterialTheme.colorScheme.onSurfaceVariant,
            hintColor = Color.White,
            contentColor = Color.Black,
            iconTint = Color.Black
        )
    } else {
        SearchFieldColors(
            barColor = MaterialTheme.colorScheme.surfaceVariant,
            hintColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f),
            contentColor = MaterialTheme.colorScheme.onSurface,
            iconTint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun SearchTextFieldContent(
    value: String,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit,
    colors: SearchFieldColors
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = colors.barColor,
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
                    text = stringResource(R.string.choose_region_search_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.hintColor,
                )
            }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = colors.contentColor,
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        IconButton(
            modifier = Modifier.size(48.dp),
            onClick = {
                if (value.isNotEmpty()) {
                    onClearClick()
                }
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
                tint = colors.iconTint,
            )
        }
    }
}

@Composable
private fun RegionEmptyPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(135.dp))

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
            text = stringResource(R.string.choose_region_not_found),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RegionErrorPlaceholder() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(82.dp))

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
            text = stringResource(R.string.choose_region_error),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}
