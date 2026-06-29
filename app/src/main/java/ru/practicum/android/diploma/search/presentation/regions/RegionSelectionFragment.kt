package ru.practicum.android.diploma.search.presentation.regions

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
import ru.practicum.android.diploma.search.presentation.place.PlaceOfWorkFragment.Companion.REGION_BUNDLE_KEY
import ru.practicum.android.diploma.search.presentation.place.PlaceOfWorkFragment.Companion.REGION_RESULT_KEY
import ru.practicum.android.diploma.ui.theme.AppTheme

class RegionSelectionFragment : Fragment() {
    private val viewModel: RegionsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    RegionsSelectionScreen(
                        viewModel = viewModel,
                        onNavigateBack = {
                            findNavController().navigateUp()
                        },
                        onCountryClick = { area ->
                            viewModel.onRegionClick(area)
                            parentFragmentManager.setFragmentResult(
                                REGION_RESULT_KEY,
                                bundleOf(REGION_BUNDLE_KEY to area.name)
                            )
                            findNavController().navigateUp()
                        }
                    )
                }
            }
        }
    }
}
