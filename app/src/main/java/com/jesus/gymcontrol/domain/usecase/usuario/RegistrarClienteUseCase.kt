package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.model.Person
import com.jesus.gymcontrol.domain.repository.UsuarioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class RegistrarClienteUseCase @Inject constructor(
	private val usuarioRepository: UsuarioRepository
) {
	sealed class Result {
		object Success : Result()
		data class Error(val message: String) : Result()
	}

	suspend operator fun invoke(user: Person): Result = withContext(Dispatchers.IO) {
		return@withContext try {
			
			if (user.usuario.isBlank()) {
				return@withContext Result.Error("El nombre no puede estar vacío")
			}

			if (user.email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(user.email).matches()) {
				return@withContext Result.Error("Email no válido")
			}
			
			usuarioRepository.agregarUsuario(user)
			Result.Success
		} catch (e: Exception) {
			Result.Error(e.message ?: "Error desconocido al registrar usuario")
		}
	}
}