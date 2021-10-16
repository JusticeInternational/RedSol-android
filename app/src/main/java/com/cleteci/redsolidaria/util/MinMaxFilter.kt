package com.cleteci.redsolidaria.util

import android.text.InputFilter
import android.text.Spanned

class MinMaxFilter(private val minValue: Int, private val maxValue: Int)  : InputFilter {

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        try {
            val input = (dest.toString() + source.toString()).toInt()
            if (isInRange(minValue, maxValue, input)) return null
        } catch (nfe: NumberFormatException) {
        }
        return ""
    }

    private fun isInRange(a: Int, b: Int, c: Int): Boolean {
        return if (b > a) c in a..b else c in b..a
    }
}