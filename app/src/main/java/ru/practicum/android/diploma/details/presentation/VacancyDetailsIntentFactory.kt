package ru.practicum.android.diploma.details.presentation

import android.content.Intent
import android.net.Uri

class VacancyDetailsIntentFactory {

    fun createShareIntent(vacancyName: String?, vacancyUrl: String?): Intent? {
        val shareText = buildShareText(vacancyName, vacancyUrl)
        if (shareText.isBlank()) return null

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = TEXT_MIME_TYPE
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        return Intent.createChooser(sendIntent, null)
    }

    fun createEmailIntent(email: String?, vacancyName: String?): Intent? {
        val preparedEmail = email?.trim().orEmpty()
        if (preparedEmail.isBlank()) return null

        return Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("$MAILTO_SCHEME:$preparedEmail")
            vacancyName
                ?.trim()
                ?.takeIf { it.isNotBlank() }
                ?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
        }
    }

    fun createPhoneIntent(phone: String?): Intent? {
        val preparedPhone = phone
            ?.filter { it.isDigit() || it == PHONE_PLUS_SIGN }
            .orEmpty()

        if (preparedPhone.isBlank()) return null

        return Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("$TEL_SCHEME:$preparedPhone")
        }
    }

    private fun buildShareText(vacancyName: String?, vacancyUrl: String?): String {
        val preparedName = vacancyName?.trim().orEmpty()
        val preparedUrl = vacancyUrl?.trim().orEmpty()

        return listOf(preparedName, preparedUrl)
            .filter { it.isNotBlank() }
            .joinToString(separator = SHARE_TEXT_SEPARATOR)
    }

    private companion object {
        const val TEXT_MIME_TYPE = "text/plain"
        const val MAILTO_SCHEME = "mailto"
        const val TEL_SCHEME = "tel"
        const val PHONE_PLUS_SIGN = '+'
        const val SHARE_TEXT_SEPARATOR = "\n"
    }
}
