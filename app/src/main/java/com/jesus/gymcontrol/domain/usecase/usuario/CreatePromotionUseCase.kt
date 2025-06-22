package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.model.Promotion
import com.jesus.gymcontrol.domain.repository.PromotionRepository
import javax.inject.Inject

class CreatePromotionUseCase @Inject constructor(
	private val promotionRepository: PromotionRepository,
) {
	suspend operator fun invoke(promotion: Promotion): Result<Unit> {
		return promotionRepository.createPromotion(promotion)
	}
	
}