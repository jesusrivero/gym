package com.jesus.gymcontrol.data.repository.report

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jesus.gymcontrol.domain.model.reportModel.ReporteCliente
import com.jesus.gymcontrol.domain.model.reportModel.ReporteMembresia
import com.jesus.gymcontrol.domain.model.reportModel.ReportePago
import com.jesus.gymcontrol.domain.model.reportModel.ReportePromocion
import com.jesus.gymcontrol.domain.repository.report.ReportesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class ReportesRepositoryImpl @Inject constructor(
	private val firestore: FirebaseFirestore,
) : ReportesRepository {
	
	@RequiresApi(Build.VERSION_CODES.O)
	override suspend fun getPagosReporte(
		gymCode: String,
		desde: LocalDate?,
		hasta: LocalDate? 
	): List<ReportePago> = withContext(Dispatchers.IO) {
		
		val collectionRef = firestore.collection("gimnasios")
			.document(gymCode)
			.collection("pagos")
		
		val desdeMillis = desde?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
		val hastaMillis = hasta?.atTime(23, 59, 59)?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
		
		var query: Query = collectionRef
		if (desdeMillis != null) query = query.whereGreaterThanOrEqualTo("date", desdeMillis)
		if (hastaMillis != null) query = query.whereLessThanOrEqualTo("date", hastaMillis)
		
		val snapshot = query.get().await()
		
		val pagos = snapshot.documents.mapNotNull { doc ->
			val data = doc.data ?: return@mapNotNull null
			
			val fecha = (data["date"] as? Number)?.toLong()
			
			ReportePago(
				nombreCliente = data["name"] as? String ?: "",
				apellidoCliente = data["lastname"] as? String ?: "",
				cedula = data["idcard"] as? String ?: "",
				membresia = data["membershipName"] as? String ?: "",
				tipoPago = data["typepayment"] as? String ?: "",
				monto = (data["amount"] as? Number)?.toDouble() ?: 0.0,
				montoDolar = (data["amountDollar"] as? Number)?.toDouble() ?: 0.0,
				montoBolivares = (data["amountBs"] as? Number)?.toDouble() ?: 0.0,
				fecha = fecha ?: 0L,
				referencia = data["reference"] as? String,
				promocionNombre = data["promocionNombre"] as? String
			)
		}
		
		return@withContext pagos.sortedByDescending { it.fecha }
	}
	
	
	
	
	@RequiresApi(Build.VERSION_CODES.O)
	override suspend fun getClientesReporte(
		gymCode: String,
		desde: LocalDate?,
		hasta: LocalDate?,
	): List<ReporteCliente> = withContext(Dispatchers.IO) {
		
		val collectionRef = firestore.collection("gimnasios")
			.document(gymCode)
			.collection("usuarios")
			.whereEqualTo("rol", "cliente")
		
		// Timestamps en milisegundos (si los necesitas como timestamps reales, cambia a Timestamp)
		val desdeMillis = desde?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
		val hastaMillis = hasta?.atTime(23, 59, 59)?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
		
		var query = collectionRef as Query
		if (desdeMillis != null) query = query.whereGreaterThanOrEqualTo("date", desdeMillis)
		if (hastaMillis != null) query = query.whereLessThanOrEqualTo("date", hastaMillis)
		
		val snapshot = query.get().await()
		
		val clientes = snapshot.documents.mapNotNull { doc ->
			val data = doc.data ?: return@mapNotNull null
			val date = (data["date"] as? Number)?.toLong() ?: 0L
			
			ReporteCliente(
				nombre = data["name"] as? String ?: "",
				apellido = data["lastname"] as? String ?: "",
				cedula = data["idcard"] as? String ?: "",
				correo = data["email"] as? String ?: "",
				telefono = data["phone"] as? String ?: "",
				activo = data["state"] as? String ?: "",
				date = date
			)
		}
		
		return@withContext clientes.sortedByDescending { it.date }
	}
	
	
	override suspend fun getMembresiasReporte(
		gymCode: String,
		desde: Long?,
		hasta: Long?
	): List<ReporteMembresia> =
		withContext(Dispatchers.IO) {
			val snapshot = firestore.collection("gimnasios")
				.document(gymCode)
				.collection("membresias")
				.get()
				.await()
			
			return@withContext snapshot.documents.mapNotNull { doc ->
				val data = doc.data ?: return@mapNotNull null
				
				val name = data["nombre"] as? String ?: ""
				val price = (data["precio"] as? Number)?.toDouble() ?: 0.0
				val duracion = (data["duracionDias"] as? Number)?.toDouble() ?: 0.0
				
				// Obtener usuarios de esta membresía y contarlos según fecha
				val usuariosSnapshot = firestore.collection("gimnasios")
					.document(gymCode)
					.collection("membresias")
					.document(doc.id)
					.collection("usuarios")
					.get()
					.await()
				
				val userCount = usuariosSnapshot.documents.count { usuarioDoc ->
					val paymentDate = (usuarioDoc.get("paymentdate") as? Number)?.toLong() ?: return@count false
					val enRango = (desde == null || paymentDate >= desde) &&
							(hasta == null || paymentDate <= hasta)
					enRango
				}
				
				ReporteMembresia(
					name = name,
					price = price,
					duracionDias = duracion,
					userCount = userCount
				)
			}
		}
	
	override suspend fun getPromocionesReporte(
		gymCode: String,
		desde: Long?,
		hasta: Long?,
		filtroActivo: Boolean?
	): List<ReportePromocion> =
		withContext(Dispatchers.IO) {
			var promocionesQuery = firestore.collection("gimnasios")
				.document(gymCode)
				.collection("promociones") as com.google.firebase.firestore.Query
			
			// Aplica filtro por activo si corresponde
			if (filtroActivo != null) {
				promocionesQuery = promocionesQuery.whereEqualTo("activo", filtroActivo)
			}
			
			val promocionesSnapshot = promocionesQuery.get().await()
			
			val result = promocionesSnapshot.documents.mapNotNull { promoDoc ->
				val data = promoDoc.data ?: return@mapNotNull null
				val promoId = promoDoc.id
				
				// 🧮 Contar usuarios según filtros
				val cantidadUsuarios: Int = if (desde == null && hasta == null) {
					// Sin filtro de fechas: usa el campo ya guardado
					(data["cantidadUsuarios"] as? Number)?.toInt() ?: 0
				} else {
					// Con filtro de fechas: cuenta los documentos en la subcolección
					val usuariosSnapshot = try {
						var query = firestore.collection("gimnasios")
							.document(gymCode)
							.collection("promociones")
							.document(promoId)
							.collection("usuarios") as com.google.firebase.firestore.Query
						
						if (desde != null) {
							query = query.whereGreaterThanOrEqualTo("paymentdate", desde)
						}
						if (hasta != null) {
							query = query.whereLessThanOrEqualTo("paymentdate", hasta)
						}
						
						query.get().await()
					} catch (e: Exception) {
						null
					}
					
					usuariosSnapshot?.size() ?: 0
				}
				
				ReportePromocion(
					nombre = data["nombre"] as? String ?: "",
					descripcion = data["descripcion"] as? String ?: "",
					porcentaje = (data["porcentajeDescuento"] as? Number)?.toDouble() ?: 0.0,
					duracion = (data["duracionDias"] as? Number)?.toInt() ?: 0,
					activa = data["activo"] as? Boolean ?: false,
					cantidadUsuarios = cantidadUsuarios,
					fechaCreacion = (data["fechaCreacion"] as? Number)?.toLong() ?: 0L,
					fechaVencimiento = (data["fechaVencimiento"] as? Number)?.toLong() ?: 0L,
				)
			}
			
			return@withContext result
		}
	
	
	
}

