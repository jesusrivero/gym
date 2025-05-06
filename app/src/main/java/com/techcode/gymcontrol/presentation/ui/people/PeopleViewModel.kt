package com.techcode.gymcontrol.presentation.ui.people

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techcode.gymcontrol.data.db.dao.UsuariosDatabaseDao
import com.techcode.gymcontrol.data.db.entity.PersonEntity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PeopleViewModel(
	private val dao: UsuariosDatabaseDao
) : ViewModel() {
	var state by mutableStateOf(PeopleState())
		private set

	init {

		viewModelScope.launch {
			dao.obtenerUsuarios().collectLatest {
				state = state.copy(
					userList = it
				)
			}
		}
	}

	fun saveUser(user: PersonEntity) =
		viewModelScope.launch {
			dao.agregarUsuario(usuario = user)
		}

	fun updateUser(user: PersonEntity) =
		viewModelScope.launch {
			dao.actualizarUsuario(usuario = user)
		}

	fun deleteUser(user: PersonEntity) =
		viewModelScope.launch {
			dao.borrarUsuario(usuario = user)
		}

	data class PeopleState(
		val userList: List<PersonEntity> = emptyList()
	)

}