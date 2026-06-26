package ru.practicum.android.diploma.app

import android.app.Application
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.di.detailsModule
import ru.practicum.android.diploma.di.favoritesModule
import ru.practicum.android.diploma.di.presentationModule
import ru.practicum.android.diploma.di.searchModule
import ru.practicum.android.diploma.di.teamModule
import ru.practicum.android.diploma.search.data.filterstoragetemp.FilterStorage

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                searchModule,
                favoritesModule,
                teamModule,
                presentationModule,
                detailsModule,
            )
        }

// TEMPORARY UNTIL FILTER IS READY
        val filterStorage: FilterStorage = getKoin().get()
        filterStorage.clearCountryId()
    }
}
