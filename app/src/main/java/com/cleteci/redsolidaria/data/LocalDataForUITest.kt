package com.cleteci.redsolidaria.data

import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.*

object LocalDataForUITest {

    const val ROLE_ORGANIZATION = "ORGANIZATION"
    const val ROLE_BENEFICIARY = "BENEFICIARY"

    fun getUsersList(): ArrayList<User> {
        val list = java.util.ArrayList<User>()
        list.add(User("0", "Miami General Medical Center", ROLE_ORGANIZATION,
            "organization0@sample.com", "123456"))
        list.add(User("1", "Palm Medical Centers", ROLE_ORGANIZATION,
            "organization1@sample.com", "123456"))
        list.add(User("2", "Carlos", ROLE_BENEFICIARY,
            "csuarez@correo.com", "123456","Suarez"))
        list.add(User("3", "Cristina", ROLE_BENEFICIARY,
            "cpaz@correo.com", "123456","Paz"))
        list.add(User("4", "Alexandra", ROLE_BENEFICIARY,
            "aperdomo@mahisoft.com", "123456","Perdomo"))
        list.add(User("5", "Leon Medical Centers Miami", ROLE_ORGANIZATION,
            "organization5@sample.com", "123456"))
        list.add(User("6", "Little Havana Activities & Nutrition Center", ROLE_ORGANIZATION,
            "organization6@sample.com", "123456"))
        list.add(User("7", "University of Miami Health System", ROLE_ORGANIZATION,
            "organization7@sample.com", "123456"))
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
            "8:00 am - 5:00 pm",
            "Miami",
            "871 Coral Way, Miami, FL 33145, United States",
            "https://www.umiamihealth.org",
            "+1 305-856-3287",
            "Básico",
            arrayListOf(getServiceById("0")!!, getServiceById("1")!!),
            ArrayList(),
            "Info 0 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar.",
            "Des 0 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar.",
            " Need 0 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar."))

        list.add(Organization("1", getUserById("1")!!,"Palm Medical Centers",25.7654, -80.2370,
            "7:00 am - 4:00 pm",
            "Miami",
            "135 E 1st St, Lakeland, FL 33805, United States",
            "https://www.palmmedicalcenters.com",
            "+1 863-686-2728",
            "Plus",
            arrayListOf(getServiceById("1")!!),
            ArrayList(),
            "Info 1 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar.",
            "Des 1 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar.",
            " Need 1Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar."))

        list.add(Organization("2", getUserById("5")!!,"Leon Medical Centers Miami",25.7711, -80.2388,
            "9:00 am - 6:00 pm",
            "Little Havana",
            "101 SW 27th Ave, Miami, FL 33135, United States",
            "https://www.convivacarecenters.com/en",
            "+1 305-642-5366",
            "Básico",
            arrayListOf(getServiceById("2")!!, getServiceById("7")!!),
            ArrayList(),
            "Info 1 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar.",
            "Des 1 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar.",
            " Need 1Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar."))

        list.add(Organization("3", getUserById("6")!!,"Little Havana Activities & Nutrition Center",25.7657, -80.2060,
            "8:00 am - 5:00 pm",
            "Little Havana",
            "700 SW 8th St, Miami, FL 33130, United States",
            "https://www.lhanc.org",
            "+1 305-858-0887",
            "Básico",
            arrayListOf(getServiceById("3")!!, getServiceById("6")!!),
            ArrayList(),
            "Info 1 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar.",
            "Des 1 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar.",
            " Need 1Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique. Sed eget ante porta, congue lectus ac, placerat sapien. Proin laoreet sagittis nisl eget pulvinar."))

        list.add(Organization("4", getUserById("7")!!,"University of Miami Health System",25.7884, -80.2164,
            "Abierto 24 horas",
            "Little Havana",
            "1295 NW 14th St, Miami, FL 33136, United States",
            "https://www.umiamihealth.org",
            "+1 305-243-4000",
            "Premium",
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

    fun getOrganizationByIdTest(id: String): Organization? {
        for ( organization in getOrganizationsList() ) {
            if(organization.id == id)
                return organization
        }
        return null
    }

    fun getServicesList(): ArrayList<Service> {
        val list = java.util.ArrayList<Service>()
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
        val list = java.util.ArrayList<Category>()
        list.add(Category("0","General", R.drawable.ic_general_category))
        list.add(Category("1","Vacuna", R.drawable.ic_vaccine))
        list.add(Category("2","Salud", R.drawable.ic_cross))
        list.add(Category("3","Comida", R.drawable.ic_food))
        list.add(Category("4","Transporte", R.drawable.ic_transport))
        list.add(Category("5","Legal", R.drawable.ic_legal))
        list.add(Category("6","Laboratorio", R.drawable.ic_0100_110_covid_tests))
        return list
    }

    fun getCategoryById(id: String): Category? {
        for ( category in getCategoriesList() ) {
            if(category.id == id)
                return category
        }
        return null
    }

    fun getGeneralCategory(): Category = getCategoriesList()[0]

}