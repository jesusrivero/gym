package com.jesus.gymcontrol.domain.usecase.usuario.getDates

import com.jesus.gymcontrol.domain.repository.PromotionRepository
import javax.inject.Inject

class GetUsersCountByPromotionUseCase @Inject constructor(
	private val repository: PromotionRepository,
) {
	suspend operator fun invoke(gymCode: String): Result<Map<String, Int>> {
		return repository.getUsersCountByPromotion(gymCode)
		
	}
	
}