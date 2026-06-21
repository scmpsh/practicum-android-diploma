package ru.practicum.android.diploma.details.presentation

import android.text.Html
import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.practicum.android.diploma.details.presentation.components.SkillsSection
import ru.practicum.android.diploma.details.presentation.components.ContactsSection

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    vacancyId: String
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(vacancyId) {
        viewModel.loadVacancy(vacancyId)
    }

    Scaffold(
        topBar = {
            DetailsTopBar(
                onBackClick = { },
                onShareClick = { },
                onFavoriteClick = { }
            )
        }
    ) { padding ->

        when (val s = state) {

            is DetailsState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is DetailsState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Ошибка загрузки")
                }
            }

            is DetailsState.Content -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {

                    Text(
                        text = s.title,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(text = s.company)
                    Text(text = s.location)

                    Spacer(Modifier.height(16.dp))

                    AndroidView(
                        factory = { TextView(it) },
                        update = {
                            it.text = Html.fromHtml(
                                s.descriptionHtml.orEmpty(),
                                Html.FROM_HTML_MODE_LEGACY
                            )
                        }
                    )

                    Spacer(Modifier.height(16.dp))

                    SkillsSection(s.skills)

                    Spacer(Modifier.height(16.dp))

                    ContactsSection(s.contacts)
                }
            }
        }
    }
}
