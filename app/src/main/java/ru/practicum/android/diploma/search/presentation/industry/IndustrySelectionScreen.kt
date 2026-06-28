package ru.practicum.android.diploma.search.presentation.industry

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.Black
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.Grey
import ru.practicum.android.diploma.ui.theme.LightGrey
import ru.practicum.android.diploma.ui.theme.White

@Composable
fun IndustrySelectionScreen(
    initialIndustry: String?,
    onNavigateBack: () -> Unit,
    onIndustryClick: (String) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedIndustry by remember { mutableStateOf<String?>(initialIndustry) }

    val industries = listOf(
        "IT",
        "Автомобильный бизнес",
        "Административный персонал",
        "Безопасность",
        "Высший менеджмент",
        "Добыча сырья",
        "Домашний персонал",
        "Закупки",
        "Инсталляция и сервис",
        "Искусство, развлечения, массмедиа",
        "Консультирование",
        "Маркетинг, реклама, PR",
        "Медицина, фармацевтика",
        "Наука, образование",
        "Продажи",
        "Производство, сервисное обслуживание",
        "Рабочий персонал",
        "Строительство, недвижимость",
        "Транспорт, логистика",
        "Туризм, гостиницы, рестораны",
        "Управление персоналом",
        "Финансы, бухгалтерия",
        "Юристы"
    )

    val filteredIndustries = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            industries
        } else {
            industries.filter {
                it.contains(searchQuery, ignoreCase = true)
            }
        }
    }

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
            onValueChange = {
                searchQuery = it
            },
            onClearClick = {
                searchQuery = ""
            }
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            filteredIndustries.forEach { industry ->
                IndustryItem(
                    title = industry,
                    selected = selectedIndustry == industry,
                    onClick = {
                        selectedIndustry = industry
                    }
                )
            }
        }

        if (!selectedIndustry.isNullOrBlank()) {
            Button(
                onClick = {
                    onIndustryClick(selectedIndustry.orEmpty())
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
                    text = "Введите отрасль",
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
