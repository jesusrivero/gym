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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.helpers.PdfReportGenerator
import com.jesus.gymcontrol.domain.model.reportModel.ReporteCliente
import com.jesus.gymcontrol.domain.model.reportModel.ReportePago
import com.jesus.gymcontrol.domain.viewmodels.report.ReportesViewModel
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


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
								listOf("Cliente", "Membresía", "Tipo", "Ref.", "Monto"),
								pagos.map {
									listOf(
										it.nombreCliente,
										it.membresia,
										it.tipoPago,
										it.referencia ?: "-",
										when (it.tipoPago.lowercase()) {
											"dólares" -> formatDollars(it.montoDolar)
											"bolívares" -> formatBolivares(it.montoBolivares)
											"mixto" -> "${formatDollars(it.montoDolar)} - ${formatBolivares(it.montoBolivares)}"
											else -> formatDollars(it.monto)
										},
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
										it.activo?.uppercase() ?: "Desconocido",
										it.telefono,
										it.correo,
									)
								}
							)
							
							"Membresías" -> Triple(
								"Reporte de Membresías",
								listOf("Nombre", "Precio", "Duración", "Cantidad de Clientes"),
								membresias.map {
									listOf(
										it.name,
										"%.2f".format(it.price),
										"${it.duracionDias} días",
										it.userCount.toString()
									)
								}
							)
							
							"Promociones" -> Triple(
								"Reporte de Promociones",
								listOf("Nombre", "Porcentaje", "Duración", "Activa", "Cantidad de Usuarios"),
								promociones.map {
									listOf(
										it.nombre,
										"${it.porcentaje}%",
										"${it.duracion} días",
										if (it.activa) "Sí" else "No",
										"${it.cantidadUsuarios} usuarios"
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
					SimpleReportList(
						items = pagos,
						field1 = { it.nombreCliente },
						field2 = { it.membresia },
						extraField = {
							when (it.tipoPago.lowercase()) {
								"dólares" -> formatDollars(it.montoDolar)
								"bolívares" -> formatBolivares(it.montoBolivares)
								"mixto" -> "${formatDollars(it.montoDolar)} - ${formatBolivares(it.montoBolivares)}"
								else -> formatDollars(it.monto)
							}
						},
						extraFieldColor = { if (it.monto > 0) Color(0xFF2E7D32) else Color.Red },
						emptyMessage = "No hay pagos para mostrar"
					)
				}
				
				selectedReportType == "Clientes" -> {
					SimpleReportList(
						items = clientes,
						field1 = { it.nombre },
						field2 = { "C.I: ${it.cedula}" },
						extraField = { (it.activo ?: "Desconocido").uppercase() },
						extraFieldColor = {
							when ((it.activo ?: "").lowercase()) {
								"activo" -> Color(0xFF2E7D32)
								"inactivo" -> Color.Red
								"pendiente" -> Color(0xFFF9A825)
								else -> MaterialTheme.colorScheme.onSurface
							}
						},
						emptyMessage = "No hay clientes para mostrar"
					)
				}
				
				selectedReportType == "Membresías" -> {
					SimpleReportList(
						items = membresias,
						field1 = { it.name },
						field2 = { "${"%.2f".format(it.price)} $" },
						extraField = { "${it.userCount} clientes" },
						emptyMessage = "No hay membresías para mostrar"
					)
				}
				
				selectedReportType == "Promociones" -> {
					SimpleReportList(
						items = promociones,
						field1 = { it.nombre },
						field2 = { "${it.porcentaje}%" },
						extraField = { if (it.activa) "Activa" else "Inactiva" },
						extraFieldColor = { if (it.activa) Color(0xFF2E7D32) else Color.Red },
						emptyMessage = "No hay promociones para mostrar",
					)
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

// Componente reutilizable para mostrar listas de reportes
@Composable
fun <T> SimpleReportList(
	items: List<T>,
	modifier: Modifier = Modifier,
	field1: (T) -> String,
	field2: (T) -> String,
	extraField: ((T) -> String)? = null,
	extraFieldColor: @Composable ((T) -> Color)? = null,
	emptyMessage: String = "No hay datos para mostrar"
) {
	if (items.isEmpty()) {
		Text(
			emptyMessage,
			modifier = Modifier.fillMaxWidth(),
			textAlign = TextAlign.Center,
			style = MaterialTheme.typography.bodyMedium
		)
		return
	}
	
	LazyColumn(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		items(items) { item ->
			Card(
				modifier = Modifier
					.fillMaxWidth()
					.shadow(2.dp, RoundedCornerShape(8.dp)),
				shape = RoundedCornerShape(8.dp),
				colors = CardDefaults.cardColors(
					containerColor = MaterialTheme.colorScheme.surface
				)
			) {
				Row(
					modifier = Modifier
						.padding(16.dp),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Column(modifier = Modifier.weight(1f)) {
						Text(
							text = field1(item),
							style = MaterialTheme.typography.titleMedium,
							maxLines = 1,
							overflow = TextOverflow.Ellipsis
						)
						Text(
							text = field2(item),
							style = MaterialTheme.typography.bodyMedium,
							maxLines = 1,
							overflow = TextOverflow.Ellipsis,
							color = MaterialTheme.colorScheme.onSurfaceVariant
						)
					}
					
					extraField?.let { ef ->
						val value = ef(item)
						
						// Aplicar lógica de símbolo si es monto de pago
						val formatted = when {
							value.contains("Bs") || value.contains("$") -> value // Ya formateado
							value.matches(Regex("\\d+(\\.\\d+)?")) -> { // Solo número
								// Intentar inferir tipo de pago desde el item si es un Pago
								val tipoPago = try {
									val prop = item!!::class.members.firstOrNull { it.name == "tipoPago" }
									val raw = prop?.call(item) as? String
									raw?.lowercase() ?: ""
								} catch (_: Exception) {
									""
								}
								
								val simbolo = when (tipoPago) {
									"Bolívares" -> "Bs"
									"Dólares" -> "$"
									"Mixto" -> "Bs - $"
									else -> ""
								}
								"$simbolo $value"
							}
							else -> value
						}
						
						val color = extraFieldColor?.invoke(item) ?: MaterialTheme.colorScheme.onSurface
						Text(
							text = formatted,
							color = color,
							fontWeight = FontWeight.Bold,
							style = MaterialTheme.typography.bodyMedium,
							modifier = Modifier.padding(start = 8.dp)
						)
					}
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


fun formatDollars(amount: Double?): String {
	if (amount == null) return "$0"
	val format = NumberFormat.getCurrencyInstance(Locale.US)
	return format.format(amount)
}

fun formatBolivares(amount: Double?): String {
	if (amount == null) return "Bs 0,00"
	val format = NumberFormat.getCurrencyInstance(Locale("es", "VE"))
	format.maximumFractionDigits = 2
	format.minimumFractionDigits = 2
	val result = format.format(amount).replace("Bs.", "Bs ")
	return result
}

