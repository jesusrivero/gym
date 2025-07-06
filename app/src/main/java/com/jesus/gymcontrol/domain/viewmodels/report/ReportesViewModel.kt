package com.jesus.gymcontrol.domain.viewmodels.report

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.model.reportModel.ReporteCliente
import com.jesus.gymcontrol.domain.model.reportModel.ReporteMembresia
import com.jesus.gymcontrol.domain.model.reportModel.ReportePago
import com.jesus.gymcontrol.domain.model.reportModel.ReportePromocion
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
	
	var clientesReport by mutableStateOf<List<ReporteCliente>>(emptyList())
		private set
	
	var membresiasReport by mutableStateOf<List<ReporteMembresia>>(emptyList())
		private set
	
	var promocionesReport by mutableStateOf<List<ReportePromocion>>(emptyList())
		private set
	
	
	var isLoading by mutableStateOf(false)
		private set
	
	var errorMessage by mutableStateOf<String?>(null)
		private set
	
	var totalDolares by mutableStateOf(0.0)
		private set
	
	var totalBolivares by mutableStateOf(0.0)
		private set
	
	
	fun cargarReportePagos(filtro: String) {
		val gymCode = sessionManager.getGymCode() ?: return
		
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			try {
				val todosPagos = generatePaymentsReportUseCase(gymCode)
				
				val filtrados = when (filtro.lowercase()) {
					"dólares", "dolares" -> todosPagos.filter { it.tipoPago.equals("dólares", ignoreCase = true) }
					"bolívares", "bolivares" -> todosPagos.filter { it.tipoPago.equals("bolívares", ignoreCase = true) }
					"mixtos" -> todosPagos.filter { it.tipoPago.equals("mixto", ignoreCase = true) }
					"con promociones" -> todosPagos.filter { !it.promocionNombre.isNullOrBlank() }
					"todos" -> todosPagos
					else -> todosPagos
				}
				
				pagosReport = filtrados
				
				// Calcula totales aquí
				totalDolares = filtrados.sumOf {
					when (it.tipoPago.lowercase()) {
						"dólares" -> it.montoDolar ?: 0.0
						"mixto" -> it.montoDolar ?: 0.0
						else -> 0.0
					}
				}
				
				totalBolivares = filtrados.sumOf {
					when (it.tipoPago.lowercase()) {
						"bolívares" -> it.montoBolivares ?: 0.0
						"mixto" -> it.montoBolivares ?: 0.0
						else -> 0.0
					}
				}
				
			} catch (e: Exception) {
				errorMessage = e.localizedMessage ?: "Error desconocido"
			} finally {
				isLoading = false
				}
		}
	}
	
	
	fun cargarReporteClientes(filtro: String) {
		val gymCode = sessionManager.getGymCode() ?: return
		
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			try {
				val todosClientes = generateClientsReportUseCase(gymCode)
				clientesReport = when (filtro.lowercase()) {
					"activos" -> todosClientes.filter { it.activo.equals("activo", ignoreCase = true) }
					"inactivos" -> todosClientes.filter { it.activo.equals("inactivo", ignoreCase = true) }
					"pendientes" -> todosClientes.filter { it.activo.equals("pendiente", ignoreCase = true) }
					"todos" -> todosClientes
					else -> todosClientes
				}
			} catch (e: Exception) {
				errorMessage = e.localizedMessage ?: "Error desconocido"
			} finally {
				isLoading = false
			}
		}
	}
	
	
	fun cargarReporteMembresias() {
		val gymCode = sessionManager.getGymCode() ?: return
		
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			try {
				membresiasReport = generateMembershipsReportUseCase(gymCode)
			} catch (e: Exception) {
				errorMessage = e.localizedMessage ?: "Error desconocido"
			} finally {
				isLoading = false
			}
		}
	}
	
	
	fun cargarReportePromociones() {
		val gymCode = sessionManager.getGymCode() ?: return
		
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			try {
				promocionesReport = generatePromotionsReportUseCase(gymCode)
			} catch (e: Exception) {
				errorMessage = e.localizedMessage ?: "Error desconocido"
			} finally {
				isLoading = false
			}
		}
	}
	
	fun clearPagos() {
		pagosReport = emptyList()
		errorMessage = null
		isLoading = false
	}
}