package com.cleteci.redsolidaria.models

class Resource {
    var service: Service? = null
    var category: Category? =  null
    var id: String = ""
    var name: String =  ""
    var iconId: Int = 0
    var type: Type = Type.OTHER

    enum class Type { CATEGORY, SERVICE, OTHER }

    constructor(service: Service) {
        this.service = service
        type = Type.SERVICE
        val resource = service.category
        if (resource != null) {
            id = resource.id
            name = resource.name
            iconId = resource.iconId
        }
    }

    constructor(category: Category) {
        this.category = category
        type = Type.CATEGORY
        id = category.id
        name = category.name
        iconId = category.iconId

    }
}