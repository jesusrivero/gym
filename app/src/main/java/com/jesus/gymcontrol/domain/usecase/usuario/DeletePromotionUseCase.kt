package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.model.Promotion
import com.jesus.gymcontrol.domain.repository.PromotionRepository

class DeletePromotionUseCase(
	private val repository: PromotionRepository
) {
	suspend operator fun invoke(promotion: Promotion): Result<Unit> {
		return repository.deletePromotion(promotion)
	}
}