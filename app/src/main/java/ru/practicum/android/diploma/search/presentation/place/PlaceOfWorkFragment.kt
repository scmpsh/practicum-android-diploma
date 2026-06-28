package ru.practicum.android.diploma.search.presentation.place

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
import ru.practicum.android.diploma.search.presentation.filter.FilterSettingsFragment.Companion.PLACE_OF_WORK_BUNDLE_KEY
import ru.practicum.android.diploma.search.presentation.filter.FilterSettingsFragment.Companion.PLACE_OF_WORK_RESULT_KEY
import ru.practicum.android.diploma.ui.theme.AppTheme

class PlaceOfWorkFragment : Fragment() {

    private val viewModel: PlaceOfWorkViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentFragmentManager.setFragmentResultListener(
            COUNTRY_RESULT_KEY,
            this
        ) { _, bundle ->
            val country = bundle.getString(COUNTRY_BUNDLE_KEY).orEmpty()
            if (country.isNotBlank()) {
                viewModel.onCountrySelected(country)
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
                    PlaceOfWorkScreen(
                        viewModel = viewModel,
                        onNavigateBack = {
                            findNavController().popBackStack()
                        },
                        onNavigateToCountry = {
                            findNavController().navigate(
                                R.id.action_placeOfWorkFragment_to_countrySelectionFragment
                            )
                        },
                        onNavigateToRegion = {
                            // Экран региона будет отдельной задачей/следующим шагом.
                        },
                        onApplyClick = { placeTitle ->
                            parentFragmentManager.setFragmentResult(
                                PLACE_OF_WORK_RESULT_KEY,
                                bundleOf(PLACE_OF_WORK_BUNDLE_KEY to placeTitle)
                            )
                            findNavController().popBackStack()
                        }
                    )
                }
            }
        }
    }

    companion object {
        const val COUNTRY_RESULT_KEY = "countryResultKey"
        const val COUNTRY_BUNDLE_KEY = "countryBundleKey"
    }
}
