package com.jesus.gymcontrol.domain.usecase.usuario.report

import com.jesus.gymcontrol.domain.model.reportModel.ReportePromocion
import com.jesus.gymcontrol.domain.repository.report.ReportesRepository
import javax.inject.Inject

class GeneratePromotionsReportUseCase @Inject constructor(
	private val reportesRepository: ReportesRepository
) {
	suspend operator fun invoke(gymCode: String): List<ReportePromocion> {
		return reportesRepository.getPromocionesReporte(gymCode)
	}
}