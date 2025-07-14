package com.jesus.gymcontrol.domain.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesus.gymcontrol.domain.model.Membership
import com.jesus.gymcontrol.domain.model.MembershipWithCount
import com.jesus.gymcontrol.domain.usecase.usuario.CreateMembershipUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.DeleteMembershipUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.EditMembershipUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetMembershipsUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetMembershipsWithUserCountUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.membership.ToggleMembershipStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MembershipViewModel @Inject constructor(
	private val createMembershipUseCase: CreateMembershipUseCase,
	private val getUseCase: GetMembershipsUseCase,
	private val deleteUseCase: DeleteMembershipUseCase,
	private val getMembershipsWithUserCountUseCase: GetMembershipsWithUserCountUseCase,
	private val editMembershipUseCase: EditMembershipUseCase,
	private val toggleMembershipStateUseCase: ToggleMembershipStateUseCase
) : ViewModel() {
	
	var membershipsSummary by mutableStateOf<List<MembershipWithCount>>(emptyList())
		private set
	
	var isLoading by mutableStateOf(true)  // <--- empieza en true
		private set
	
	var memberships by mutableStateOf<List<Membership>>(emptyList())
		private set
	
	var isSuccess by mutableStateOf(false)
		private set
	
	var errorMessage by mutableStateOf<String?>(null)
		private set
	
	var isFirstLoadDone by mutableStateOf(false)  // <--- empieza en false
		private set
	
	private val _membershipActionMessage = mutableStateOf<String?>(null)
	val membershipActionMessage: State<String?> = _membershipActionMessage
	
	private val _isActionSuccess = MutableStateFlow<Boolean?>(null)
	val isActionSuccess: StateFlow<Boolean?> = _isActionSuccess
	
	init {
		// Carga inicial al crear el ViewModel
		loadMembershipsSummary()
	}
	
	fun createMembership(membership: Membership) {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			
			val result = createMembershipUseCase(membership)
			isLoading = false
			
			result.onSuccess {
				_membershipActionMessage.value = "Membresía creada exitosamente"
				_isActionSuccess.value = true
				loadMemberships()
				loadMembershipsSummary()
			}.onFailure {
				errorMessage = it.message
				_membershipActionMessage.value = it.message
				_isActionSuccess.value = false
			}
		}
	}
	
	fun loadMemberships() {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			
			getUseCase().onSuccess {
				memberships = it
			}.onFailure {
				errorMessage = it.message
			}
			isLoading = false
		}
	}
	
	fun loadMembershipsSummary() {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			
			getMembershipsWithUserCountUseCase().onSuccess {
				membershipsSummary = it
			}.onFailure {
				errorMessage = it.message
			}
			isFirstLoadDone = true  // <--- solo aquí lo marcas como cargado
			isLoading = false
		}
	}
	
	fun editMembership(membership: Membership) {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			
			val result = editMembershipUseCase(membership)
			isLoading = false
			
			result.onSuccess {
				_membershipActionMessage.value = "Membresía actualizada correctamente"
				loadMembershipsSummary()
				_isActionSuccess.value = true
				loadMemberships()
			}.onFailure {
				errorMessage = it.message
				_membershipActionMessage.value = it.message
				_isActionSuccess.value = false
			}
		}
	}
	
	fun toggleMembershipState(membership: Membership) {
		viewModelScope.launch {
			val newState = !membership.activo
			
			val result = toggleMembershipStateUseCase(
				membershipId = membership.id,
				gymCode = membership.gimnasioCode,
				newState = newState
			)
			
			if (result.isSuccess) {
				_membershipActionMessage.value =
					"Membresía ${if (newState) "activada" else "desactivada"} correctamente"
				_isActionSuccess.value = true
				loadMembershipsSummary()
			} else {
				_membershipActionMessage.value = result.exceptionOrNull()?.message ?: "Error desconocido"
				_isActionSuccess.value = false
			}
		}
	}
	
	fun clearMembershipMessage() {
		_membershipActionMessage.value = null
		_isActionSuccess.value = null
	}
	
	
	//		VOY A DEJAR ESTA FUNCION PARA UNA FUTURA IMPLEMENTACION
	fun deleteMembership(membership: Membership) {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			val result = deleteUseCase(membership)
			isLoading = false
			result.onSuccess {
				_membershipActionMessage.value = "Membresía eliminada exitosamente"
				_isActionSuccess.value = true
				loadMemberships()
				loadMembershipsSummary()
			}
			deleteUseCase(membership).onSuccess {
				loadMemberships()
			}.onFailure {
				_membershipActionMessage.value = "La membresia no se puede eliminar, tiene usuarios inscritos"
			}
			isLoading = false
		}
	}
}