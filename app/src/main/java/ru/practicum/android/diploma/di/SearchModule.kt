package ru.practicum.android.diploma.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.search.data.AreaRepositoryImpl
import ru.practicum.android.diploma.search.data.FilterPreferences
import ru.practicum.android.diploma.search.data.FilterRepositoryImpl
import ru.practicum.android.diploma.search.data.IndustriesRepositoryImpl
import ru.practicum.android.diploma.search.data.IndustryFilterMapper
import ru.practicum.android.diploma.search.data.SearchRepositoryImpl
import ru.practicum.android.diploma.search.data.VacancyDetailRepositoryImpl
import ru.practicum.android.diploma.search.data.mappers.VacancyDetailMapper
import ru.practicum.android.diploma.search.data.mappers.VacancyMapper
import ru.practicum.android.diploma.search.data.network.ConnectionChecker
import ru.practicum.android.diploma.search.data.network.ConnectionCheckerImpl
import ru.practicum.android.diploma.search.data.network.HeadHunterApi
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.search.domain.api.AreaInteractor
import ru.practicum.android.diploma.search.domain.api.AreaRepository
import ru.practicum.android.diploma.search.domain.api.FilterInteractor
import ru.practicum.android.diploma.search.domain.api.FilterRepository
import ru.practicum.android.diploma.search.domain.api.IndustriesInteractor
import ru.practicum.android.diploma.search.domain.api.IndustriesRepository
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.api.VacancyDetailRepository
import ru.practicum.android.diploma.search.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.search.domain.impl.AreaInteractorImpl
import ru.practicum.android.diploma.search.domain.impl.FilterInteractorImpl
import ru.practicum.android.diploma.search.domain.impl.IndustriesInteractorImpl
import ru.practicum.android.diploma.search.domain.impl.SearchInteractorImpl
import ru.practicum.android.diploma.search.domain.impl.VacancyDetailsInteractorImpl
import ru.practicum.android.diploma.search.presentation.country.CountryViewModel
import ru.practicum.android.diploma.search.presentation.filter.FilterSettingsViewModel
import ru.practicum.android.diploma.search.presentation.models.IndustriesViewModel
import ru.practicum.android.diploma.search.presentation.models.SearchViewModel
import ru.practicum.android.diploma.search.presentation.place.PlaceOfWorkViewModel
import ru.practicum.android.diploma.search.presentation.regions.RegionsViewModel

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

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            "filter_prefs",
            Context.MODE_PRIVATE
        )
    }

    single {
        FilterPreferences(get())
    }

    factory { VacancyMapper() }
    factory { VacancyDetailMapper() }
    factory { IndustryFilterMapper() }

    single<SearchRepository> {
        SearchRepositoryImpl(get(), get())
    }

    single<VacancyDetailRepository> {
        VacancyDetailRepositoryImpl(get(), get())
    }

    single<AreaRepository> {
        AreaRepositoryImpl(get())
    }

    single<IndustriesRepository> {
        IndustriesRepositoryImpl(get(), get())
    }

    single<FilterRepository> {
        FilterRepositoryImpl(get())
    }

    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    single<VacancyDetailsInteractor> {
        VacancyDetailsInteractorImpl(get())
    }

    single<AreaInteractor> {
        AreaInteractorImpl(get())
    }

    single<IndustriesInteractor> {
        IndustriesInteractorImpl(get())
    }

    single<FilterInteractor> {
        FilterInteractorImpl(get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        IndustriesViewModel(get())
    }

    viewModel {
        FilterSettingsViewModel(get())
    }

    viewModel {
        PlaceOfWorkViewModel(get())
    }

    viewModel {
        CountryViewModel(get(), get())
    }

    viewModel {
        RegionsViewModel(get(), get())
    }
}
