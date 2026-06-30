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
import ru.practicum.android.diploma.search.presentation.filter.FilterSettingsFragment.Companion.INDUSTRY_BUNDLE_KEY
import ru.practicum.android.diploma.search.presentation.filter.FilterSettingsFragment.Companion.INDUSTRY_RESULT_KEY
import ru.practicum.android.diploma.search.presentation.industry.IndustrySelectionScreen
import ru.practicum.android.diploma.search.presentation.models.IndustriesViewModel
import ru.practicum.android.diploma.ui.theme.AppTheme

class IndustrySelectionFragment : Fragment() {

    private val viewModel: IndustriesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    IndustrySelectionScreen(
                        viewModel = viewModel,
                        onNavigateBack = {
                            findNavController().popBackStack()
                        },
                        onIndustryClick = { industry ->
                            viewModel.onIndustrySelected(industry)
                            parentFragmentManager.setFragmentResult(
                                INDUSTRY_RESULT_KEY,
                                bundleOf(INDUSTRY_BUNDLE_KEY to industry.name)
                            )
                            findNavController().popBackStack()
                        }
                    )
                }
            }
        }
    }
}
