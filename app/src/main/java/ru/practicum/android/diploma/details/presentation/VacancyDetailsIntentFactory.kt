package ru.practicum.android.diploma.details.presentation

import android.content.Context
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

        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.fromParts(MAILTO_SCHEME, preparedEmail, null)
            vacancyName?.trim()
                ?.takeIf { it.isNotBlank() }
                ?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
        }

        return Intent.createChooser(emailIntent, null)
    }

    fun createPhoneIntent(phone: String?): Intent? {
        val preparedPhone = phone
            ?.filter { it.isDigit() || it == PHONE_PLUS_SIGN }
            .orEmpty()

        if (preparedPhone.isBlank()) return null

        val phoneIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.fromParts(TEL_SCHEME, preparedPhone, null)
        }

        return Intent.createChooser(phoneIntent, null)
    }

    fun canHandleIntent(context: Context, intent: Intent): Boolean {
        return intent.getChooserTargetIntent().resolveActivity(context.packageManager) != null
    }

    private fun buildShareText(vacancyName: String?, vacancyUrl: String?): String {
        val preparedName = vacancyName?.trim().orEmpty()
        val preparedUrl = vacancyUrl?.trim().orEmpty()

        return listOf(preparedName, preparedUrl)
            .filter { it.isNotBlank() }
            .joinToString(separator = SHARE_TEXT_SEPARATOR)
    }

    @Suppress("DEPRECATION")
    private fun Intent.getChooserTargetIntent(): Intent {
        if (action != Intent.ACTION_CHOOSER) return this
        return getParcelableExtra(Intent.EXTRA_INTENT) ?: this
    }

    private companion object {
        const val TEXT_MIME_TYPE = "text/plain"
        const val MAILTO_SCHEME = "mailto"
        const val TEL_SCHEME = "tel"
        const val PHONE_PLUS_SIGN = '+'
        const val SHARE_TEXT_SEPARATOR = "\n"
    }
}
