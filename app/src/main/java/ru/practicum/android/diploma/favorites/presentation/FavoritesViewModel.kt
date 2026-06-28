package ru.practicum.android.diploma.favorites.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.favorites.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.favorites.presentation.models.FavoritesState

class FavoritesViewModel(
    private val interactor: FavoritesInteractor
) : ViewModel() {

    val favoritesState = interactor.getFavorites()
        .map { list ->
            if (list.isEmpty()) {
                FavoritesState.Empty
            } else {
                FavoritesState.Content(list)
            }
        }
        .catch { e ->
            emit(FavoritesState.Error(e.message ?: "Unknown error"))
        }
}
