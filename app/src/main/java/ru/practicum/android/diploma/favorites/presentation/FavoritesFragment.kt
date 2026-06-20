package ru.practicum.android.diploma.favorites.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.favorites.presentation.models.FavoritesState
import ru.practicum.android.diploma.ui.theme.AppTheme

class FavoritesFragment : Fragment() {
    private val favoritesViewModel: FavoritesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    val state = favoritesViewModel.favoritesState.collectAsState(FavoritesState.Empty).value

                    FavoritesScreen(
                        state = state,
                        onNavigateToDetails = { vacancyId ->
                            val bundle = Bundle().apply {
                                putString(VACANCY_ID, vacancyId)
                            }
                            // В detailes фрагмент получить bundle VacancyId, и по нему сделать запрос Favorites БД
                            findNavController().navigate(
                                R.id.action_favoritesFragment_to_vacancyDetailsFragment,
                                bundle
                            )
                        })
                }
            }
        }
    }

    companion object {
        const val VACANCY_ID = "vacancyId"
    }
}
