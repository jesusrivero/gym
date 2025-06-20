package com.jesus.gymcontrol.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.domain.model.Pago
import com.jesus.gymcontrol.domain.repository.PagoRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PagoRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PagoRepository {
	
	override suspend fun addPago(pago: Pago): Result<Unit> = try {
		val pagoMap = mapOf(
			"id" to pago.id,
			"userId" to pago.userId,
			"Name" to pago.userName,
			"idcard" to pago.userCedula,
			"membershipId" to pago.membershipId,
			"membershipName" to pago.membershipName,
			"tipepayment" to pago.tipoPago,
			"amount" to pago.monto,
			"description" to pago.descripcion,
			"reference" to pago.referencia,
			"date" to pago.fecha,
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
				"name" to pago.userName,
				"idcard" to pago.userCedula,
				"paymentdate" to pago.fecha
			))
			
			// ✅ Usuario en membresía/usuarios con campo state
			batch.set(membershipUsersRef, mapOf(
				"name" to pago.userName,
				"idcard" to pago.userCedula,
				"paymentdate" to pago.fecha,
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
}