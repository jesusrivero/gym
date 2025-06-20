package com.jesus.gymcontrol.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.domain.model.Pago
import com.jesus.gymcontrol.domain.model.Payment
import com.jesus.gymcontrol.domain.repository.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PaymentRepository {
	
	override suspend fun addPago(pago: Pago): Result<Unit> = try {
		val pagoMap = mapOf(
			"id" to pago.id,
			"userId" to pago.userId,
			"name" to pago.name,
			"idcard" to pago.idcard,
			"membershipId" to pago.membershipId,
			"membershipName" to pago.membershipName,
			"typepayment" to pago.tipepayment,
			"amount" to pago.amount,
			"description" to pago.description,
			"reference" to pago.reference,
			"date" to pago.date,
			"gimnasioCode" to pago.gimnasioCode
		)
		
		val gymRef = firestore.collection("gimnasios").document(pago.gimnasioCode)
		
		val pagoRef = gymRef.collection("pagos").document(pago.id)
		
		val userPagoRef = firestore.collection("users")
			.document(pago.userId)
			.collection("gimnasios")
			.document(pago.gimnasioCode)
			.collection("pagos")
			.document(pago.id)
		
		// Usuario en gym/usuarios/{membershipId}/personas/{userId}
		val membresiaUserRef = gymRef
			.collection("usuarios")
			.document(pago.membershipId)
			.collection("personas")
			.document(pago.userId)
		
		// ✅ Usuario en gym/membresias/{membershipId}/usuarios/{userId}
		val membershipUsersRef = gymRef
			.collection("membresias")
			.document(pago.membershipId)
			.collection("usuarios")
			.document(pago.userId)
		
		// Documento del usuario
		val userRef = firestore.collection("users").document(pago.userId)
		
		firestore.runBatch { batch ->
			// Registro de pago en gimnasio
			batch.set(pagoRef, pagoMap)
			
			// Registro de pago en usuario
			batch.set(userPagoRef, pagoMap)
			
			// Usuario en gym/usuarios/{membershipId}/personas
			batch.set(membresiaUserRef, mapOf(
				"name" to pago.name,
				"idcard" to pago.idcard,
				"paymentdate" to pago.date
			))
			
			// ✅ Usuario en membresía/usuarios con campo state
			batch.set(membershipUsersRef, mapOf(
				"name" to pago.name,
				"idcard" to pago.idcard,
				"paymentdate" to pago.date,
				"state" to "activo"
			))
			
			// ✅ Actualización del estado del usuario y su membresía
			batch.update(userRef, mapOf(
				"state" to "activo",
				"membership" to pago.membershipName
			))
		}.await()
		
		Result.success(Unit)
	} catch (e: Exception) {
		Result.failure(e)
	}
	
	override suspend fun getAllPayments(gymCode: String): List<Payment> = withContext(Dispatchers.IO) {
		val paymentsSnapshot = firestore
			.collection("gimnasios")
			.document(gymCode)
			.collection("pagos")
			.get()
			.await()
		Log.d("PaymentRepositoryImpl", "getAllPayments: $paymentsSnapshot")
		return@withContext paymentsSnapshot.documents.mapNotNull { doc ->
			doc.toObject(Payment::class.java)?.copy(id = doc.id)
		}
	}
}