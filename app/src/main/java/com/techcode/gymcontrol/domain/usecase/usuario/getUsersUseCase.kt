package com.techcode.gymcontrol.domain.usecase.usuario

import com.techcode.gymcontrol.domain.model.Person
import com.techcode.gymcontrol.domain.repository.UsuarioRepository
import javax.inject.Inject

data class getUsersUseCase @Inject constructor(
	private val usuarioRepository: UsuarioRepository
	
	){
	suspend operator fun invoke(): List<Person> {
		return usuarioRepository.obtenerUsuarios()
	}
}



