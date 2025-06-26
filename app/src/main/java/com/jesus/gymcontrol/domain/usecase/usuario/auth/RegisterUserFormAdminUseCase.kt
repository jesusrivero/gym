package com.jesus.gymcontrol.domain.usecase.usuario.auth

import com.jesus.gymcontrol.domain.model.UserRegistrationData
import com.jesus.gymcontrol.domain.repository.UserAdminRepository
import javax.inject.Inject


class RegisterUserFromAdminUseCase @Inject constructor(
    private val repository: UserAdminRepository
) {
	suspend operator fun invoke(userData: UserRegistrationData): Result<Unit> {
		return repository.registerUserFromAdmin(userData)
	}
}