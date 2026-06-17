package ru.practicum.android.diploma.favorites.data.db.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.data.db.entity.VacancyEntity

@Dao
interface VacancyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyEntity): Long

    @Delete
    suspend fun deleteVacancy(vacancy: VacancyEntity): Int

    @Query("DELETE FROM vacancy WHERE id = :id")
    suspend fun deleteVacancyById(id: String): Int

    @Query("SELECT * FROM vacancy ORDER BY createdAt DESC")
    fun getAllVacancies(): Flow<List<VacancyEntity>>

    @Query("SELECT * FROM vacancy WHERE id = :id")
    suspend fun getVacancyById(id: String): VacancyEntity?
}
