package ru.practicum.android.diploma.details.presentation

import android.text.Html
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.details.presentation.components.ContactsSection
import ru.practicum.android.diploma.details.presentation.components.SkillsSection

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    vacancyId: String,
    initialLogoUrl: String,
    onBackClick: () -> Unit,
    onShareClick: (DetailsState.Content) -> Unit,
    onEmailClick: (DetailsState.Content) -> Unit,
    onPhoneClick: (DetailsState.Content) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(vacancyId) {
        viewModel.loadVacancy(vacancyId)
    }

    Scaffold(
        topBar = {
            val contentState = state as? DetailsState.Content
            DetailsTopBar(
                isFavorite = contentState?.isFavorite ?: false,
                onBackClick = onBackClick,
                onShareClick = {
                    contentState?.let(onShareClick)
                },
                onFavoriteClick = { viewModel.onFavoriteClick() }
            )
        }
    ) { padding ->

        when (val currentState = state) {
            DetailsState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            DetailsState.NoInternet -> {
                DetailsPlaceholder(
                    imageRes = R.drawable.il_no_internet,
                    text = "Нет интернета",
                    modifier = Modifier.padding(padding)
                )
            }

            DetailsState.Error -> {
                DetailsPlaceholder(
                    imageRes = R.drawable.il_empty_search_result,
                    text = "Не удалось получить\nинформацию о вакансии",
                    modifier = Modifier.padding(padding)
                )
            }

            is DetailsState.Content -> {
                VacancyDetailsContent(
                    state = currentState,
                    initialLogoUrl = initialLogoUrl,
                    onEmailClick = onEmailClick,
                    onPhoneClick = onPhoneClick,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
private fun DetailsPlaceholder(
    imageRes: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                modifier = Modifier.size(
                    width = 328.dp,
                    height = 223.dp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun VacancyDetailsContent(
    state: DetailsState.Content,
    initialLogoUrl: String,
    onEmailClick: (DetailsState.Content) -> Unit,
    onPhoneClick: (DetailsState.Content) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = state.title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = state.salary,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        CompanyCard(
            company = state.company,
            location = state.location,
            logoUrl = state.logoUrl.orEmpty().ifBlank { initialLogoUrl }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Требуемый опыт",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = state.experience,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = listOf(state.employment, state.schedule)
                .filter { it.isNotBlank() }
                .joinToString(", "),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        HtmlDescription(
            html = state.descriptionHtml
        )

        if (state.skills.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))

            SkillsSection(
                skills = state.skills
            )
        }

        if (!state.contactEmail.isNullOrBlank() || !state.contactPhone.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(24.dp))

            ContactsSection(
                email = state.contactEmail,
                phone = state.contactPhone,
                comment = state.contactComment,
                onEmailClick = { onEmailClick(state) },
                onPhoneClick = { onPhoneClick(state) }
            )
        }
    }
}

@Composable
private fun CompanyCard(
    company: String,
    location: String,
    logoUrl: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompanyLogo(
            company = company,
            logoUrl = logoUrl
        )

        Spacer(modifier = Modifier.size(12.dp))

        Column {
            Text(
                text = company,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = location,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun CompanyLogo(
    company: String,
    logoUrl: String
) {
    val context = LocalContext.current

    if (logoUrl.isBlank()) {
        Image(
            painter = painterResource(R.drawable.ic_company_placeholder_32px),
            contentDescription = company,
            modifier = Modifier.size(48.dp),
            contentScale = ContentScale.Fit
        )
        return
    }

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(context)
            .data(logoUrl)
            .addHeader("User-Agent", "Mozilla/5.0")
            .crossfade(true)
            .build(),
        contentDescription = company,
        modifier = Modifier.size(48.dp),
        contentScale = ContentScale.Fit,
        loading = {
            Box(
                modifier = Modifier.size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        error = {
            Image(
                painter = painterResource(R.drawable.ic_company_placeholder_32px),
                contentDescription = company,
                modifier = Modifier.size(48.dp),
                contentScale = ContentScale.Fit
            )
        },
        success = {
            SubcomposeAsyncImageContent()
        }
    )
}

@Composable
private fun HtmlDescription(
    html: String
) {
    val textColor = MaterialTheme.colorScheme.onBackground.toArgb()

    AndroidView(
        factory = { context ->
            TextView(context).apply {
                textSize = 16f
                setTextColor(textColor)
                includeFontPadding = true
            }
        },
        update = { textView ->
            textView.setTextColor(textColor)
            textView.text = Html.fromHtml(
                html,
                Html.FROM_HTML_MODE_LEGACY
            )
        }
    )
}
