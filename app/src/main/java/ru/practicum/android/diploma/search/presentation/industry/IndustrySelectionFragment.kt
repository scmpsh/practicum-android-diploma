package ru.practicum.android.diploma.search.presentation.industry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.inject
import ru.practicum.android.diploma.search.domain.api.FilterInteractor
import ru.practicum.android.diploma.search.presentation.filter.FilterSettingsFragment.Companion.INDUSTRY_BUNDLE_KEY
import ru.practicum.android.diploma.search.presentation.filter.FilterSettingsFragment.Companion.INDUSTRY_RESULT_KEY
import ru.practicum.android.diploma.ui.theme.AppTheme

class IndustrySelectionFragment : Fragment() {

    private val filterInteractor: FilterInteractor by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val currentIndustry = filterInteractor.getFilterSettings().industryName
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    IndustrySelectionScreen(
                        initialIndustry = currentIndustry,
                        onNavigateBack = {
                            findNavController().popBackStack()
                        },
                        onIndustryClick = { industry ->
                            parentFragmentManager.setFragmentResult(
                                INDUSTRY_RESULT_KEY,
                                bundleOf(INDUSTRY_BUNDLE_KEY to industry)
                            )
                            findNavController().popBackStack()
                        }
                    )
                }
            }
        }
    }
}
