package com.cleteci.redsolidaria.models

import java.io.Serializable

/**
 * Created by heiker araujo on 10/01/2020.
 */
data class Organization(val id: String, val name: String, val phone: String, val webPage: String, val aboutUs: String, val servicesDesc: String, val location: String?, val plan: String) : Serializable
