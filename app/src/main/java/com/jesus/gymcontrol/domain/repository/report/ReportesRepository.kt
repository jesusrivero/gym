package com.jesus.gymcontrol.domain.repository.report

import com.jesus.gymcontrol.domain.model.reportModel.ReporteCliente
import com.jesus.gymcontrol.domain.model.reportModel.ReporteMembresia
import com.jesus.gymcontrol.domain.model.reportModel.ReportePago
import com.jesus.gymcontrol.domain.model.reportModel.ReportePromocion

interface ReportesRepository {
	
	suspend fun getPagosReporte(gymCode: String): List<ReportePago>
	
	
	suspend fun getClientesReporte(gymCode: String): List<ReporteCliente>
	
	
	suspend fun getMembresiasReporte(gymCode: String): List<ReporteMembresia>
	
	suspend fun getPromocionesReporte(gymCode: String): List<ReportePromocion>
	
}
