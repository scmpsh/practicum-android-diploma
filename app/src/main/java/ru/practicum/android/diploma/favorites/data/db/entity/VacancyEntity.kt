package ru.practicum.android.diploma.favorites.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy")
data class VacancyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val salaryCurrency: String?,
    val employerName: String?,
    val employerLogoUrl: String?,
    val areaName: String?,
    val experience: String?,
    val schedule: String?,
    val employment: String?,
    val addressCity: String?,
    val addressStreet: String?,
    val addressBuilding: String?,
    val addressRaw: String?,
    val contactsName: String?,
    val contactsEmail: String?,
    val contactsPhones: String?,
    val contactsComments: String?,
    val keySkills: String?,
    val url: String?,
    val createdAt: Long = System.currentTimeMillis()
)
