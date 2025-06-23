package com.jesus.gymcontrol.domain.repository.report

import com.jesus.gymcontrol.domain.model.report.ReporteCliente
import com.jesus.gymcontrol.domain.model.report.ReporteMembresia
import com.jesus.gymcontrol.domain.model.report.ReportePago
import com.jesus.gymcontrol.domain.model.report.ReportePromocion

interface ReportesRepository {
	
	suspend fun getPagosReporte(gymCode: String): List<ReportePago>
	
	
	suspend fun getClientesReporte(gymCode: String): List<ReporteCliente>
	
	
	suspend fun getMembresiasReporte(gymCode: String): List<ReporteMembresia>
	
	suspend fun getPromocionesReporte(gymCode: String): List<ReportePromocion>
	
}
