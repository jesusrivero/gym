package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.repository.AuthRepository
import javax.inject.Inject


class UpdateDatesUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        uid: String,
        idcard: String,
        age: String,
        phone: String,
        gender: String
    ): Result<Unit> {
        return repository.updateDatesUser(uid, idcard, age, phone, gender)
    }
}