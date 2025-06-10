package com.jesus.gymcontrol.domain.model


data class Gym(
    val propietario: String = "",
    val nombre: String = "",
    val admin: String = "",
    val entrenador: String = "",
    val direccion: String = "",
    val telefono: String = "",
    val codigo: String = "",
//    val fechaCreacion: Timestamp? = null
)