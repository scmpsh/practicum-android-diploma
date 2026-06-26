package ru.practicum.android.diploma.search.presentation.country

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.search.presentation.place.PlaceOfWorkFragment
import ru.practicum.android.diploma.ui.theme.AppTheme

class CountrySelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    CountrySelectionScreen(
                        onNavigateBack = {
                            findNavController().popBackStack()
                        },
                        onCountryClick = { country ->
                            parentFragmentManager.setFragmentResult(
                                PlaceOfWorkFragment.COUNTRY_RESULT_KEY,
                                bundleOf(PlaceOfWorkFragment.COUNTRY_BUNDLE_KEY to country)
                            )
                            findNavController().popBackStack()
                        }
                    )
                }
            }
        }
    }
}
