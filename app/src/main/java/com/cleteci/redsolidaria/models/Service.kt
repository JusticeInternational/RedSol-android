package com.cleteci.redsolidaria.models

import java.io.Serializable

data class Service (val id: String,
                    val name:String,
                    val category: Category,
                    val hourHand: String = "",
                    val ranking: String = "",
                    val cate: String? = "",
                    val location: String = "",
                    val description: String? = "",
                    val isGeneric: Boolean = false
                    ) : Serializable