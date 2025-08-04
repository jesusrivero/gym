package com.jesus.gymcontrol.data.repository

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import com.jesus.gymcontrol.domain.model.Pago
import com.jesus.gymcontrol.domain.model.Payment
import com.jesus.gymcontrol.domain.repository.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
	private val firestore: FirebaseFirestore,
) : PaymentRepository {
	
	
	override suspend fun addPago(pago: Pago): Result<Unit> = try {
		Log.d("addPago", "== INICIO DE addPago ==")
		
		val gymRef = firestore.collection("gimnasios").document(pago.gimnasioCode)
		
		// 🔍 1. Obtener duración de la membresía
		val duracionDias = gymRef
			.collection("membresias")
			.document(pago.membershipId)
			.get()
			.await()
			.getLong("duracionDias")
			?.toInt() ?: 0
		
		// 🔍 2. CONSULTAR la fecha de vencimiento actual del usuario
		val gymUserRef = gymRef.collection("usuarios").document(pago.userId)
		val currentUserData = gymUserRef.get().await()
		
		val fechaVencimientoActual = if (currentUserData.exists()) {
			currentUserData.getLong("fechaVencimiento") ?: 0L
		} else {
			0L
		}
		
		Log.d("addPago", "📅 Fecha de pago: ${java.util.Date(pago.date)}")
		Log.d("addPago", "📅 Fecha vencimiento actual: ${if (fechaVencimientoActual > 0) java.util.Date(fechaVencimientoActual) else "No tiene"}")
		Log.d("addPago", "📅 Duración nueva membresía: $duracionDias días")
		
		// 🧮 3. CALCULAR la nueva fecha de vencimiento según la lógica de negocio
		val fechaVencimientoFinal = when {
			// Si tiene membresía activa y no ha vencido: extender desde fecha actual de vencimiento
			fechaVencimientoActual > System.currentTimeMillis() -> {
				Log.d("addPago", "✅ Usuario con membresía activa - Extendiendo desde vencimiento actual")
				fechaVencimientoActual + duracionDias * 24 * 60 * 60 * 1000L
			}
			
			// Si no tiene membresía activa o ya venció: calcular desde fecha de pago
			else -> {
				Log.d("addPago", "✅ Usuario sin membresía activa - Calculando desde fecha de pago")
				pago.date + duracionDias * 24 * 60 * 60 * 1000L
			}
		}
		
		Log.d("addPago", "📅 Fecha vencimiento FINAL: ${java.util.Date(fechaVencimientoFinal)}")
		
		val pagoMap = mutableMapOf(
			"id" to pago.id,
			"userId" to pago.userId,
			"name" to pago.name,
			"lastname" to pago.lastname,
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
			"fechaVencimiento" to fechaVencimientoFinal, // ✅ Nueva fecha calculada correctamente
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
		val membershipUsersRef = gymRef
			.collection("membresias")
			.document(pago.membershipId)
			.collection("usuarios")
			.document(pago.userId)
		
		val membershipRef = gymRef.collection("membresias").document(pago.membershipId)
		
		val promoRef = pago.promocionId?.let { promoId ->
			gymRef.collection("promociones").document(promoId)
		}
		val promoUserRef = promoRef?.collection("usuarios")?.document(pago.id)
		
		// 🔁 Desactivar membresías anteriores
		val membresiasSnapshot = gymRef.collection("membresias").get().await()
		for (doc in membresiasSnapshot.documents) {
			val usuarioRef = gymRef
				.collection("membresias")
				.document(doc.id)
				.collection("usuarios")
				.document(pago.userId)
			
			val usuarioDoc = usuarioRef.get().await()
			if (usuarioDoc.exists() && usuarioDoc.getString("state") == "activo") {
				usuarioRef.update("state", "inactivo")
			}
		}
		
		firestore.runBatch { batch ->
			batch.set(pagoRef, pagoMap)
			batch.set(userPagoRef, pagoMap)
			
			batch.set(
				gymUserRef,
				mapOf(
					"name" to pago.name,
					"lastname" to pago.lastname,
					"idcard" to pago.idcard,
					"state" to "activo",
					"paymentdate" to pago.date,
					"membership" to pago.membershipName,
					"fechaVencimiento" to fechaVencimientoFinal // ✅ Usar fecha calculada correctamente
				),
				SetOptions.merge()
			)
			
			batch.set(
				membershipUsersRef,
				mapOf(
					"name" to pago.name,
					"lastname" to pago.lastname,
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
			
			// ✅ Incrementa cantidadUsuarios en promoción
			promoRef?.let {
				batch.update(it, "cantidadUsuarios", FieldValue.increment(1))
			}
			
			// ✅ Guarda en colección usuarios de la promoción
			promoUserRef?.let {
				batch.set(
					it,
					mapOf(
						"userId" to pago.userId,
						"paymentdate" to pago.date
					)
				)
			}
			
			// ✅ Incrementa cantidadUsuarios en membresía
			batch.update(membershipRef, "cantidadUsuarios", FieldValue.increment(1))
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
					.limit(100)
					.get()
					.await()
				
				paymentsSnapshot.documents.mapNotNull { doc ->
					val data = doc.data ?: return@mapNotNull null
					try {
						Payment(
							id = data["id"] as? String ?: "",
							userId = data["userId"] as? String ?: "",
							name = data["name"] as? String ?: "",
							lastname = data["lastname"] as? String ?: "",
							idCard = data["idcard"] as? String ?: "",
							membershipId = data["membershipId"] as? String ?: "",
							membershipName = data["membershipName"] as? String ?: "",
							paymentType = data["typepayment"] as? String ?: "",
							amount = (data["amount"] as? Number)?.toDouble() ?: 0.0,
							amountDollar = (data["amountDollar"] as? Number)?.toDouble() ?: 0.0,
							amountBs = (data["amountBs"] as? Number)?.toDouble() ?: 0.0,
							description = data["description"] as? String ?: "",
							reference = data["reference"] as? String,
							fechaVencimiento = (data["fechaVencimiento"] as? Number)?.toLong() ?: 0L,
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
	
	override suspend fun calcularNuevaFechaVencimiento(
		userId: String,
		gymCode: String,
		membershipDays: Int,
	): Long {
		val userRef = firestore
			.collection("users")
			.document(userId)
			.collection("gimnasios")
			.document(gymCode)
		
		val snapshot = userRef.get().await()
		val fechaActual = snapshot.getLong("fechaVencimiento") ?: 0L
		val hoy = System.currentTimeMillis()
		val base = if (fechaActual > hoy) fechaActual else hoy
		return base + (membershipDays * 24 * 60 * 60 * 1000L)
	}
	
	
	override suspend fun generarDescripcionPago(
		userId: String,
		gymCode: String,
		nuevaMembresia: String,
	): String {
		Log.d(
			"PagoDebug",
			"Iniciando generación para userId=$userId gymCode=$gymCode nuevaMembresia=$nuevaMembresia"
		)
		
		val userGymDoc = firestore
			.collection("gimnasios")
			.document(gymCode)
			.collection("usuarios")
			.document(userId)
			.get(Source.SERVER) // 👈 lee del servidor
			.await()
		
		Log.d("PagoDebug", "Documento existe: ${userGymDoc.exists()}")
		Log.d("PagoDebug", "Datos completos: ${userGymDoc.data}")
		
		val membershipActual = userGymDoc.data?.get("membership")?.toString()
		Log.d("PagoDebug", "membershipActual=$membershipActual")
		
		val fechaVencimiento = userGymDoc.getLong("fechaVencimiento") ?: 0L
		Log.d("PagoDebug", "fechaVencimiento=$fechaVencimiento")
		
		val hoy = System.currentTimeMillis()
		val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
		val fechaStr = sdf.format(Date(fechaVencimiento))
		
		return when {
			membershipActual == null -> {
				Log.d("PagoDebug", "Caso: No tenía membresía previa")
				"Este pago inicia la membresía $nuevaMembresia."
			}
			
			fechaVencimiento < hoy -> {
				Log.d("PagoDebug", "Caso: Membresía vencida")
				"Este pago reinicia la membresía $nuevaMembresia. La membresía anterior $membershipActual estaba vencida desde $fechaStr."
			}
			
			else -> {
				Log.d("PagoDebug", "Caso: Extiende membresía")
				"Este pago extiende la membresía $membershipActual (vence $fechaStr) con la nueva membresía $nuevaMembresia."
			}
		}
	}
	
}