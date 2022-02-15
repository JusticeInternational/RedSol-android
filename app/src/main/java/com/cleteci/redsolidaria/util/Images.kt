package com.cleteci.redsolidaria.util

import com.cleteci.redsolidaria.R

    fun getIcon(iconName: String): Int {
        return when (iconName) {
            "1000-010_Employment" -> R.drawable.ic_general_category
            "1000-000_Food" -> R.drawable.ic_food
            "0250-000_Water" -> R.drawable.ic_food
            "0600-000_Health" -> R.drawable.ic_emergency
            "1400-000_Legal" -> R.drawable.ic_filled_create_organization_24
            "3200-000_Social" -> R.drawable.ic_filled_person_24
            else -> R.drawable.ic_general_category
        }

    }
