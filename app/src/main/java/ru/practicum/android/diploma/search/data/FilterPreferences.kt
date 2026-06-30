package ru.practicum.android.diploma.search.data

import android.content.SharedPreferences
import ru.practicum.android.diploma.search.domain.models.FilterSettings

class FilterPreferences(
    private val sharedPreferences: SharedPreferences
) {

    fun getFilterSettings(): FilterSettings {
        return FilterSettings(
            countryId = sharedPreferences.getNullableInt(KEY_COUNTRY_ID),
            countryName = sharedPreferences.getNullableString(KEY_COUNTRY_NAME),
            regionId = sharedPreferences.getNullableInt(KEY_REGION_ID),
            regionName = sharedPreferences.getNullableString(KEY_REGION_NAME),
            industryId = sharedPreferences.getNullableString(KEY_INDUSTRY_ID),
            industryName = sharedPreferences.getNullableString(KEY_INDUSTRY_NAME),
            salary = sharedPreferences.getNullableInt(KEY_SALARY),
            onlyWithSalary = sharedPreferences.getBoolean(KEY_ONLY_WITH_SALARY, false)
        )
    }

    fun saveFilterSettings(settings: FilterSettings) {
        sharedPreferences.edit()
            .putNullableInt(KEY_COUNTRY_ID, settings.countryId)
            .putNullableString(KEY_COUNTRY_NAME, settings.countryName)
            .putNullableInt(KEY_REGION_ID, settings.regionId)
            .putNullableString(KEY_REGION_NAME, settings.regionName)
            .putNullableString(KEY_INDUSTRY_ID, settings.industryId)
            .putNullableString(KEY_INDUSTRY_NAME, settings.industryName)
            .putNullableInt(KEY_SALARY, settings.salary)
            .putBoolean(KEY_ONLY_WITH_SALARY, settings.onlyWithSalary)
            .apply()
    }

    fun clearFilterSettings() {
        sharedPreferences.edit()
            .remove(KEY_COUNTRY_ID)
            .remove(KEY_COUNTRY_NAME)
            .remove(KEY_REGION_ID)
            .remove(KEY_REGION_NAME)
            .remove(KEY_INDUSTRY_ID)
            .remove(KEY_INDUSTRY_NAME)
            .remove(KEY_SALARY)
            .remove(KEY_ONLY_WITH_SALARY)
            .apply()
    }

    private fun SharedPreferences.getNullableString(key: String): String? {
        val value = try {
            getString(key, null)
        } catch (ignored: ClassCastException) {
            all[key]?.toString()
        }
        return value?.takeIf { it.isNotBlank() }
    }

    private fun SharedPreferences.Editor.putNullableString(
        key: String,
        value: String?
    ): SharedPreferences.Editor {
        return if (value.isNullOrBlank()) {
            remove(key)
        } else {
            putString(key, value)
        }
    }

    private fun SharedPreferences.getNullableInt(key: String): Int? {
        return try {
            val value = getInt(key, UNKNOWN_ID)
            if (value == UNKNOWN_ID) null else value
        } catch (ignored: ClassCastException) {
            all[key]?.toString()?.toIntOrNull()
        }
    }

    private fun SharedPreferences.Editor.putNullableInt(
        key: String,
        value: Int?
    ): SharedPreferences.Editor {
        return if (value == null) {
            remove(key)
        } else {
            putInt(key, value)
        }
    }

    private companion object {
        const val KEY_COUNTRY_ID = "country_id"
        const val KEY_COUNTRY_NAME = "country_name"
        const val KEY_REGION_ID = "region_id"
        const val KEY_REGION_NAME = "region_name"
        const val KEY_INDUSTRY_ID = "industry_id"
        const val KEY_INDUSTRY_NAME = "industry_name"
        const val KEY_SALARY = "salary"
        const val KEY_ONLY_WITH_SALARY = "only_with_salary"
        const val UNKNOWN_ID = -1
    }
}
