package com.jesus.gymcontrol.domain.usecase.usuario.auth

import com.jesus.gymcontrol.domain.repository.AuthRepository
import javax.inject.Inject

class CheckidcardExistsUseCase @Inject constructor(
	private val repository: AuthRepository
) {
	suspend operator fun invoke(idcard: String): Boolean {
		return repository.checkidcardExists(idcard)
	}
}
