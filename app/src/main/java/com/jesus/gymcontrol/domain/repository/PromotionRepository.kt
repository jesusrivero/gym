package com.jesus.gymcontrol.domain.repository

import com.jesus.gymcontrol.domain.model.Promotion

interface PromotionRepository {
	suspend fun createPromotion(promotion:Promotion): Result<Unit>

suspend fun getPromotions(): Result<List<Promotion>>
	
	suspend fun updatePromotion(promotion: Promotion, forceRecalculate: Boolean = false):Result<Unit>
	
	suspend fun deletePromotion(promotion: Promotion) : Result<Unit>
	
	suspend fun getUsersCountByPromotion(gymCode: String): Result<Map<String, Int>>
	
	suspend fun togglePromotionState(promotion: Promotion): Result<Unit>

}