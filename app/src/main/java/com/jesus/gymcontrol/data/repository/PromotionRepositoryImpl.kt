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
	
	override suspend fun updatePromotion(promotion: Promotion): Result<Unit> = try {
		val uid = auth.currentUser?.uid
			?: return Result.failure(Exception("Usuario no autenticado"))
		
		val userDoc = firestore.collection("users").document(uid).get().await()
		val gymCode = userDoc.getString("gimnasioCode")
			?: return Result.failure(Exception("No se encontró el gimnasioCode del usuario"))
		
		firestore.collection("gimnasios")
			.document(gymCode)
			.collection("promociones")
			.document(promotion.id)
			.set(promotion)
			.await()
		
		Result.success(Unit)
	} catch (e: Exception) {
		Result.failure(e)
	}
	
	override suspend fun deletePromotion(promotion: Promotion): Result<Unit> {
		return try {
			val uid = auth.currentUser?.uid
				?: return Result.failure(Exception("Usuario no autenticado"))
			
			val userDoc = firestore.collection("users").document(uid).get().await()
			val gymCode = userDoc.getString("gimnasioCode")
				?: return Result.failure(Exception("No se encontró el gimnasioCode del usuario"))
			
			val usuariosSnapshot = firestore.collection("gimnasios")
				.document(gymCode)
				.collection("promociones")
				.document(promotion.id)
				.collection("usuarios")
				.get()
				.await()
			
			// Verificamos si hay algún usuario activo
			val hayUsuariosActivos = usuariosSnapshot.documents.any { doc ->
				doc.getString("state") == "activo"
			}
			
			if (hayUsuariosActivos) {
				return Result.failure(Exception("No se puede eliminar la promoción: hay usuarios con estado activo."))
			}
			
			// Si no hay usuarios activos, se permite eliminar la promoción
			firestore.collection("gimnasios")
				.document(gymCode)
				.collection("promociones")
				.document(promotion.id)
				.delete()
				.await()
			
			Result.success(Unit)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}
	
	override suspend fun getUsersCountByPromotion(gymCode: String): Result<Map<String, Int>> = try {
		val promotionsRef = firestore.collection("gimnasios")
			.document(gymCode)
			.collection("promociones")
		
		val promotionsSnapshot = promotionsRef.get().await()
		
		val counts = mutableMapOf<String, Int>()
		
		for (promoDoc in promotionsSnapshot.documents) {
			val promoId = promoDoc.id
			
			// 🔍 Filtrar usuarios por estado "activo"
			val usersSnapshot = promotionsRef
				.document(promoId)
				.collection("usuarios")
				.whereEqualTo("state", "activo") // ✅ solo los activos
				.get()
				.await()
			
			counts[promoId] = usersSnapshot.size()
		}
		
		Result.success(counts)
	} catch (e: Exception) {
		Result.failure(e)
	}
	
	override suspend fun togglePromotionState(promotion: Promotion): Result<Unit> = try {
		val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Usuario no autenticado"))
		val userDoc = firestore.collection("users").document(uid).get().await()
		val gymCode = userDoc.getString("gimnasioCode") ?: return Result.failure(Exception("No se encontró el gimnasioCode del usuario"))
		
		val newState = !promotion.activo
		
		firestore.collection("gimnasios")
			.document(gymCode)
			.collection("promociones")
			.document(promotion.id)
			.update("activo", newState)
			.await()
		
		Result.success(Unit)
	} catch (e: Exception) {
		Result.failure(e)
	}

	
}
