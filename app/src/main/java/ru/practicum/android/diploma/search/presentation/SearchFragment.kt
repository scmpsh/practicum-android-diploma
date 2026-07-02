package ru.practicum.android.diploma.search.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.details.presentation.DetailsFragment
import ru.practicum.android.diploma.search.presentation.filter.FilterSettingsFragment
import ru.practicum.android.diploma.search.presentation.SearchViewModel
import ru.practicum.android.diploma.ui.theme.AppTheme

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentFragmentManager.setFragmentResultListener(
            FilterSettingsFragment.FILTER_APPLIED_RESULT_KEY,
            this
        ) { _, _ ->
            viewModel.onFilterApplied()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    SearchScreen(
                        onNavigateToFilter = {
                            findNavController().navigate(
                                R.id.action_searchFragment_to_filterSettingsFragment,
                            )
                        },
                        onNavigateToVacancyDetails = { vacancyId, logoUrl ->
                            findNavController().navigate(
                                R.id.action_searchFragment_to_vacancyDetailsFragment,
                                bundleOf(
                                    DetailsFragment.ARG_VACANCY_ID to vacancyId,
                                    DetailsFragment.ARG_LOGO_URL to logoUrl
                                )
                            )
                        },
                        viewModel = viewModel,
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onFilterApplied()
    }
}
