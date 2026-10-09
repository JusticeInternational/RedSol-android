package com.cleteci.redsolidaria.models

class Resource {
    var service: Service? = null
    var category: Category? = null
    var id: String = ""
    var name: String = ""
    var iconId: Int = 0
    var type: Type = Type.OTHER

    enum class Type { CATEGORY, SERVICE, OTHER }

    constructor(service: Service) {
        this.service = service
        type = Type.SERVICE
        id = service.id
        name = service.name.orEmpty()
        val cat = service.category
        if (cat != null) {
            iconId = cat.iconId
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
