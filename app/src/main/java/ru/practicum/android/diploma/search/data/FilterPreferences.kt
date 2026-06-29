package ru.practicum.android.diploma.search.data

import android.content.SharedPreferences
import ru.practicum.android.diploma.search.domain.models.FilterSettings

class FilterPreferences(
    private val sharedPreferences: SharedPreferences
) {

    fun getFilterSettings(): FilterSettings {
        return FilterSettings(
            placeOfWork = sharedPreferences.getNullableString(KEY_PLACE_OF_WORK),
            industry = sharedPreferences.getNullableString(KEY_INDUSTRY),
            salary = sharedPreferences.getString(KEY_SALARY, "").orEmpty(),
            doNotShowWithoutSalary = sharedPreferences.getBoolean(KEY_DO_NOT_SHOW_WITHOUT_SALARY, false),
            countryId = sharedPreferences.getNullableInt(KEY_COUNTRY_ID),
            regionId = sharedPreferences.getNullableInt(KEY_REGION_ID)
        )
    }

    fun saveFilterSettings(settings: FilterSettings) {
        sharedPreferences.edit()
            .putNullableString(KEY_PLACE_OF_WORK, settings.placeOfWork)
            .putNullableString(KEY_INDUSTRY, settings.industry)
            .putString(KEY_SALARY, settings.salary)
            .putBoolean(KEY_DO_NOT_SHOW_WITHOUT_SALARY, settings.doNotShowWithoutSalary)
            .putNullableInt(KEY_COUNTRY_ID, settings.countryId)
            .putNullableInt(KEY_REGION_ID, settings.regionId)
            .apply()
    }

    fun clearFilterSettings() {
        sharedPreferences.edit()
            .remove(KEY_PLACE_OF_WORK)
            .remove(KEY_INDUSTRY)
            .remove(KEY_SALARY)
            .remove(KEY_DO_NOT_SHOW_WITHOUT_SALARY)
            .remove(KEY_COUNTRY_ID)
            .remove(KEY_REGION_ID)
            .apply()
    }

    private fun SharedPreferences.getNullableString(key: String): String? {
        return getString(key, null)?.takeIf { it.isNotBlank() }
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
        val value = getInt(key, UNKNOWN_ID)
        return if (value == UNKNOWN_ID) null else value
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
        const val KEY_PLACE_OF_WORK = "place_of_work"
        const val KEY_INDUSTRY = "industry"
        const val KEY_SALARY = "salary"
        const val KEY_DO_NOT_SHOW_WITHOUT_SALARY = "do_not_show_without_salary"
        const val KEY_COUNTRY_ID = "country_id"
        const val KEY_REGION_ID = "region_id"
        const val UNKNOWN_ID = -1
    }
}
