package com.cleteci.redsolidaria.models

import com.cleteci.redsolidaria.util.getIcon

class Resource(val service: Service? = null, val category: Category? = null) {

    val type: Type = when {
        service != null -> Type.SERVICE
        service!!.serviceCategory!= null -> Type.CATEGORY

        else -> Type.OTHER
    }
    var id: String = service!!.serviceCategory?.id() ?: service?.id ?: ""
    var name: String = service!!.serviceCategory?.name() ?: service?.name ?: ""
    var iconId: Int = getIcon(service!!.serviceCategory!!.icon()) ?: service?.category!!.iconId ?: 0

    enum class Type { CATEGORY, SERVICE, OTHER }

}