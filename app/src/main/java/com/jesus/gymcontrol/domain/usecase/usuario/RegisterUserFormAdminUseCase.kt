package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.repository.UserAdminRepository
import javax.inject.Inject


class RegisterUserFromAdminUseCase @Inject constructor(
    private val repository: UserAdminRepository
) {
    suspend operator fun invoke(
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
    ): Result<Unit> {
        return repository.registerUserFromAdmin(
            email = email,
            password = password,
            name = name,
            phone = phone,
            idCard = idCard,
            gender = gender,
            age = age,
            membership = membership,
            code = code,
            gimnasioCode = gimnasioCode,
            rol = rol
        )
    }
}