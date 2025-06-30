package com.jesus.gymcontrol.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.jesus.gymcontrol.domain.model.Pago
import com.jesus.gymcontrol.domain.model.Payment
import com.jesus.gymcontrol.domain.repository.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
	private val firestore: FirebaseFirestore,
) : PaymentRepository {
	
	override suspend fun addPago(pago: Pago): Result<Unit> = try {
		Log.d("addPago", "== INICIO DE addPago ==")
		
		val gymRef = firestore.collection("gimnasios").document(pago.gimnasioCode)
		
		// 1️⃣ Obtener duración de la membresía desde Firestore
		val duracionDias = gymRef
			.collection("membresias")
			.document(pago.membershipId)
			.get()
			.await()
			.getLong("duracionDias")
			?.toInt() ?: 0
		
		// 2️⃣ Calcular fecha de vencimiento con la duración obtenida
		val fechaVencimientoFinal = pago.fechaVencimiento.takeIf { it > 0 }
			?: pago.date + duracionDias * 24 * 60 * 60 * 1000L
		
		// 3️⃣ Crear el mapa del pago
		val pagoMap = mutableMapOf(
			"id" to pago.id,
			"userId" to pago.userId,
			"name" to pago.name,
			"idcard" to pago.idcard,
			"membershipId" to pago.membershipId,
			"membershipName" to pago.membershipName,
			"typepayment" to pago.tipepayment,
			"amount" to pago.amount,
			"amountDollar" to pago.amountDollar,
			"amountBs" to pago.amountBs,
			"description" to pago.description,
			"reference" to pago.reference,
			"date" to pago.date,
			"duracionDias" to duracionDias,
			"fechaVencimiento" to fechaVencimientoFinal,
			"gimnasioCode" to pago.gimnasioCode,
			"promocionId" to pago.promocionId,
			"promocionNombre" to pago.promocionNombre,
			"promocionDescripcion" to pago.promocionDescripcion,
			"promocionPorcentajeDescuento" to pago.promocionDescuento
		).filterValues { it != null }
		
		val pagoRef = gymRef.collection("pagos").document(pago.id)
		
		val userPagoRef = firestore.collection("users")
			.document(pago.userId)
			.collection("gimnasios")
			.document(pago.gimnasioCode)
			.collection("pagos")
			.document(pago.id)
		
		val userRef = firestore.collection("users").document(pago.userId)
		val gymUserRef = gymRef.collection("usuarios").document(pago.userId)
		
		val membershipUsersRef = gymRef
			.collection("membresias")
			.document(pago.membershipId)
			.collection("usuarios")
			.document(pago.userId)
		
		val promoUserRef = pago.promocionId?.let { promoId ->
			gymRef.collection("promociones")
				.document(promoId)
				.collection("usuarios")
				.document(pago.userId)
		}
		
		// 4️⃣ Guardar todos los datos en un batch
		firestore.runBatch { batch ->
			batch.set(pagoRef, pagoMap)
			batch.set(userPagoRef, pagoMap)
			
			batch.set(
				gymUserRef,
				mapOf(
					"name" to pago.name,
					"idcard" to pago.idcard,
					"state" to "activo",
					"paymentdate" to pago.date,
					"membership" to pago.membershipName,
					"fechaVencimiento" to fechaVencimientoFinal
				),
				SetOptions.merge()
			)
			
			batch.set(
				membershipUsersRef,
				mapOf(
					"name" to pago.name,
					"idcard" to pago.idcard,
					"paymentdate" to pago.date,
					"state" to "activo"
				)
			)
			
			batch.update(
				userRef,
				mapOf(
					"state" to "activo",
					"membership" to pago.membershipName,
					"promocion" to pago.promocionNombre
				)
			)
			
			promoUserRef?.let {
				batch.set(
					it,
					mapOf(
						"userId" to pago.userId,
						"name" to pago.name,
						"idcard" to pago.idcard,
						"paymentdate" to pago.date,
						"state" to "activo",
						"membershipId" to pago.membershipId,
						"membershipName" to pago.membershipName
					)
				)
			}
		}.await()
		
		Log.d("addPago", "✅ Pago agregado correctamente")
		Result.success(Unit)
	} catch (e: Exception) {
		Log.e("addPago", "❌ Error al agregar pago: ${e.localizedMessage}", e)
		Result.failure(e)
	}
	
	
	override suspend fun getAllPayments(gymCode: String): List<Payment> =
		withContext(Dispatchers.IO) {
			try {
				val paymentsSnapshot = firestore
					.collection("gimnasios")
					.document(gymCode)
					.collection("pagos")
					.orderBy("date", Query.Direction.DESCENDING)
					.get()
					.await()

				paymentsSnapshot.documents.mapNotNull { doc ->
					val data = doc.data ?: return@mapNotNull null
					try {
						Payment(
							id = data["id"] as? String ?: "",
							userId = data["userId"] as? String ?: "",
							name = data["name"] as? String ?: "",
							idCard = data["idcard"] as? String ?: "",
							membershipId = data["membershipId"] as? String ?: "",
							membershipName = data["membershipName"] as? String ?: "",
							paymentType = data["typepayment"] as? String ?: "",
							amount = (data["amount"] as? Number)?.toDouble() ?: 0.0,
							amountDollar = (data["amountDollar"] as? Number)?.toDouble() ?: 0.0,
							amountBs = (data["amountBs"] as? Number)?.toDouble() ?: 0.0,
							description = data["description"] as? String ?: "",
							reference = data["reference"] as? String,
							date = (data["date"] as? Number)?.toLong() ?: 0L,
							gymCode = data["gimnasioCode"] as? String ?: "",
							promocionNombre = data["promocionNombre"] as? String,
							promocionPorcentajeDescuento = (data["promocionPorcentajeDescuento"] as? Number)?.toDouble()
						)
					} catch (e: Exception) {
						Log.e("getAllPayments", "Error parsing document ${doc.id}", e)
						null
					}
				}
			} catch (e: Exception) {
				Log.e("getAllPayments", "Firestore fetch failed", e)
				emptyList()
			}
		}
}