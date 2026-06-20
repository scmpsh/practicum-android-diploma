package ru.practicum.android.diploma.favorites.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.favorites.data.db.dao.VacancyDao
import ru.practicum.android.diploma.favorites.data.db.entity.VacancyEntity

@Database(
    entities = [VacancyEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun vacancyDao(): VacancyDao
}
