package ru.practicum.android.diploma.search.presentation.industry

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import ru.practicum.android.diploma.ui.theme.*
import java.text.Collator
import java.util.Locale

@Composable
fun IndustrySelectionScreen(
    viewModel: IndustriesViewModel,
    initialIndustryId: String?,
    onNavigateBack: () -> Unit,
    onIndustryClick: (IndustryFilter) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }
    var selectedIndustryId by remember(initialIndustryId) {
        mutableStateOf(initialIndustryId)
    }

    LaunchedEffect(Unit) {
        viewModel.loadIndustries()
    }

    val russianCollator = remember {
        Collator.getInstance(Locale("ru", "RU"))
    }

    val industries = remember(state) {
        (state as? IndustriesState.Content)
            ?.industries
            .orEmpty()
            .sortedWith { a, b -> russianCollator.compare(a.name, b.name) }
    }

    val filteredIndustries = remember(searchQuery, industries) {
        val query = searchQuery.trim().lowercase()
        if (query.isBlank()) industries
        else industries.filter { it.matchesQuery(query) }
    }

    val selectedIndustry = industries.firstOrNull {
        it.id.toString() == selectedIndustryId
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        IndustrySelectionTopBar(onNavigateBack)

        IndustrySearchField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            onClearClick = { searchQuery = "" }
        )

        Box(modifier = Modifier.weight(1f)) {

            when (state) {

                is IndustriesState.Content -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(filteredIndustries, key = { it.id }) { industry ->
                            IndustryItem(
                                title = industry.name,
                                selected = selectedIndustryId == industry.id.toString(),
                                onClick = {
                                    val id = industry.id.toString()
                                    selectedIndustryId =
                                        if (selectedIndustryId == id) null else id
                                }
                            )
                        }
                    }
                }

                IndustriesState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                IndustriesState.Empty -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 72.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.il_regions_error),
                            contentDescription = null,
                            modifier = Modifier.size(328.dp, 223.dp)
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

                IndustriesState.Error,
                IndustriesState.NoInternet -> {
                    IndustryErrorPlaceholder()
                }

                IndustriesState.Initial -> {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        }

        if (selectedIndustry != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(start = 17.dp, end = 17.dp, bottom = 24.dp)
            ) {
                Button(
                    onClick = { onIndustryClick(selectedIndustry) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(59.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue,
                        contentColor = White
                    )
                ) {
                    Text(text = stringResource(R.string.choose_button_text))
                }
            }
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
        IconButton(onClick = onNavigateBack) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Text(
            text = stringResource(R.string.industry_choice_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
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
    val isDarkTheme = isSystemInDarkTheme()

    val searchFieldBackground = if (isDarkTheme) Grey else LightGrey
    val searchFieldTextColor = if (isDarkTheme) White else Black
    val searchFieldHintColor = if (isDarkTheme) White else Grey

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp)
            .background(searchFieldBackground, RoundedCornerShape(12.dp))
            .padding(start = 16.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {

            if (value.isBlank()) {
                Text(
                    text = stringResource(R.string.industry_search_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = searchFieldHintColor
                )
            }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = searchFieldTextColor
                ),
                singleLine = true,
                cursorBrush = SolidColor(Blue),
                modifier = Modifier.fillMaxWidth()
            )
        }

        IconButton(
            modifier = Modifier.size(48.dp),
            onClick = {
                if (value.isNotBlank()) onClearClick()
            }
        ) {
            Icon(
                imageVector = if (value.isBlank())
                    Icons.Default.Search
                else
                    Icons.Default.Close,
                contentDescription = null,
                tint = Black
            )
        }
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
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onBackground
        )

        IndustryRadioButton(selected)
    }
}

@Composable
private fun IndustryRadioButton(selected: Boolean) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .border(1.dp, if (selected) Blue else Grey, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Blue, CircleShape)
            )
        }
    }
}

@Composable
private fun IndustryErrorPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.il_regions_error),
            contentDescription = null,
            modifier = Modifier.size(328.dp, 223.dp)
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

private fun IndustryFilter.matchesQuery(query: String): Boolean {
    val normalizedName = name.lowercase()

    if (normalizedName.contains(query)) return true

    val isItQuery = query == IT_QUERY || query == IT_QUERY_CYRILLIC

    return isItQuery && (
        normalizedName.contains("информацион") ||
            normalizedName.contains("интернет") ||
            normalizedName.contains("программ") ||
            normalizedName.contains("software")
        )
}

private const val IT_QUERY = "it"
private const val IT_QUERY_CYRILLIC = "ит"
