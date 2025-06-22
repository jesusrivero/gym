package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.model.Promotion
import com.jesus.gymcontrol.domain.repository.PromotionRepository
import javax.inject.Inject

class GetPromotionUseCase @Inject constructor(
	private val Repository: PromotionRepository,
) {
	suspend operator fun invoke(): Result<List<Promotion>> {
		return Repository.getPromotions()
	}
}
