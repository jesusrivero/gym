package com.jesus.gymcontrol.domain.model


data class Gym(
    val owner: String = "",
    val name: String = "",
    val admin: String = "",
    val coach: String = "",
    val direction: String = "",
    val phone: String = "",
    val code: String = "",
//    val fechaCreacion: Timestamp? = null
)