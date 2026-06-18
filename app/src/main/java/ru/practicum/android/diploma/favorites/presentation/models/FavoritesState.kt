package ru.practicum.android.diploma.favorites.presentation.models

import androidx.compose.runtime.Immutable
import ru.practicum.android.diploma.search.domain.models.Vacancy

@Immutable
sealed interface FavoritesState {
    @Immutable
    data class Content(
        val list: List<Vacancy>
    ) : FavoritesState

    @Immutable
    object Empty : FavoritesState

    @Immutable
    data class Error(
        val message: String
    ) : FavoritesState
}
