package com.jesus.gymcontrol.domain.usecase.usuario.report

import com.jesus.gymcontrol.domain.model.reportModel.ReportePago
import com.jesus.gymcontrol.domain.repository.report.ReportesRepository
import javax.inject.Inject

class GeneratePaymentsReportUseCase @Inject constructor(
	private val reportesRepository: ReportesRepository
) {
	suspend operator fun invoke(gymCode: String): List<ReportePago> {
		return reportesRepository.getPagosReporte(gymCode)
	}
}