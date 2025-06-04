package com.jesus.gymcontrol.presentation.ui.people

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesus.gymcontrol.domain.model.Person
import com.jesus.gymcontrol.domain.usecase.usuario.DeleteUserUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.GetUsersUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.RegistrarClienteUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PeopleViewModel @Inject constructor(
	private val registrarUsuarioUseCase: RegistrarClienteUseCase,
	private val updateUserUseCase: UpdateUserUseCase,
	private val deleteUserUseCase: DeleteUserUseCase,
	private val getUsersUseCase: GetUsersUseCase,
) : ViewModel() {
	
	var state by mutableStateOf(PeopleState())
		private set
	
	fun saveUser(user: Person) {
		viewModelScope.launch {
			state = state.copy(isLoading = true, error = null)
			try {
				registrarUsuarioUseCase.invoke(user).let {
					when (it) {
						is RegistrarClienteUseCase.Result.Success -> {
							getUsers()
						}
						
						is RegistrarClienteUseCase.Result.Error -> {
							state = state.copy(error = it.message)
						}
					}
				}
			} catch (e: Exception) {
				state = state.copy(error = e.message ?: "Error al guardar usuario")
				e.printStackTrace()
			} finally {
				state = state.copy(isLoading = false)
			}
		}
	}
	
	fun getUsers() = viewModelScope.launch {
		state = state.copy(isLoading = true, error = null)
		try {
			val res = getUsersUseCase.invoke()
			state = state.copy(userList = res.sortedByDescending { it.id })
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
			updateUserUseCase(user)
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
			
			
			state = state.copy()
		} catch (e: Exception) {
			state = state.copy(error = e.message ?: "Error al eliminar usuario")
			e.printStackTrace()
		} finally {
			state = state.copy(isLoading = false)
		}
	}

	
	data class PeopleState(
		val userList: List<Person> = emptyList(),
		val isLoading: Boolean = false,
		val error: String? = null,
	)
}
