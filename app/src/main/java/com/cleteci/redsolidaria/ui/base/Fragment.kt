package com.cleteci.redsolidaria.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment

inline fun <T : Fragment> T.withArguments(capacity: Int, block: Bundle.() -> Unit): T {
    arguments = Bundle(capacity).apply(block)
    return this
}