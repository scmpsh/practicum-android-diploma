package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.search.data.IndustriesRepositoryImpl
import ru.practicum.android.diploma.search.data.IndustryFilterMapper
import ru.practicum.android.diploma.search.data.SearchRepositoryImpl
import ru.practicum.android.diploma.search.data.VacancyDetailMapper
import ru.practicum.android.diploma.search.data.VacancyDetailRepositoryImpl
import ru.practicum.android.diploma.search.data.VacancyMapper
import ru.practicum.android.diploma.search.data.network.ConnectionChecker
import ru.practicum.android.diploma.search.data.network.ConnectionCheckerImpl
import ru.practicum.android.diploma.search.data.network.HeadHunterApi
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.search.domain.api.IndustriesInteractor
import ru.practicum.android.diploma.search.domain.api.IndustriesRepository
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.api.VacancyDetailRepository
import ru.practicum.android.diploma.search.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.search.domain.impl.IndustriesInteractorImpl
import ru.practicum.android.diploma.search.domain.impl.SearchInteractorImpl
import ru.practicum.android.diploma.search.domain.impl.VacancyDetailsInteractorImpl
import ru.practicum.android.diploma.search.presentation.models.IndustriesViewModel
import ru.practicum.android.diploma.search.presentation.models.SearchViewModel
import ru.practicum.android.diploma.search.presentation.filter.FilterSettingsViewModel
import ru.practicum.android.diploma.search.presentation.place.PlaceOfWorkViewModel

const val HEAD_HUNTER_BASE_URL = "https://android-diploma.education-services.ru"

val searchModule = module {

    single<HeadHunterApi> {
        Retrofit.Builder()
            .baseUrl(HEAD_HUNTER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HeadHunterApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }

    single<ConnectionChecker> {
        ConnectionCheckerImpl(androidContext())
    }

    factory { VacancyMapper() }
    factory { VacancyDetailMapper() }
    factory { IndustryFilterMapper() }

    single<VacancyDetailRepository> {
        VacancyDetailRepositoryImpl(get(), get())
    }

    single<IndustriesRepository> {
        IndustriesRepositoryImpl(get(), get())
    }

    single<SearchRepository> {
        SearchRepositoryImpl(get(), get())
    }

    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    single<VacancyDetailsInteractor> {
        VacancyDetailsInteractorImpl(get())
    }

    single<IndustriesInteractor> {
        IndustriesInteractorImpl(get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        IndustriesViewModel(get())
    }

    viewModel {
        FilterSettingsViewModel()
    }

    viewModel {
        PlaceOfWorkViewModel()
    }
}
