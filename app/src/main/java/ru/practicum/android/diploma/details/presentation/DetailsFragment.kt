package ru.practicum.android.diploma.details.presentation

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.AppTheme

class DetailsFragment : Fragment(R.layout.fragment_compose) {

    private val viewModel: DetailsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vacancyId = requireArguments().getString(ARG_VACANCY_ID).orEmpty()
        val logoUrl = requireArguments().getString(ARG_LOGO_URL).orEmpty()

        view.findViewById<ComposeView>(R.id.compose_view).setContent {
            AppTheme {
                DetailsScreen(
                    viewModel = viewModel,
                    vacancyId = vacancyId,
                    initialLogoUrl = logoUrl,
                    onBackClick = {
                        findNavController().popBackStack()
                    }
                )
            }
        }
    }

    companion object {
        const val ARG_VACANCY_ID = "vacancyId"
        const val ARG_LOGO_URL = "logoUrl"
    }
}
