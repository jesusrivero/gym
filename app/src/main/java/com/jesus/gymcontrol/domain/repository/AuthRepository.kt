package com.jesus.gymcontrol.domain.repository


interface AuthRepository {
    suspend fun registerUser(
        name:String,
        email: String,
        password: String,
        idcard: String
    ) : Result<Unit>


    suspend fun loginUser(
        email: String,
        password: String):
            Result<Unit>

    suspend fun recoverPassword(
        email:String):
            Result<Unit>

    suspend fun updateRolAndCode(
        uid: String,
        rol: String,
        code: String
    ):  Result<Unit>

    suspend fun updateDatesUser(
        uid: String,
        idcard: String,
        age: String,
        phone: String,
        gender: String,
    ):  Result<Unit>
}

