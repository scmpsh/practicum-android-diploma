package ru.practicum.android.diploma.search.presentation.filter

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.Black
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.Grey
import ru.practicum.android.diploma.ui.theme.LightGrey
import ru.practicum.android.diploma.ui.theme.White

@Composable
fun FilterSettingsScreen(
    viewModel: FilterSettingsViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToPlaceOfWork: () -> Unit,
    onNavigateToIndustry: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    FilterSettingsContent(
        state = state,
        onNavigateBack = onNavigateBack,
        onNavigateToPlaceOfWork = onNavigateToPlaceOfWork,
        onNavigateToIndustry = onNavigateToIndustry,
        onSalaryChange = viewModel::onSalaryChange,
        onSalaryClearClick = viewModel::onSalaryClearClick,
        onDoNotShowWithoutSalaryChange = viewModel::onDoNotShowWithoutSalaryChange,
        onResetClick = viewModel::onResetClick
    )
}

@Composable
private fun FilterSettingsContent(
    state: FilterSettingsState,
    onNavigateBack: () -> Unit,
    onNavigateToPlaceOfWork: () -> Unit,
    onNavigateToIndustry: () -> Unit,
    onSalaryChange: (String) -> Unit,
    onSalaryClearClick: () -> Unit,
    onDoNotShowWithoutSalaryChange: (Boolean) -> Unit,
    onResetClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        FilterTopBar(
            onNavigateBack = onNavigateBack
        )

        FilterMenuItem(
            title = stringResource(R.string.filter_settings_country_title),
            value = state.placeOfWork,
            onClick = onNavigateToPlaceOfWork
        )

        FilterMenuItem(
            title = stringResource(R.string.specialization_title),
            value = state.industry,
            onClick = onNavigateToIndustry
        )

        SalaryInput(
            salary = state.salary,
            onSalaryChange = onSalaryChange,
            onClearClick = onSalaryClearClick
        )

        DoNotShowWithoutSalaryCheckbox(
            checked = state.doNotShowWithoutSalary,
            onCheckedChange = onDoNotShowWithoutSalaryChange
        )

        Spacer(modifier = Modifier.weight(1f))

        if (state.hasAnyFilter) {
            Button(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = White
                )
            ) {
                Text(
                    text = stringResource(R.string.apply_button_text),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onResetClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.cancel_button_text),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FilterTopBar(
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
            text = stringResource(R.string.filter_settings_title),
            style = MaterialTheme.typography.headlineMedium,
            color = Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FilterMenuItem(
    title: String,
    value: String?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = Grey,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (!value.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall,
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

@Composable
private fun SalaryInput(
    salary: String,
    onSalaryChange: (String) -> Unit,
    onClearClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp)
            .height(60.dp)
            .background(
                color = LightGrey,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(start = 16.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.filter_settings_salary_title),
                style = MaterialTheme.typography.bodySmall,
                color = Grey,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                if (salary.isBlank()) {
                    Text(
                        text = stringResource(R.string.input_amount_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Grey
                    )
                }

                BasicTextField(
                    value = salary,
                    onValueChange = onSalaryChange,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = Black
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    cursorBrush = SolidColor(Blue),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (salary.isNotBlank()) {
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
            Spacer(modifier = Modifier.size(48.dp))
        }
    }
}

@Composable
private fun DoNotShowWithoutSalaryCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onCheckedChange(!checked) }
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.do_not_show_without_salary_title),
            style = MaterialTheme.typography.bodyMedium,
            color = Black,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    color = if (checked) Blue else MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(2.dp)
                )
                .border(
                    width = 2.dp,
                    color = Blue,
                    shape = RoundedCornerShape(2.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
