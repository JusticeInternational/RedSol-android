package com.cleteci.redsolidaria.models

data class User(val id: String,
                val name: String,
                val role: String,
                val email:String,
                val password: String? ="",
                val lastName:String? =""
)
