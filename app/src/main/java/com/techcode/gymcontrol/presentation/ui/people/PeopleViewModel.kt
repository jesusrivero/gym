package com.techcode.gymcontrol.presentation.ui.people

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techcode.gymcontrol.data.db.dao.UsuariosDatabaseDao
import com.techcode.gymcontrol.data.db.entity.PersonEntity
import com.techcode.gymcontrol.domain.model.Person
import com.techcode.gymcontrol.domain.usecase.usuario.RegistrarUsuarioUseCase
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PeopleViewModel @Inject constructor(
	private val registrarUsuarioUseCase: RegistrarUsuarioUseCase,
	private val dao: UsuariosDatabaseDao

) : ViewModel() {


	var state by mutableStateOf(PeopleState())
		private set

	init {

	
	}

	fun saveUser(user: Person) =
		viewModelScope.launch {
			registrarUsuarioUseCase(user)
		}
	
	fun getUsers(){
		viewModelScope.launch {
			dao.obtenerUsuarios().collectLatest {
				state = state.copy(
					userList = it
				)
			}
		}
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
