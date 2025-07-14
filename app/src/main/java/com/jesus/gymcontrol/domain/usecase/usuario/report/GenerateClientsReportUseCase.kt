package com.jesus.gymcontrol.domain.usecase.usuario.report

import com.jesus.gymcontrol.domain.model.reportModel.ReporteCliente
import com.jesus.gymcontrol.domain.repository.report.ReportesRepository
import java.time.LocalDate
import javax.inject.Inject

class GenerateClientsReportUseCase @Inject constructor(
	private val reportesRepository: ReportesRepository
) {
	suspend operator fun invoke(
		gymCode: String,
		desde: LocalDate? = null,
		hasta: LocalDate? = null
	): List<ReporteCliente> {
		return reportesRepository.getClientesReporte(
			gymCode = gymCode,
			desde = desde,
			hasta = hasta
		)
	}
}
