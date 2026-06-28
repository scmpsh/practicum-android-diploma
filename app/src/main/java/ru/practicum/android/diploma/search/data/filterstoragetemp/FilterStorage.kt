package ru.practicum.android.diploma.search.data.filterstoragetemp

import android.content.SharedPreferences

class FilterStorage(
    private val sharedPrefs: SharedPreferences
) {

    companion object {
        private const val COUNTRY_ID = "country_id"
        private const val REGION_ID = "region_id"
    }

    fun saveCountryId(id: Int) {
        sharedPrefs.edit()
            .putInt(COUNTRY_ID, id)
            .apply()
    }

    fun getCountryId(): Int? {
        val id = sharedPrefs.getInt(COUNTRY_ID, -1)
        return if (id == -1) null else id
    }

    fun saveRegionId(id: Int) {
        sharedPrefs.edit()
            .putInt(REGION_ID, id)
            .apply()
    }

    fun getRegionId(): Int? {
        val id = sharedPrefs.getInt(REGION_ID, -1)
        return if (id == -1) null else id
    }

    fun clearCountryId() {
        sharedPrefs.edit()
            .remove(COUNTRY_ID)
            .apply()
    }

    fun clearRegionId() {
        sharedPrefs.edit()
            .remove(REGION_ID)
            .apply()
    }
}
