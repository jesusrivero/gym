package com.techcode.gymcontrol.presentation.ui.people

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techcode.gymcontrol.data.db.AppDatabase
import com.techcode.gymcontrol.data.db.dao.UsuariosDatabaseDao
import com.techcode.gymcontrol.data.db.entity.PersonEntity
import com.techcode.gymcontrol.domain.model.Person
import com.techcode.gymcontrol.domain.usecase.usuario.DeleteUserUseCase
import com.techcode.gymcontrol.domain.usecase.usuario.GetUsersUseCase
import com.techcode.gymcontrol.domain.usecase.usuario.RegistrarUsuarioUseCase
import com.techcode.gymcontrol.domain.usecase.usuario.UpdateUserUseCase

import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PeopleViewModel @Inject constructor(
	private val registrarUsuarioUseCase: RegistrarUsuarioUseCase,
	private val updateUserUseCase: UpdateUserUseCase,
	private val deleteUserUseCase: DeleteUserUseCase,
	private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

	var state by mutableStateOf(PeopleState())
		private set

	fun saveUser(user: Person) = viewModelScope.launch {
		state = state.copy(isLoading = true, error = null)
		try {
			registrarUsuarioUseCase(user)
			getUsers() // Actualizar la lista después de agregar
		} catch (e: Exception) {
			state = state.copy(error = e.message ?: "Error al guardar usuario")
			e.printStackTrace()
		} finally {
			state = state.copy(isLoading = false)
		}
	}

	fun getUsers() = viewModelScope.launch {
		state = state.copy(isLoading = true, error = null)
		try {
			val res = getUsersUseCase()
			state = state.copy(userList = res)
		} catch (e: Exception) {
			state = state.copy(error = e.message ?: "Error al obtener usuarios")
			e.printStackTrace()
		} finally {
			state = state.copy(isLoading = false)
		}
	}

	fun updateUser(user: Person) = viewModelScope.launch {
		state = state.copy(isLoading = true, error = null)
		try {
			updateUserUseCase(user) // Pasamos el usuario a actualizar
			// Actualizamos la lista completa después de la modificación
			getUsers()
		} catch (e: Exception) {
			state = state.copy(error = e.message ?: "Error al actualizar usuario")
			e.printStackTrace()
		} finally {
			state = state.copy(isLoading = false)
		}
	}

	fun deleteUser(user: Person) = viewModelScope.launch {
		state = state.copy(isLoading = true, error = null)
		try {
			deleteUserUseCase(user) // Pasamos el usuario a eliminar
			// Actualizamos la lista completa después de la eliminación
			getUsers()

			// Opcional: Mensaje de éxito
			state = state.copy()
		} catch (e: Exception) {
			state = state.copy(error = e.message ?: "Error al eliminar usuario")
			e.printStackTrace()
		} finally {
			state = state.copy(isLoading = false)
		}
	}

	// ... similares para updateUser y deleteUser

	data class PeopleState(
		val userList: List<Person> = emptyList(),
		val isLoading: Boolean = false,
		val error: String? = null
	)
}
