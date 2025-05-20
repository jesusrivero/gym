package com.techcode.gymcontrol.domain.usecase.usuario

import com.techcode.gymcontrol.data.db.entity.PersonEntity
import com.techcode.gymcontrol.domain.model.Person
import com.techcode.gymcontrol.domain.repository.UsuarioRepository
import javax.inject.Inject

data class updateUserUseCase @Inject constructor(
	private val usuarioRepository: UsuarioRepository
){
	suspend operator fun invoke(usuario: Person) {
		usuarioRepository.updateUser(usuario)
	  }
}

