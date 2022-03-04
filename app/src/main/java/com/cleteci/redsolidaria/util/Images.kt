package com.cleteci.redsolidaria.util

import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.util.DbConstants.CATEGORY_ICON_NAMES


object DbConstants {
    val CATEGORY_ICON_NAMES = arrayListOf(
        "0100-000_All_COVID",
        "0100-110_COVID_tests",
        "0100-120_Vaccination",
        "0100-130_COVID_support",
        "0200-000_Food",
        "0250-000_Water",
        "0300-000_Housing",
        "0400-000_Small_Donations",
        "0400-100_SD_Basics",
        "0400-200_SD_Others",
        "0500-000_Information",
        "0600-000_Health",
        "0600-700_Mental_Health",
        "0600-800_Dental_health",
        "0600-700_Drugs_alcohol",
        "0600-800_Weight_loss",
        "0700-000_Education",
        "0800-000_Communications",
        "0900-000_Transportation",
        "1000-000_Economy",
        "1000-010_Employment",
        "1000-020_Entrepreneurship",
        "1100-000_Religion",
        "1200-000_Public_services",
        "1300-000_Immigration",
        "1400-000_Legal",
        "1500-000_Sports",
        "1600-000_Culture")

}
    fun getCategoryIconByIconString(iconName: String): Int {
        return when (iconName.toLowerCase()) {
            CATEGORY_ICON_NAMES[0].toLowerCase() -> R.drawable.ic_0100_000_all_covid
            CATEGORY_ICON_NAMES[1].toLowerCase() -> R.drawable.ic_0100_110_covid_tests
            CATEGORY_ICON_NAMES[2].toLowerCase() -> R.drawable.ic_0100_120_vaccination
            CATEGORY_ICON_NAMES[3].toLowerCase() -> R.drawable.ic_0100_130_covid_suport
            CATEGORY_ICON_NAMES[4].toLowerCase() -> R.drawable.ic_0200_000_food
            CATEGORY_ICON_NAMES[5].toLowerCase() -> R.drawable.ic_0250_000_water
            CATEGORY_ICON_NAMES[6].toLowerCase() -> R.drawable.ic_0300_000_housing
            CATEGORY_ICON_NAMES[7].toLowerCase() -> R.drawable.ic_general_category//R.drawable.ic_0400_000_small_donations
            CATEGORY_ICON_NAMES[8].toLowerCase() -> R.drawable.ic_general_category//R.drawable.ic_0400_100_sd_basics
            CATEGORY_ICON_NAMES[9].toLowerCase() -> R.drawable.ic_general_category//R.drawable.ic_0400_200_sd_others
            CATEGORY_ICON_NAMES[10].toLowerCase() -> R.drawable.ic_general_category//R.drawable.ic_0500_000_information
            CATEGORY_ICON_NAMES[11].toLowerCase() -> R.drawable.ic_0600_000_health
            CATEGORY_ICON_NAMES[12].toLowerCase() -> R.drawable.ic_0600_700_mental_health
            CATEGORY_ICON_NAMES[13].toLowerCase() -> R.drawable.ic_0600_800_dental_health
            CATEGORY_ICON_NAMES[14].toLowerCase() -> R.drawable.ic_0600_700_drugs_alcohol
            CATEGORY_ICON_NAMES[15].toLowerCase() -> R.drawable.ic_0600_800_weight_loss
            CATEGORY_ICON_NAMES[16].toLowerCase() -> R.drawable.ic_0700_000_education
            CATEGORY_ICON_NAMES[17].toLowerCase() -> R.drawable.ic_0800_000_communications
            CATEGORY_ICON_NAMES[18].toLowerCase() -> R.drawable.ic_0900_000_transportation
            CATEGORY_ICON_NAMES[19].toLowerCase() -> R.drawable.ic_1000_000_economy
            CATEGORY_ICON_NAMES[20].toLowerCase() -> R.drawable.ic_1000_010_employment
            CATEGORY_ICON_NAMES[21].toLowerCase() -> R.drawable.ic_1000_020_entrepreneurship
            CATEGORY_ICON_NAMES[22].toLowerCase() -> R.drawable.ic_1100_000_religion
            CATEGORY_ICON_NAMES[23].toLowerCase() -> R.drawable.ic_general_category//R.drawable.ic_1200_000_public_services
            CATEGORY_ICON_NAMES[24].toLowerCase() -> R.drawable.ic_1300_000_immigration
            CATEGORY_ICON_NAMES[25].toLowerCase() -> R.drawable.ic_1400_000_legal
            CATEGORY_ICON_NAMES[26].toLowerCase() -> R.drawable.ic_1500_000_sports
            CATEGORY_ICON_NAMES[27].toLowerCase() -> R.drawable.ic_1600_000_culture
            else -> R.drawable.ic_general_category
        }

    }
