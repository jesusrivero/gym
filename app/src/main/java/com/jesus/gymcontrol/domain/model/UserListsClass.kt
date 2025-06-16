package com.jesus.gymcontrol.domain.model

data class ListUser(
    val id: String = "",
    val name: String = "",
    val idCard: String = "",
    val phone: String = "",
    val email: String = "",
    val isActive: Boolean = false,
    val lastPaymentDate: Long? = null // timestamp en millis (nullable)
)