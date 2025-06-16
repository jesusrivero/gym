package com.jesus.gymcontrol.domain.repository

interface UserAdminRepository {
    suspend fun registerUserFromAdmin(
        email: String,
        password: String,
        name: String,
        phone: String,
        idCard: String,
        gender: String,
        age: Int,
        rol: String,
        membership: String,
        code: String,
        gimnasioCode: String
    ): Result<Unit>
}


