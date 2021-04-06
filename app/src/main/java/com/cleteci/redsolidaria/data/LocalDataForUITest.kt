package com.cleteci.redsolidaria.data

import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.*

object LocalDataForUITest {

    const val ROLE_ORGANIZATION = "ORGANIZATION"
    const val ROLE_BENEFICIARY = "BENEFICIARY"

    fun getUsersList(): ArrayList<User> {
        val list = java.util.ArrayList<User>()
        list.add(User("0", "Miami General Medical Center", ROLE_ORGANIZATION,
            "scvmc@correo.com", "123456"))
        list.add(User("1", "Palm Medical Centers", ROLE_ORGANIZATION,
            "lacommunity@correo.com", "123456"))
        list.add(User("2", "Carlos", ROLE_BENEFICIARY,
            "csuarez@correo.com", "123456","Suarez"))
        list.add(User("3", "Cristina", ROLE_BENEFICIARY,
            "cpaz@correo.com", "123456","Paz"))
        list.add(User("4", "Alexandra", ROLE_BENEFICIARY,
            "aperdomo@mahisoft.com", "123456","Perdomo"))
        list.add(User("5", "Leon Medical Centers Miami", ROLE_ORGANIZATION,
            "scvmc@correo.com", "123456"))
        list.add(User("6", "Little Havana Activities & Nutrition Center", ROLE_ORGANIZATION,
            "lacommunity@correo.com", "123456"))
        list.add(User("7", "University of Miami Health System", ROLE_ORGANIZATION,
            "lacommunity@correo.com", "123456"))
        return list
    }

    fun getUserById(id: String): User? {
         for ( user in getUsersList() ) {
             if(user.id == id)
                 return user
         }
        return null
    }

    fun uiTestLogin(email: String, pass: String): User? {
        for ( user in getUsersList() ) {
            if(user.email == email && user.password == pass)
                return user
        }
        return null
    }

    fun getOrganizationsList(): ArrayList<Organization> {
        val list = java.util.ArrayList<Organization>()

        list.add(Organization("0", getUserById("0")!!,"Miami General Medical Center",25.7510, -80.2252,
            "Horario: 8:00 am - 5:00 pm",
            "Ubicación: Miami",
            "val location: String",
            "Página Web: www.scvmc.com",
            "Teléfono: +984651384951",
            "Básico",
            arrayListOf(getServiceById("0")!!),
            ArrayList(),
            "Info 0 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar.",
            "Des 0 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar.",
            " Need 0 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar."))

        list.add(Organization("1", getUserById("1")!!,"Palm Medical Centers",25.7654, -80.2370,
            "Horario: 8:00 am - 5:00 pm",
            "Ubicación: Little Havana",
            "val location: String",
            "Página Web: www.lacommunity.com",
            "Teléfono: +984651384951",
            "Básico",
            arrayListOf(getServiceById("1")!!),
            ArrayList(),
            "Info 1 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar.",
            "Des 1 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar.",
            " Need 1Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar."))

        list.add(Organization("2", getUserById("5")!!,"Leon Medical Centers Miami",25.7711, -80.2388,
            "Horario: 8:00 am - 5:00 pm",
            "Ubicación: Miami",
            "val location: String",
            "Página Web: www.lacommunity.com",
            "Teléfono: +984651384951",
            "Básico",
            arrayListOf(getServiceById("2")!!, getServiceById("7")!!),
            ArrayList(),
            "Info 1 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar.",
            "Des 1 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar.",
            " Need 1Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar."))

        list.add(Organization("3", getUserById("6")!!,"Little Havana Activities & Nutrition Center",25.7657, -80.2060,
            "Horario: 8:00 am - 5:00 pm",
            "Ubicación: Little Havana",
            "val location: String",
            "Página Web: www.lacommunity.com",
            "Teléfono: +984651384951",
            "Básico",
            arrayListOf(getServiceById("3")!!, getServiceById("6")!!),
            ArrayList(),
            "Info 1 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar.",
            "Des 1 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar.",
            " Need 1Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar."))

        list.add(Organization("4", getUserById("7")!!,"University of Miami Health System",25.7884, -80.2164,
            "Horario: 8:00 am - 5:00 pm",
            "Ubicación: Miami",
            "val location: String",
            "Página Web: www.lacommunity.com",
            "Teléfono: +984651384951",
            "Básico",
            arrayListOf(getServiceById("4")!!, getServiceById("5")!!),
            ArrayList(),
            "Info 1 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar.",
            "Des 1 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar.",
            " Need 1Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar."))
        return list
    }

    fun getOrganizationByUserId(id: String): Organization? {
        for ( organization in getOrganizationsList() ) {
            if(organization.user.id == id)
                return organization
        }
        return null
    }

    fun getServicesList(): ArrayList<Service> {
        var list = java.util.ArrayList<Service>()
        list.add(Service("0", "Prueba Covid-19", getCategoryById("0")!!))
        list.add(Service("1", "Vacuna Covic-19", getCategoryById("1")!!))
        list.add(Service("2", "Revisión General", getCategoryById("2")!!))
        list.add(Service("3", "Almuerzo", getCategoryById("3")!!))
        list.add(Service("4", "Transporte Escolar", getCategoryById("4")!!))
        list.add(Service("5", "Asesoria Legal", getCategoryById("5")!!))
        list.add(Service("6", "Desayuno", getCategoryById("3")!!))
        list.add(Service("7", "Revisión Odontologíca", getCategoryById("2")!!))
        return list
    }

    fun getServiceById(id: String): Service? {
        for ( service in getServicesList() ) {
            if(service.id == id)
                return service
        }
        return null
    }

    fun getCategoriesList(): ArrayList<Category> {
        var list = java.util.ArrayList<Category>()
        list.add(Category("0","Laboratorio", R.drawable.ic_test))
        list.add(Category("1","Vacuna", R.drawable.ic_vaccine))
        list.add(Category("2","Medicina General", R.drawable.ic_cross))
        list.add(Category("3","Comida", R.drawable.ic_food))
        list.add(Category("4","Transporte", R.drawable.ic_transport))
        list.add(Category("5","Legal", R.drawable.ic_legal))
        return list
    }

    fun getCategoryById(id: String): Category? {
        for ( category in getCategoriesList() ) {
            if(category.id == id)
                return category
        }
        return null
    }
}