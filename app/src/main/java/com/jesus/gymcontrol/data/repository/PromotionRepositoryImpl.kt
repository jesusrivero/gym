package com.jesus.gymcontrol.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.domain.model.Promotion
import com.jesus.gymcontrol.domain.repository.PromotionRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PromotionRepositoryImpl @Inject constructor(
	private val firestore: FirebaseFirestore,
	private val auth: FirebaseAuth,
) : PromotionRepository {
	
	
	override suspend fun createPromotion(promotion: Promotion): Result<Unit> = try {
		val uid = auth.currentUser?.uid
			?: return Result.failure(Exception("Usuario no autenticado"))
		
		val userDoc = firestore.collection("users").document(uid).get().await()
		val gymCode = userDoc.getString("gimnasioCode")
			?: return Result.failure(Exception("No se encontró el gimnasioCode del usuario"))
		
		val id = firestore.collection("gimnasios")
			.document(gymCode)
			.collection("promociones")
			.document().id
		
		val newPromotion = promotion.copy(id = id, gimnasioCode = gymCode)
		
		firestore.collection("gimnasios")
			.document(gymCode)
			.collection("promociones")
			.document(id)
			.set(newPromotion)
			.await()
		
		Result.success(Unit)
	} catch (e: Exception) {
		Result.failure(e)
	}
	
	override suspend fun getPromotions(): Result<List<Promotion>> = try {
		val uid = auth.currentUser?.uid
			?: return Result.failure(Exception("Usuario no autenticado"))
		
		val userDoc = firestore.collection("users").document(uid).get().await()
		val gymCode = userDoc.getString("gimnasioCode")
			?: return Result.failure(Exception("No se encontró el gimnasioCode del usuario"))
		
		val snapshot = firestore.collection("gimnasios")
			.document(gymCode)
			.collection("promociones")
			.get()
			.await()
		
		val promotions = snapshot.documents.mapNotNull { doc ->
			doc.toObject(Promotion::class.java)?.copy(id = doc.id)
		}
		
		Result.success(promotions)
	} catch (e: Exception) {
		Result.failure(e)
	}
	
	override suspend fun updatePromotion(promotion: Promotion) {
	
	}
	
}
