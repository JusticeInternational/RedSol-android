package com.cleteci.redsolidaria.models

import java.io.Serializable

/**
 * Created by ogulcan on 07/02/2018.
 */
data class ResourceCategory(val id: String, val name: String, val icon: String, val photo: Int, val description: String?) : Serializable

