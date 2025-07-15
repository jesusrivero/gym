package com.jesus.gymcontrol.domain.usecase.usuario.report

import com.jesus.gymcontrol.domain.model.reportModel.ReporteMembresia
import com.jesus.gymcontrol.domain.repository.report.ReportesRepository
import javax.inject.Inject

class GenerateMembershipsReportUseCase @Inject constructor(
	private val reportesRepository: ReportesRepository
) {
	suspend operator fun invoke(
		gymCode: String,
		desde: Long?,
		hasta: Long?,
		filtroActivo: Boolean? = null
	): List<ReporteMembresia> {
		val lista = reportesRepository.getMembresiasReporte(gymCode, desde, hasta)
		return when (filtroActivo) {
			true -> lista.filter { it.activo }
			false -> lista.filter { !it.activo }
			null -> lista
		}
	}
	
}
