package com.cleteci.redsolidaria.ui.base

import android.content.Intent
import android.os.Bundle

inline fun Intent.withExtras(count: Int, block: Bundle.() -> Unit): Intent {
    putExtras(Bundle(count).apply(block))
    return this
}