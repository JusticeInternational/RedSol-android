package com.cleteci.redsolidaria.models

import java.io.Serializable

data class Organization(
    val id: String, val user: User,
    val name: String,
    val lat: Double,
    val lng: Double,
    val schedule: String,
    val address: String,
    val location: String,
    val webPage: String,
    val phone: String,
    val plan: String,
    val servicesList: ArrayList<Service>?,
    val postsList: ArrayList<Post>?,
    val aboutUs: String? = "",
    val servicesDesc: String? = "",
    val whatWeNeedDesc: String? = ""
) : Serializable {
    enum class Attribute { NAME, SCHEDULE, LOCATION, PAGE, PHONE, EMAIL, LAT, LNG }
    class OrganizationLists(
        val servicesList: ArrayList<Service>,
        val serviceCategories: ArrayList<Category>,
        val genericServices: ArrayList<Service>
    )
}
