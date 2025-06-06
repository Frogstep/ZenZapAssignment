package io.fresco.zenzapassignment.ui.utils

import java.util.Locale

object TextFormatter {

    fun getFormattedChange(change: String, changePercents: String): ChangePriceFormatted {
        val strBuilder = StringBuilder()
        var positive = true
        change.toDoubleOrNull()?.let {
            if (it < 0) {
                positive = false
            }
            val value = String.format(Locale.ENGLISH, "%.2f", it)
            strBuilder.append(value)
        } ?: strBuilder.append("0.00")
        strBuilder.append(" (")
        val cleanPercents = changePercents.replace("%", "")
        cleanPercents.toDoubleOrNull()?.let {
            val percentValue = String.format(Locale.ENGLISH, "%.2f", it)
            strBuilder.append(percentValue)
            strBuilder.append('%')
        } ?: strBuilder.append("0.0%")

        return ChangePriceFormatted(strBuilder.append(")").toString(), positive)
    }

    fun formatPrice(price: String): String {
        return price.toDoubleOrNull()?.let {
            "$${String.format(Locale.ENGLISH, "%.2f", it)}"
        } ?: "$0.00"
    }
}

data class ChangePriceFormatted(val text: String, val positive: Boolean)
