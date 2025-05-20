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
import com.techcode.gymcontrol.domain.usecase.usuario.deleteUserUseCase
import com.techcode.gymcontrol.domain.usecase.usuario.getUsersUseCase
import com.techcode.gymcontrol.domain.usecase.usuario.updateUserUseCase
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PeopleViewModel @Inject constructor(
	private val registrarUsuarioUseCase: RegistrarUsuarioUseCase,
	private val updateUserUseCase: updateUserUseCase,
	private val deleteUserUseCase:deleteUserUseCase,
  private val getUsersUseCase:getUsersUseCase,
	private val dao: UsuariosDatabaseDao

) : ViewModel() {


	var state by mutableStateOf(PeopleState())
		private set


	fun saveUser(user: Person) =
		viewModelScope.launch {
			registrarUsuarioUseCase.invoke(user)
		}
	
	fun getUsers(){
		viewModelScope.launch {
			
			val res: List<Person> = getUsersUseCase.invoke()
			state = state.copy(
				userList = res
			)
		}
	}

	fun updateUser(user: Person) =
		viewModelScope.launch {
			updateUserUseCase.invoke(user)
			
		}

	fun deleteUser(user: Person) =
		viewModelScope.launch {
			deleteUserUseCase.invoke(user)
		}

	data class PeopleState(
		val userList: List<Person> = emptyList()
	)
}
