package com.techcode.gymcontrol.domain.usecase.usuario

import com.techcode.gymcontrol.domain.model.Person
import com.techcode.gymcontrol.domain.repository.UsuarioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
	private val usuarioRepository: UsuarioRepository
) {
	suspend operator fun invoke(): List<Person> {
		return withContext(Dispatchers.IO) {
			usuarioRepository.obtenerUsuarios()
		}
	}
}


