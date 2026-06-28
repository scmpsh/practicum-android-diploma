package ru.practicum.android.diploma.search.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Dimens
import ru.practicum.android.diploma.util.SalaryFormatter

@Composable
fun VacancyItem(
    vacancy: Vacancy,
    onClick: (Vacancy) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(vacancy) }
            .padding(vertical = Dimens.Padding8),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(vacancy.logoUrl)
                .addHeader("User-Agent", "Mozilla/5.0")
                .crossfade(true)
                .build(),
            contentDescription = vacancy.company,
            placeholder = painterResource(R.drawable.ic_company_placeholder_32px),
            error = painterResource(R.drawable.ic_company_placeholder_32px),
            fallback = painterResource(R.drawable.ic_company_placeholder_32px),
            modifier = Modifier
                .size(Dimens.VacancyCompanyLogoSize)
                .clip(RoundedCornerShape(Dimens.VacancyCardCornerRadius))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(Dimens.VacancyCardCornerRadius)
                ),
            contentScale = ContentScale.Inside
        )

        Spacer(modifier = Modifier.width(Dimens.Padding12))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            val titleText = if (vacancy.city.isNullOrBlank()) {
                vacancy.name
            } else {
                "${vacancy.name}, ${vacancy.city}"
            }

            Text(
                text = titleText,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = vacancy.company ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = SalaryFormatter.format(
                    context = context,
                    from = vacancy.salaryFrom,
                    to = vacancy.salaryTo,
                    currency = vacancy.salaryCurrency
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VacancyItemPreview() {
    AppTheme {
        VacancyItem(
            vacancy = Vacancy(
                id = "1",
                name = "Разработчик на С++ в команду внутренних сервисов",
                company = "Practicum",
                city = "Москва",
                salaryFrom = 100_000,
                salaryTo = 150_000,
                salaryCurrency = "RUB",
                logoUrl = null
            ),
            onClick = {}
        )
    }
}
