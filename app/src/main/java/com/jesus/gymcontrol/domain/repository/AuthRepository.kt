package com.jesus.gymcontrol.domain.repository


interface AuthRepository {
    suspend fun registerUser(
        name:String,
        email: String,
        password: String,
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
        codigo: String
    ):  Result<Unit>

    suspend fun updateDatesUser(
        uid: String,
        cedula: String,
        edad: String,
        numero: String,
        sexo: String,
    ):  Result<Unit>
}

