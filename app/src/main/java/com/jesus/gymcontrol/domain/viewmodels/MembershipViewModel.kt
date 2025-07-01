package com.jesus.gymcontrol.domain.viewmodels

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MembershipViewModel @Inject constructor(
	private val createMembershipUseCase: CreateMembershipUseCase,
	private val getUseCase: GetMembershipsUseCase,
	private val deleteUseCase: DeleteMembershipUseCase,
	private val getMembershipsWithUserCountUseCase: GetMembershipsWithUserCountUseCase,
	private val editMembershipUseCase: EditMembershipUseCase,
) : ViewModel() {
	
	
	var membershipsSummary by mutableStateOf<List<MembershipWithCount>>(emptyList())
		private set
	
	var isLoading by mutableStateOf(false)
		private set
	
	var memberships by mutableStateOf<List<Membership>>(emptyList())
		private set
	
	var isSuccess by mutableStateOf(false)
		private set
	
	var errorMessage by mutableStateOf<String?>(null)
		private set
	
	var isFirstLoadDone by mutableStateOf(true)
		private set
	
	
	fun createMembership(membership: Membership) {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			
			val result = createMembershipUseCase(membership)
			isLoading = false
			
			result.onSuccess {
				loadMemberships()
			}.onFailure {
				errorMessage = it.message
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
	
	
	
	fun deleteMembership(membership: Membership) {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			
			deleteUseCase(membership).onSuccess {
				loadMemberships()
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
			isFirstLoadDone = true
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
				loadMemberships()
			}.onFailure {
				errorMessage = it.message
				}
		}
	}

}