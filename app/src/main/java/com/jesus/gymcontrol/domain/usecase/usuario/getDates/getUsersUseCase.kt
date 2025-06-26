package com.jesus.gymcontrol.domain.usecase.usuario.getDates

import com.jesus.gymcontrol.domain.model.Person
import com.jesus.gymcontrol.domain.repository.UsuarioRepository
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


