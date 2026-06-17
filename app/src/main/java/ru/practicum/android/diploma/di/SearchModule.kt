package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.search.data.SearchRepositoryImpl
import ru.practicum.android.diploma.search.data.VacancyMapper
import ru.practicum.android.diploma.search.data.network.ConnectionChecker
import ru.practicum.android.diploma.search.data.network.ConnectionCheckerImpl
import ru.practicum.android.diploma.search.data.network.HeadHunterApi
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.impl.SearchInteractorImpl

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

    single<SearchRepository> {
        SearchRepositoryImpl(get(), get())
    }

    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }
}
