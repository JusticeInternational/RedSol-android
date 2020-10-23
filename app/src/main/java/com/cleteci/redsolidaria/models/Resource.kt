package com.cleteci.redsolidaria.models

import java.io.Serializable

data class Resource (val id: String, val name:String, val hourHand: String, val ranking: String, val cate: String?, val location: String, val photo:Int, val description: String?, val isGeneric: Boolean) : Serializable