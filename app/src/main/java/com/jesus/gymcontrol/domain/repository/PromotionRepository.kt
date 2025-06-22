package com.jesus.gymcontrol.domain.repository

import com.jesus.gymcontrol.domain.model.Promotion

interface PromotionRepository {
	suspend fun createPromotion(promotion:Promotion): Result<Unit>

suspend fun getPromotions(): Result<List<Promotion>>

	suspend fun updatePromotion(promotion: Promotion)
//	suspend fun deletePromotion(promotion: Promotion)

}