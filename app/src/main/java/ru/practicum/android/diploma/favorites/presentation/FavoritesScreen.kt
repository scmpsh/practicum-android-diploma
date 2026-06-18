package ru.practicum.android.diploma.favorites.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.favorites.presentation.models.FavoritesState
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Medium22


@Composable
fun FavoritesScreen(state: FavoritesState) {
    AppTheme { }
    Column(
        Modifier.fillMaxSize()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 19.dp, horizontal = 16.dp)
        ) {
            Text(
                text = "Избранное",
                style = Medium22
            )
        }
        FavoritesContent(state)
    }
}

@Composable
fun FavoritesContent(state: FavoritesState) {

    when (state) {

        is FavoritesState.Empty -> {
            FavoritesEmpty()
        }

        is FavoritesState.Content -> {
            FavoritesList(state.list)
        }

        is FavoritesState.Error -> {
            FavoritesError()
        }
    }
}


@Composable
fun FavoritesList(list: List<Vacancy>) {
    LazyColumn {
        items(list) { item ->
            //Здесь будет VacancyCard
            Text(item.name)
        }
    }

}

@Composable
fun FavoritesEmpty() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.img_favorites_empty),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp),
            contentScale = ContentScale.FillWidth
        )


        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.no_favorite_vacancies),
            style = Medium22
        )
//      Поднять картинку чуть выше середины
        Spacer(Modifier.weight(1.3f))
    }
}

@Composable
fun FavoritesError() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.img_favorites_error),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp),
            contentScale = ContentScale.FillWidth
        )


        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.no_vacancies_error),
            style = Medium22,
            textAlign = TextAlign.Center
        )
//      Поднять картинку чуть выше середины
        Spacer(Modifier.weight(1.3f))
    }
}

