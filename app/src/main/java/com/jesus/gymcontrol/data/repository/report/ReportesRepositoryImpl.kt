package com.jesus.gymcontrol.data.repository.report

import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.domain.model.reportModel.ReporteCliente
import com.jesus.gymcontrol.domain.model.reportModel.ReporteMembresia
import com.jesus.gymcontrol.domain.model.reportModel.ReportePago
import com.jesus.gymcontrol.domain.model.reportModel.ReportePromocion
import com.jesus.gymcontrol.domain.repository.report.ReportesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReportesRepositoryImpl @Inject constructor(
	private val firestore: FirebaseFirestore,
) : ReportesRepository {
	
	override suspend fun getPagosReporte(gymCode: String): List<ReportePago> =
		withContext(Dispatchers.IO) {
			val snapshot = firestore.collection("gimnasios")
				.document(gymCode)
				.collection("pagos")
				.get()
				.await()
			
			val pagos = snapshot.documents.mapNotNull { doc ->
				val data = doc.data ?: return@mapNotNull null
				
				val fecha = (data["date"] as? Number)?.toLong()
				
				ReportePago(
					nombreCliente = data["name"] as? String ?: "",
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
	
	override suspend fun getClientesReporte(gymCode: String): List<ReporteCliente> =
		withContext(Dispatchers.IO) {
			val snapshot = firestore.collection("gimnasios")
				.document(gymCode)
				.collection("usuarios")
				.get()
				.await()
			
			val clientes = snapshot.documents.mapNotNull { doc ->
				val data = doc.data ?: return@mapNotNull null
				if ((data["rol"] as? String)?.lowercase() == "cliente") {
					val date = (data["date"] as? Number)?.toLong() ?: 0L
					ReporteCliente(
						nombre = data["name"] as? String ?: "",
						cedula = data["idcard"] as? String ?: "",
						correo = data["email"] as? String ?: "",
						telefono = data["phone"] as? String ?: "",
						activo = data["state"] as? String ?: "",
						date = date
					)
				} else null
			}
			
			return@withContext clientes.sortedByDescending { it.date }
		}
	
	
	override suspend fun getMembresiasReporte(gymCode: String): List<ReporteMembresia> =
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
				
				// Obtener cantidad de usuarios inscritos a esta membresía
				val userCountSnapshot = firestore.collection("gimnasios")
					.document(gymCode)
					.collection("membresias")
					.document(doc.id)
					.collection("usuarios")
					.get()
					.await()
				
				val userCount = userCountSnapshot.size()
				
				ReporteMembresia(
					name = name,
					price = price,
					duracionDias = duracion,
					userCount = userCount
				)
			}
		}
	
	override suspend fun getPromocionesReporte(gymCode: String): List<ReportePromocion> =
		withContext(Dispatchers.IO) {
			val promocionesRef = firestore.collection("gimnasios")
				.document(gymCode)
				.collection("promociones")
			
			val promocionesSnapshot = promocionesRef.get().await()
			
			val result = promocionesSnapshot.documents.mapNotNull { promoDoc ->
				val data = promoDoc.data ?: return@mapNotNull null
				val promoId = promoDoc.id
				
				// Obtener usuarios de esta promoción
				val usuariosSnapshot = try {
					promocionesRef
						.document(promoId)
						.collection("usuarios")
						.get()
						.await()
				} catch (e: Exception) {
					null
				}
				val cantidadUsuarios = usuariosSnapshot?.size() ?: 0
				
				ReportePromocion(
					nombre = data["nombre"] as? String ?: "",
					descripcion = data["descripcion"] as? String ?: "",
					porcentaje = (data["porcentajeDescuento"] as? Number)?.toDouble() ?: 0.0,
					duracion = (data["duracionDias"] as? Number)?.toInt() ?: 0,
					activa = data["activo"] as? Boolean ?: false,
					cantidadUsuarios = cantidadUsuarios,
					fechaCreacion = (data["fechaCreacion"] as? Number)?.toLong() ?: 0L
				)
			}
			
			return@withContext result
		}
	
	
	
}

