package com.cleteci.redsolidaria.models

class Resource(val service: Service? = null, val category: Category? = null) {

    val type: Type = when {
        category != null -> Type.CATEGORY
        service != null -> Type.SERVICE
        else -> Type.OTHER
    }
    var id: String = category?.id ?: service?.id ?: ""
    var name: String = category?.name ?: service?.name ?: ""
    var iconId: Int = category?.iconId ?: service?.category!!.iconId ?: 0

    enum class Type { CATEGORY, SERVICE, OTHER }

}