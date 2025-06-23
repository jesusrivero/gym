import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.viewmodels.report.ReportesViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
	navController: NavController,
	viewModel: ReportesViewModel = hiltViewModel(),
) {
	val snackbarHostState = remember { SnackbarHostState() }
	var selectedReportType by remember { mutableStateOf("Todos") }
	
	val reportTypes = listOf("Todos", "Clientes", "Pagos", "Membresías", "Próximos Pagos", "Promociones", "Vencimientos")
	
	val pagos by remember { derivedStateOf { viewModel.pagosReport } }
	val clientes by remember { derivedStateOf { viewModel.clientesReport } }
	val membresias by remember { derivedStateOf { viewModel.membresiasReport } }
	val promociones by remember { derivedStateOf { viewModel.promocionesReport } }
	
	
	val isLoading by remember { derivedStateOf { viewModel.isLoading } }
	val errorMessage by remember { derivedStateOf { viewModel.errorMessage } }
	
	LaunchedEffect(selectedReportType) {
		when (selectedReportType) {
			"Pagos" -> viewModel.cargarReportePagos()
			"Clientes" -> viewModel.cargarReporteClientes()
			"Membresías" -> viewModel.cargarReporteMembresias()
			"Promociones" -> viewModel.cargarReportePromociones()
		}
	}
	
	Scaffold(
		snackbarHost = { SnackbarHost(snackbarHostState) },
		topBar = {
			Column {
				TopAppBar(
					title = {
						Text("Reportes", color = MaterialTheme.colorScheme.onPrimary)
					},
					navigationIcon = {
						IconButton(onClick = { navController.popBackStack() }) {
							Icon(
								painter = painterResource(id = R.drawable.ic_back),
								contentDescription = "Regresar",
								tint = MaterialTheme.colorScheme.onPrimary
							)
						}
					},
					colors = TopAppBarDefaults.topAppBarColors(
						containerColor = MaterialTheme.colorScheme.primary
					)
				)
				
				ReportFilters(
					selectedReportType = selectedReportType,
					reportTypes = reportTypes,
					onReportTypeSelected = { selectedReportType = it },
					startDate = null,
					endDate = null,
					onStartDateClick = {},
					onEndDateClick = {},
					onClearFilters = {
						selectedReportType = "Todos"
					},
					dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
				)
			}
		}
	) { innerPadding ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.padding(16.dp),
			contentAlignment = Alignment.TopCenter
		) {
			when {
				selectedReportType == "Todos" -> {
					Text(
						"Seleccione un tipo de reporte para mostrar datos",
						textAlign = TextAlign.Center,
						style = MaterialTheme.typography.bodyMedium
					)
				}
				
				isLoading -> {
					CircularProgressIndicator()
				}
				
				errorMessage != null -> {
					Text(
						"Error: $errorMessage",
						color = MaterialTheme.colorScheme.error,
						textAlign = TextAlign.Center
					)
				}
				
				selectedReportType == "Pagos" -> {
					if (pagos.isEmpty()) {
						Text("No hay pagos para mostrar", textAlign = TextAlign.Center)
					} else {
						LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
							items(pagos) { pago ->
								Column(modifier = Modifier.fillMaxWidth()) {
									Text("${pago.nombreCliente} - ${pago.membresia}")
									Text("Tipo: ${pago.tipoPago} - Monto: ${pago.monto}")
									pago.referencia?.let { Text("Ref: $it") }
									Divider()
								}
							}
						}
					}
				}
				
				selectedReportType == "Clientes" -> {
					if (clientes.isEmpty()) {
						Text("No hay clientes para mostrar", textAlign = TextAlign.Center)
					} else {
						LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
							items(clientes) { cliente ->
								Column(modifier = Modifier.fillMaxWidth()) {
									Text("${cliente.nombre} - C.I: ${cliente.cedula}")
									Text("Correo: ${cliente.correo}")
									Text("Estado: ${(cliente.activo ?: "Desconocido").uppercase()}")
								}
							}
						}
					}
				}
				
				selectedReportType == "Membresías" -> {
					if (membresias.isEmpty()) {
						Text("No hay membresías para mostrar", textAlign = TextAlign.Center)
					} else {
						LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
							items(membresias) { item ->
								Column(modifier = Modifier.fillMaxWidth()) {
									Text("Nombre: ${item.name}")
									Text("Precio: $${item.price}")
									Text("Duracion: ${item.duracionDias} días")
									Divider()
								}
							}
						}
					}
				}
				
				selectedReportType == "Promociones" -> {
					if (promociones.isEmpty()) {
						Text("No hay promociones para mostrar", textAlign = TextAlign.Center)
					} else {
						LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
							items(promociones) { promo ->
								Column(modifier = Modifier.fillMaxWidth()) {
									Text("Nombre: ${promo.nombre}")
									Text("Porcentaje: ${promo.porcentaje}%")
									Text("Activa: ${if (promo.activa) "Sí" else "No"}")
									Text("Duración: ${promo.duracion} días")
									Divider()
								}
							}
						}
					}
				}
				
				else -> {
					Text(
						"Reporte para '$selectedReportType' aún no implementado.",
						textAlign = TextAlign.Center,
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
		}
	}
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportFilters(
	selectedReportType: String,
	reportTypes: List<String>,
	onReportTypeSelected: (String) -> Unit,
	startDate: LocalDate?,
	endDate: LocalDate?,
	onStartDateClick: () -> Unit,
	onEndDateClick: () -> Unit,
	onClearFilters: () -> Unit,
	dateFormatter: DateTimeFormatter,
) {
	var expandedFilter by remember { mutableStateOf(false) }
	
	Surface(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp, vertical = 8.dp),
		shape = RoundedCornerShape(16.dp),
		color = MaterialTheme.colorScheme.surface,
		shadowElevation = 4.dp
	) {
		Column(modifier = Modifier.padding(16.dp)) {
			Text(
				text = "Tipo de Reporte",
				style = MaterialTheme.typography.labelMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(bottom = 4.dp)
			)
			
			ExposedDropdownMenuBox(
				expanded = expandedFilter,
				onExpandedChange = { expandedFilter = it }
			) {
				OutlinedTextField(
					value = selectedReportType,
					onValueChange = {},
					readOnly = true,
					trailingIcon = {
						ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFilter)
					},
					modifier = Modifier
						.fillMaxWidth()
						.menuAnchor(),
					shape = RoundedCornerShape(12.dp),
				)
				
				ExposedDropdownMenu(
					expanded = expandedFilter,
					onDismissRequest = { expandedFilter = false },
					modifier = Modifier.background(Color.White)
				) {
					reportTypes.forEach { type ->
						DropdownMenuItem(
							text = { Text(type) },
							onClick = {
								onReportTypeSelected(type)
								expandedFilter = false
							}
						)
					}
				}
			}
			
			Spacer(modifier = Modifier.height(16.dp))
			
			Text(
				text = "Rango de Fechas",
				style = MaterialTheme.typography.labelMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(bottom = 4.dp)
			)
			
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.spacedBy(8.dp)
			) {
				DateSelectorButton("Desde", startDate, dateFormatter, onStartDateClick, Modifier.weight(1f))
				DateSelectorButton("Hasta", endDate, dateFormatter, onEndDateClick, Modifier.weight(1f))
			}
			
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 16.dp),
				horizontalArrangement = Arrangement.spacedBy(8.dp)
			) {
				Button(
					onClick = { /* Lógica para aplicar filtros */ },
					enabled = selectedReportType != "Todos" || startDate != null || endDate != null,
					modifier = Modifier.weight(1f)
				) {
					Text("Generar Reporte")
				}
				
				OutlinedButton(
					onClick = onClearFilters,
					modifier = Modifier.weight(1f),
					border = BorderStroke(
						1.dp,
						MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
					)
				) {
					Row(verticalAlignment = Alignment.CenterVertically) {
						Icon(
							Icons.Default.Close,
							contentDescription = "Limpiar",
							modifier = Modifier.size(18.dp)
						)
						Spacer(modifier = Modifier.width(8.dp))
						Text("Limpiar")
					}
				}
			}
		}
	}
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DateSelectorButton(
	label: String,
	date: LocalDate?,
	formatter: DateTimeFormatter,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	OutlinedButton(
		onClick = onClick,
		modifier = modifier,
		shape = RoundedCornerShape(12.dp),
		colors = ButtonDefaults.outlinedButtonColors()
	) {
		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			Text(
				text = label,
				style = MaterialTheme.typography.labelSmall,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
			Text(
				text = date?.format(formatter) ?: "Seleccionar",
				style = MaterialTheme.typography.bodyMedium,
				color = if (date != null) MaterialTheme.colorScheme.onSurface
				else MaterialTheme.colorScheme.onSurfaceVariant
			)
		}
	}
}
