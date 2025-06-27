package com.jesus.gymcontrol.domain.usecase.usuario.password

import com.jesus.gymcontrol.domain.model.PasswordChangeRequest
import com.jesus.gymcontrol.domain.repository.AuthRepository

class ChangePasswordUseCase(
	private val authRepository: AuthRepository
) {
	suspend operator fun invoke(request: PasswordChangeRequest): Result<Unit> {
		return authRepository.changePassword(request)
	}
}
