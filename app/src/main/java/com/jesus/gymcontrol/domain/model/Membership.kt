package com.jesus.gymcontrol.domain.model

data class Membership(
    val id:String= "",
    val nombre:String = "",
    val tipo: String = "",
    val precio: Double= 0.0,
    val gimnasioCode: String = ""
)
