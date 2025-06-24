package com.jesus.gymcontrol.domain.usecase.usuario.report

import com.jesus.gymcontrol.domain.model.reportModel.ReporteMembresia
import com.jesus.gymcontrol.domain.repository.report.ReportesRepository
import javax.inject.Inject

class GenerateMembershipsReportUseCase @Inject constructor(
	private val reportesRepository: ReportesRepository
) {
	suspend operator fun invoke(gymCode: String): List<ReporteMembresia> {
		return reportesRepository.getMembresiasReporte(gymCode)
	}
}