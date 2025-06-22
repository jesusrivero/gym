package com.jesus.gymcontrol.domain.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.model.Promotion
import com.jesus.gymcontrol.domain.usecase.usuario.CreatePromotionUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.GetPromotionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromotionViewModel @Inject constructor(
	private val createPromotionUseCase: CreatePromotionUseCase,
	private val getPromotionsUseCase: GetPromotionUseCase,
	private val sessionManager: SessionManager
) : ViewModel() {
	
	var isLoading by mutableStateOf(false)
		private set
	
	var successMessage by mutableStateOf<String?>(null)
		private set
	
	var errorMessage by mutableStateOf<String?>(null)
		private set
	
	var promotions by mutableStateOf<List<Promotion>>(emptyList())
		private set
	
	
	
	fun createPromotion(promotion: Promotion) {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			
			val result = createPromotionUseCase(promotion)
			isLoading = false
			
			result.onFailure {
				errorMessage = it.message
			}
			}
		}
	
	fun loadPromotions() {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			
			val result = getPromotionsUseCase()
			isLoading = false
			
			result.onSuccess {
				promotions = it
			}.onFailure {
				errorMessage = it.message
			}
		}
	}
	
	
}