package com.techcode.gymcontrol.domain.usecase.usuario

import com.techcode.gymcontrol.domain.model.Person
import com.techcode.gymcontrol.domain.repository.UsuarioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

class RegistrarUsuarioUseCase @Inject constructor(
	private val usuarioRepository: UsuarioRepository
) {
	sealed class Result {
		object Success : Result()
		data class Error(val message: String) : Result()
	}

	suspend operator fun invoke(usuario: Person): Result = withContext(Dispatchers.IO) {
		return@withContext try {
			// Validaciones básicas del usuario
			if (usuario.usuario.isBlank()) {
				return@withContext Result.Error("El nombre no puede estar vacío")
			}

			if (usuario.email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(usuario.email).matches()) {
				return@withContext Result.Error("Email no válido")
			}

			// Intentar registrar el usuario
			usuarioRepository.agregarUsuario(usuario)
			Result.Success
		} catch (e: Exception) {
			Result.Error(e.message ?: "Error desconocido al registrar usuario")
		}
	}
}