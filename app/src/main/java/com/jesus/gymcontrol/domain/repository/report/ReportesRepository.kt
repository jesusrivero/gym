package com.jesus.gymcontrol.domain.repository.report

import com.jesus.gymcontrol.domain.model.reportModel.ReporteCliente
import com.jesus.gymcontrol.domain.model.reportModel.ReporteMembresia
import com.jesus.gymcontrol.domain.model.reportModel.ReportePago
import com.jesus.gymcontrol.domain.model.reportModel.ReportePromocion
import java.time.LocalDate

interface ReportesRepository {
	
	suspend fun getPagosReporte(
		gymCode: String,
		desde: LocalDate? = null,
		hasta: LocalDate? = null
	): List<ReportePago>

	
	
	suspend fun getClientesReporte(
		gymCode: String,
		desde: LocalDate? = null,
		hasta: LocalDate? = null
	): List<ReporteCliente>
	
	
	
	suspend fun getMembresiasReporte(
		gymCode: String,
		desde: Long? = null,
		hasta: Long? = null
	): List<ReporteMembresia>

	
	suspend fun getPromocionesReporte(
		gymCode: String,
		desde: Long? = null,
		hasta: Long? = null
	): List<ReportePromocion>

	
}
