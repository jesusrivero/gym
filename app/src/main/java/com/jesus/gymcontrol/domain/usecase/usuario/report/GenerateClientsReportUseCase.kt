package com.jesus.gymcontrol.domain.usecase.usuario.report

import com.jesus.gymcontrol.domain.model.report.ReporteCliente
import com.jesus.gymcontrol.domain.repository.report.ReportesRepository
import javax.inject.Inject

class GenerateClientsReportUseCase @Inject constructor(
	private val reportesRepository: ReportesRepository
) {
	suspend operator fun invoke(gymCode: String): List<ReporteCliente> {
		return reportesRepository.getClientesReporte(gymCode)
	}
}