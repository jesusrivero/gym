import android.content.Context
import android.content.Intent
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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.helpers.PdfReportGenerator
import com.jesus.gymcontrol.domain.model.reportModel.ReporteCliente
import com.jesus.gymcontrol.domain.model.reportModel.ReporteMembresia
import com.jesus.gymcontrol.domain.model.reportModel.ReportePago
import com.jesus.gymcontrol.domain.model.reportModel.ReportePromocion
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
	var selectedSubFilter by remember { mutableStateOf("Todos") }
	
	val reportTypes = listOf("Todos", "Clientes", "Pagos", "Membresías", "Promociones")
	
	val clientesFilters = listOf("Todos", "Activos", "Inactivos", "Pendientes")
	val pagosFilters = listOf("Todos", "Dólares", "Bolívares", "Mixtos", "Con promociones")
	
	val pagos by remember { derivedStateOf { viewModel.pagosReport } }
	val clientes by remember { derivedStateOf { viewModel.clientesReport } }
	val membresias by remember { derivedStateOf { viewModel.membresiasReport } }
	val promociones by remember { derivedStateOf { viewModel.promocionesReport } }
	
	val isLoading by remember { derivedStateOf { viewModel.isLoading } }
	val errorMessage by remember { derivedStateOf { viewModel.errorMessage } }
	
	val context = LocalContext.current
	
	LaunchedEffect(selectedReportType, selectedSubFilter) {
		when (selectedReportType) {
			"Pagos" -> viewModel.cargarReportePagos(filtro = selectedSubFilter)
			"Clientes" -> viewModel.cargarReporteClientes(filtro = selectedSubFilter)
			"Membresías" -> viewModel.cargarReporteMembresias()
			"Promociones" -> viewModel.cargarReportePromociones()
			else -> { /* no hacer nada */ }
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
					onReportTypeSelected = {
						selectedReportType = it
						selectedSubFilter = "Todos" // reset filtro secundario al cambiar tipo
					},
					selectedSubFilter = selectedSubFilter,
					onSubFilterSelected = { selectedSubFilter = it },
					clientesFilters = clientesFilters,
					pagosFilters = pagosFilters,
					startDate = null,
					endDate = null,
					onStartDateClick = {},
					onEndDateClick = {},
					onClearFilters = {
						selectedReportType = "Todos"
						selectedSubFilter = "Todos"
					},
					dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"),
					context = context,
					pagos = pagos,
					clientes = clientes
				)
			}
		},
		floatingActionButton = {
			if (selectedReportType != "Todos") {
				FloatingActionButton(
					onClick = {
						val (reportTitle, headers, rows) = when (selectedReportType) {
							"Pagos" -> Triple(
								"Reporte de Pagos",
								listOf("Cliente", "Membresía", "Monto", "Tipo", "Ref."),
								pagos.map {
									listOf(
										it.nombreCliente,
										it.membresia,
										"%.2f".format(it.monto),
										it.tipoPago,
										it.referencia ?: "-"
									)
								}
							)
							"Clientes" -> Triple(
								"Reporte de Clientes",
								listOf("Nombre", "Cédula", "Correo", "Teléfono", "Estado"),
								clientes.map {
									listOf(
										it.nombre,
										it.cedula,
										it.correo,
										it.telefono,
										it.activo?.uppercase() ?: "Desconocido"
									)
								}
							)
							"Membresías" -> Triple(
								"Reporte de Membresías",
								listOf("Nombre", "Precio", "Duración"),
								membresias.map {
									listOf(
										it.name,
										"%.2f".format(it.price),
										"${it.duracionDias} días"
									)
								}
							)
							"Promociones" -> Triple(
								"Reporte de Promociones",
								listOf("Nombre", "Porcentaje", "Duración", "Activa"),
								promociones.map {
									listOf(
										it.nombre,
										"${it.porcentaje}%",
										"${it.duracion} días",
										if (it.activa) "Sí" else "No"
									)
								}
							)
							else -> Triple("Reporte", emptyList(), emptyList())
						}
						
						if (headers.isEmpty() || rows.isEmpty()) return@FloatingActionButton
						
						val file = PdfReportGenerator.generateReportPdf(
							context = context,
							reportTitle = reportTitle,
							headers = headers,
							rows = rows
						)
						
						file?.let {
							val uri = PdfReportGenerator.getUriFromFile(context, it)
							val intent = Intent(Intent.ACTION_SEND).apply {
								type = "application/pdf"
								putExtra(Intent.EXTRA_STREAM, uri)
								addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
							}
							context.startActivity(Intent.createChooser(intent, "Compartir reporte PDF"))
						}
					},
					containerColor = MaterialTheme.colorScheme.primary,
					contentColor = MaterialTheme.colorScheme.onPrimary,
				) {
					Icon(Icons.Default.Share, contentDescription = "Exportar reporte PDF")
				}
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
	selectedSubFilter: String,
	onSubFilterSelected: (String) -> Unit,
	clientesFilters: List<String>,
	pagosFilters: List<String>,
	startDate: LocalDate?,
	endDate: LocalDate?,
	onStartDateClick: () -> Unit,
	onEndDateClick: () -> Unit,
	onClearFilters: () -> Unit,
	dateFormatter: DateTimeFormatter,
	context: Context,
	pagos: List<ReportePago>,
	clientes: List<ReporteCliente>,
) {
	var expandedFilter by remember { mutableStateOf(false) }
	var expandedSubFilter by remember { mutableStateOf(false) }
	
	Surface(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 8.dp, vertical = 4.dp),
		shape = RoundedCornerShape(12.dp),
		color = MaterialTheme.colorScheme.surface,
		shadowElevation = 2.dp
	) {
		Column(modifier = Modifier.padding(12.dp)) {
			
			// Tipo de reporte
			Text(
				text = "Tipos de reportes",
				style = MaterialTheme.typography.labelSmall,
				color = MaterialTheme.colorScheme.onSurfaceVariant
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
					textStyle = MaterialTheme.typography.bodySmall,
					shape = RoundedCornerShape(10.dp),
					singleLine = true
				)
				
				ExposedDropdownMenu(
					expanded = expandedFilter,
					onDismissRequest = { expandedFilter = false },
					modifier = Modifier.background(MaterialTheme.colorScheme.surface)
				) {
					reportTypes.forEach { type ->
						DropdownMenuItem(
							text = { Text(type, style = MaterialTheme.typography.bodySmall) },
							onClick = {
								onReportTypeSelected(type)
								expandedFilter = false
							}
						)
					}
				}
			}
			
			// Filtro secundario dinámico según tipo de reporte
			if (selectedReportType == "Clientes" || selectedReportType == "Pagos") {
				Spacer(modifier = Modifier.height(8.dp))
				Text(
					text = "Filtros",
					style = MaterialTheme.typography.labelSmall,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
				
				val filters = when (selectedReportType) {
					"Clientes" -> clientesFilters
					"Pagos" -> pagosFilters
					else -> emptyList()
				}
				
				ExposedDropdownMenuBox(
					expanded = expandedSubFilter,
					onExpandedChange = { expandedSubFilter = it }
				) {
					OutlinedTextField(
						value = selectedSubFilter,
						onValueChange = {},
						readOnly = true,
						trailingIcon = {
							ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSubFilter)
						},
						modifier = Modifier
							.fillMaxWidth()
							.menuAnchor(),
						textStyle = MaterialTheme.typography.bodySmall,
						shape = RoundedCornerShape(10.dp),
						singleLine = true
					)
					
					ExposedDropdownMenu(
						expanded = expandedSubFilter,
						onDismissRequest = { expandedSubFilter = false },
						modifier = Modifier.background(MaterialTheme.colorScheme.surface)
					) {
						filters.forEach { filter ->
							DropdownMenuItem(
								text = { Text(filter, style = MaterialTheme.typography.bodySmall) },
								onClick = {
									onSubFilterSelected(filter)
									expandedSubFilter = false
								}
							)
						}
					}
				}
			}
			
			// Rango de fechas (para todos excepto "Todos")
			if (selectedReportType != "Todos") {
				Spacer(modifier = Modifier.height(8.dp))
				
				Text(
					text = "Fechas",
					style = MaterialTheme.typography.labelSmall,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
				
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.spacedBy(8.dp)
				) {
					DateSelectorButton(
						label = "Desde",
						date = startDate,
						formatter = dateFormatter,
						onClick = onStartDateClick,
						modifier = Modifier.weight(1f)
					)
					DateSelectorButton(
						label = "Hasta",
						date = endDate,
						formatter = dateFormatter,
						onClick = onEndDateClick,
						modifier = Modifier.weight(1f)
					)
				}
			}
			
			// Botón limpiar
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 8.dp),
				horizontalArrangement = Arrangement.End
			) {
				OutlinedButton(
					onClick = onClearFilters,
					modifier = Modifier
						.height(36.dp)
						.fillMaxWidth()
						.padding(horizontal = 8.dp),
					border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
				) {
					Icon(Icons.Default.Close, contentDescription = "Limpiar", modifier = Modifier.size(16.dp))
					Spacer(modifier = Modifier.width(4.dp))
					Text("Limpiar", style = MaterialTheme.typography.labelSmall)
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


@Composable
fun ExportReportButton(
	context: Context,
	selectedReportType: String,
	pagos: List<ReportePago> = emptyList(),
	clientes: List<ReporteCliente> = emptyList(),
	membresias: List<ReporteMembresia> = emptyList(),
	promociones: List<ReportePromocion> = emptyList(),
	modifier: Modifier = Modifier,
) {
	Button(
		onClick = {
			val (reportTitle, headers, rows) = when (selectedReportType) {
				"Pagos" -> Triple(
					"Reporte de Pagos",
					listOf("Cliente", "Membresía", "Monto", "Tipo", "Ref."),
					pagos.map {
						listOf(
							it.nombreCliente,
							it.membresia,
							"%.2f".format(it.monto),
							it.tipoPago,
							it.referencia ?: "-"
						)
					}
				)
				
				"Clientes" -> Triple(
					"Reporte de Clientes",
					listOf("Nombre", "Cédula", "Correo", "Teléfono", "Estado"),
					clientes.map {
						listOf(
							it.nombre,
							it.cedula,
							it.correo,
							it.telefono,
							it.activo?.uppercase() ?: "Desconocido"
						)
					}
				)
				
				"Membresías" -> Triple(
					"Reporte de Membresías",
					listOf("Nombre", "Precio", "Duración"),
					membresias.map {
						listOf(
							it.name,
							"%.2f".format(it.price),
							"${it.duracionDias} días"
						)
					}
				)
				
				"Promociones" -> Triple(
					"Reporte de Promociones",
					listOf("Nombre", "Porcentaje", "Duración", "Activa"),
					promociones.map {
						listOf(
							it.nombre,
							"${it.porcentaje}%",
							"${it.duracion} días",
							if (it.activa) "Sí" else "No"
						)
					}
				)
				
				else -> Triple("Reporte", emptyList(), emptyList())
			}
			
			if (headers.isEmpty() || rows.isEmpty()) return@Button
			
			val file = PdfReportGenerator.generateReportPdf(
				context = context,
				reportTitle = reportTitle,
				headers = headers,
				rows = rows
			)
			
			file?.let {
				val uri = PdfReportGenerator.getUriFromFile(context, it)
				val intent = Intent(Intent.ACTION_SEND).apply {
					type = "application/pdf"
					putExtra(Intent.EXTRA_STREAM, uri)
					addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
				}
				context.startActivity(Intent.createChooser(intent, "Compartir reporte PDF"))
			}
		},
		modifier = modifier
	) {
		Icon(Icons.Default.Share, contentDescription = null)
		Spacer(modifier = Modifier.width(8.dp))
		Text("Exportar PDF")
	}
}

