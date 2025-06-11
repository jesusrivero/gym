package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.model.Person
import com.jesus.gymcontrol.domain.repository.UsuarioRepository
import javax.inject.Inject

data class UpdateUserUseCase @Inject constructor(
	private val usuarioRepository: UsuarioRepository
){
	suspend operator fun invoke(user: Person) {
		usuarioRepository.updateUser(user)
	}
}

