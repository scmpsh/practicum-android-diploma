package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.details.presentation.DetailsViewModel

val detailsModule = module {
    viewModel {
        DetailsViewModel(
            interactor = get(),
            favoritesInteractor = get(),
            context = androidContext()
        )
    }
}

