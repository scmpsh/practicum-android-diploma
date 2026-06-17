package ru.practicum.android.diploma.util

import android.content.Context
import ru.practicum.android.diploma.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object SalaryFormatter {

    fun format(context: Context, from: Int?, to: Int?, currency: String?): String {
        val symbol = getCurrencySymbol(currency)
        val fromStr = from?.let { formatNumber(it) }
        val toStr = to?.let { formatNumber(it) }

        return when {
            fromStr != null && toStr != null -> {
                context.getString(R.string.salary_from_to, fromStr, toStr, symbol)
            }
            fromStr != null -> {
                context.getString(R.string.salary_from, fromStr, symbol)
            }
            toStr != null -> {
                context.getString(R.string.salary_to, toStr, symbol)
            }
            else -> {
                context.getString(R.string.no_salary)
            }
        }
    }

    fun formatNumber(number: Int): String {
        val symbols = DecimalFormatSymbols(Locale.getDefault())
        symbols.groupingSeparator = ' '
        val formatter = DecimalFormat("#,###", symbols)
        return formatter.format(number)
    }

    private fun getCurrencySymbol(currency: String?): String {
        return when (currency) {
            "AZN" -> "₼"
            "BYR" -> "р."
            "EUR" -> "€"
            "GEL" -> "₾"
            "KGS" -> "сом"
            "KZT" -> "₸"
            "RUR", "RUB" -> "₽"
            "UAH" -> "₴"
            "USD" -> "$"
            "UZS" -> "сўм"
            else -> currency ?: ""
        }
    }
}
