package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.model.Promotion
import com.jesus.gymcontrol.domain.repository.PromotionRepository
import javax.inject.Inject
class UpdatePromotionUseCase @Inject constructor(
	private val repository: PromotionRepository
) {
	suspend operator fun invoke(
		promotion: Promotion,
		forceRecalculate: Boolean = false
	): Result<Unit> {
		return repository.updatePromotion(promotion, forceRecalculate)
	}
}