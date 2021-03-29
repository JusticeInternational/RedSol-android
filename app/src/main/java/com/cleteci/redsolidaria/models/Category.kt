package com.cleteci.redsolidaria.models

import java.io.Serializable

data class Category(val id: String, val name: String, val iconId: Int,
                    val photo: Int = 0, val description: String? = "", val icon: String = "") : Serializable

