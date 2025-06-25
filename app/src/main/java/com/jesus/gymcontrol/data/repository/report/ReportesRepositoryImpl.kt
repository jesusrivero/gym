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
			
			return@withContext snapshot.documents.mapNotNull { doc ->
				val data = doc.data ?: return@mapNotNull null
				
				ReportePago(
					nombreCliente = data["name"] as? String ?: "",
					cedula = data["idcard"] as? String ?: "",
					membresia = data["membershipName"] as? String ?: "",
					tipoPago = data["typepayment"] as? String ?: "",
					monto = (data["amount"] as? Number)?.toDouble() ?: 0.0,
					fecha = (data["date"] as? Number)?.toLong() ?: 0L,
					referencia = data["reference"] as? String,
					promocionNombre = data["promocionNombre"] as? String,
			
				)
			}
		}
	
	override suspend fun getClientesReporte(gymCode: String): List<ReporteCliente> =
		withContext(Dispatchers.IO) {
			val snapshot = firestore.collection("gimnasios")
				.document(gymCode)
				.collection("usuarios")
				.get()
				.await()
			
			return@withContext snapshot.documents.mapNotNull { doc ->
				val data = doc.data ?: return@mapNotNull null
				if ((data["rol"] as? String)?.lowercase() == "cliente") {
					ReporteCliente(
						nombre = data["name"] as? String ?: "",
						cedula = data["idcard"] as? String ?: "",
						correo = data["email"] as? String ?: "",
						telefono = data["phone"] as? String ?: "",
						activo = data["state"] as? String ?: "",
					)
				} else null
			}
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
				
				ReporteMembresia(
					name = data["nombre"] as? String ?: "",
					price = (data["precio"] as? Number)?.toDouble() ?: 0.0,
					duracionDias =  (data["duracionDias"] as? Number)?.toDouble() ?: 0.0
				)
			}
		}
	
	override suspend fun getPromocionesReporte(gymCode: String): List<ReportePromocion> =
		withContext(Dispatchers.IO) {
			val snapshot = firestore.collection("gimnasios")
				.document(gymCode)
				.collection("promociones")
				.get()
				.await()
			
			return@withContext snapshot.documents.mapNotNull { doc ->
				val data = doc.data ?: return@mapNotNull null
				ReportePromocion(
					nombre = data["nombre"] as? String ?: "",
					descripcion = data["descripcion"] as? String ?: "",
					porcentaje = (data["porcentajeDescuento"] as? Number)?.toDouble() ?: 0.0,
					duracion = (data["duracionDias"] as? Number)?.toInt() ?: 0,
					activa = data["activo"] as? Boolean ?: false
				)
			}
		}
	
	
	
}

