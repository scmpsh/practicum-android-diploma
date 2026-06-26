package ru.practicum.android.diploma.search.presentation.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.AppTheme

class FilterSettingsFragment : Fragment() {

    private val viewModel: FilterSettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentFragmentManager.setFragmentResultListener(
            PLACE_OF_WORK_RESULT_KEY,
            this
        ) { _, bundle ->
            val placeOfWork = bundle.getString(PLACE_OF_WORK_BUNDLE_KEY).orEmpty()
            if (placeOfWork.isNotBlank()) {
                viewModel.onPlaceOfWorkSelected(placeOfWork)
            }
        }

        parentFragmentManager.setFragmentResultListener(
            INDUSTRY_RESULT_KEY,
            this
        ) { _, bundle ->
            val industry = bundle.getString(INDUSTRY_BUNDLE_KEY).orEmpty()
            if (industry.isNotBlank()) {
                viewModel.onIndustrySelected(industry)
            }
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
                    FilterSettingsScreen(
                        viewModel = viewModel,
                        onNavigateBack = {
                            findNavController().popBackStack()
                        },
                        onNavigateToPlaceOfWork = {
                            findNavController().navigate(
                                R.id.action_filterSettingsFragment_to_placeOfWorkFragment
                            )
                        },
                        onNavigateToIndustry = {
                            findNavController().navigate(
                                R.id.action_filterSettingsFragment_to_industrySelectionFragment
                            )
                        }
                    )
                }
            }
        }
    }

    companion object {
        const val PLACE_OF_WORK_RESULT_KEY = "placeOfWorkResultKey"
        const val PLACE_OF_WORK_BUNDLE_KEY = "placeOfWorkBundleKey"

        const val INDUSTRY_RESULT_KEY = "industryResultKey"
        const val INDUSTRY_BUNDLE_KEY = "industryBundleKey"
    }
}
