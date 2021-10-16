package com.cleteci.redsolidaria.models


class Beneficiary {

    class ResourcesLists(
        val pending: List<Service>,
        val saved: List<Service>,
        val volunteering: List<Service>,
        val used: List<Service>
    )
}