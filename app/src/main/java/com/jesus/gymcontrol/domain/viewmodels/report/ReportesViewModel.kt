package com.jesus.gymcontrol.domain.viewmodels.report

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.model.report.ReporteCliente
import com.jesus.gymcontrol.domain.model.report.ReporteMembresia
import com.jesus.gymcontrol.domain.model.report.ReportePago
import com.jesus.gymcontrol.domain.model.report.ReportePromocion
import com.jesus.gymcontrol.domain.usecase.usuario.report.GenerateClientsReportUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.report.GenerateMembershipsReportUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.report.GeneratePaymentsReportUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.report.GeneratePromotionsReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportesViewModel @Inject constructor(
	private val generatePaymentsReportUseCase: GeneratePaymentsReportUseCase,
	private val generateClientsReportUseCase: GenerateClientsReportUseCase,
	private val generateMembershipsReportUseCase: GenerateMembershipsReportUseCase,
	private val generatePromotionsReportUseCase: GeneratePromotionsReportUseCase,
	
	private val sessionManager: SessionManager
) : ViewModel() {
	
	var pagosReport by mutableStateOf<List<ReportePago>>(emptyList())
		private set
	
	var clientesReport by mutableStateOf<List<ReporteCliente>>(emptyList()) // ✅ nuevo
		private set
	
	var membresiasReport by mutableStateOf<List<ReporteMembresia>>(emptyList())
		private set
	
	var promocionesReport by mutableStateOf<List<ReportePromocion>>(emptyList())
		private set
	
	
	var isLoading by mutableStateOf(false)
		private set
	
	var errorMessage by mutableStateOf<String?>(null)
		private set
	
	fun cargarReportePagos() {
		val gymCode = sessionManager.getGymCode() ?: return
		
		viewModelScope.launch {
			isLoading = true
			try {
				pagosReport = generatePaymentsReportUseCase(gymCode)
			} catch (e: Exception) {
				errorMessage = e.localizedMessage
			} finally {
				isLoading = false
			}
		}
	}
	
	fun cargarReporteClientes() {
		val gymCode = sessionManager.getGymCode() ?: return
		viewModelScope.launch {
			isLoading = true
			try {
				clientesReport = generateClientsReportUseCase(gymCode)
			} catch (e: Exception) {
				errorMessage = e.localizedMessage
			} finally {
				isLoading = false
			}
		}
	}
	
	
	fun cargarReporteMembresias() {
		val gymCode = sessionManager.getGymCode() ?: return
		
		viewModelScope.launch {
			isLoading = true
			try {
				membresiasReport = generateMembershipsReportUseCase(gymCode)
			} catch (e: Exception) {
				errorMessage = e.localizedMessage
			} finally {
				isLoading = false
			}
		}
	}
	
	
	fun cargarReportePromociones() {
		val gymCode = sessionManager.getGymCode() ?: return
		viewModelScope.launch {
			isLoading = true
			try {
				promocionesReport = generatePromotionsReportUseCase(gymCode)
			} catch (e: Exception) {
				errorMessage = e.localizedMessage
			} finally {
				isLoading = false
			}
		}
	}
	
	
	
	// En ViewModel, agrega esta función para limpiar pagos (o estado según necesidad)
	fun ReportesViewModel.clearPagos() {
		pagosReport = emptyList()
		errorMessage = null
		isLoading = false
	}
}
