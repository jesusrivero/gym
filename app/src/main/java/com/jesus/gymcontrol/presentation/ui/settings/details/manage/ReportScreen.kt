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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.google.firebase.auth.FirebaseAuth
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.helpers.PdfReportGenerator
import com.jesus.gymcontrol.domain.model.reportModel.ReporteCliente
import com.jesus.gymcontrol.domain.model.reportModel.ReportePago
import com.jesus.gymcontrol.domain.viewmodels.GymViewModel
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
	gymViewModel: GymViewModel = hiltViewModel(), // <- para acceder al gimnasio
) {
	val snackbarHostState = remember { SnackbarHostState() }
	var selectedReportType by rememberSaveable { mutableStateOf("Todos") }
	var selectedSubFilter by rememberSaveable { mutableStateOf("Todos") }
	val reportTypes = listOf("Todos", "Clientes", "Pagos", "Membresías", "Promociones")
	val clientesFilters = listOf("Todos", "Activos", "Inactivos", "Pendientes")
	val promotionFilter = listOf("Todos", "Activos", "Inactivos")
	val membershipFilter = listOf("Todos", "Activos", "Inactivos")
	val pagosFilters = listOf("Todos", "Dólares", "Bolívares", "Mixtos", "Con promociones")
	val pagos by remember { derivedStateOf { viewModel.pagosReport } }
	val clientes by remember { derivedStateOf { viewModel.clientesReport } }
	val membresias by remember { derivedStateOf { viewModel.membresiasReport } }
	val promociones by remember { derivedStateOf { viewModel.promocionesReport } }
	val isLoading by remember { derivedStateOf { viewModel.isLoading } }
	val errorMessage by remember { derivedStateOf { viewModel.errorMessage } }
	var startDate by rememberSaveable { mutableStateOf<LocalDate?>(null) }
	var endDate by rememberSaveable { mutableStateOf<LocalDate?>(null) }
	val scrollState = rememberScrollState()
	val context = LocalContext.current
	
	// ✅ Cargar datos del gimnasio actual
	LaunchedEffect(Unit) {
		val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
		gymViewModel.loadCurrentGymData(uid)
	}
	
	// ✅ Cargar datos según filtros
	LaunchedEffect(selectedReportType, selectedSubFilter, startDate, endDate) {
		when (selectedReportType) {
			"Pagos" -> viewModel.cargarReportePagos(
				filtro = selectedSubFilter,
				desde = startDate,
				hasta = endDate
			)
			
			"Clientes" -> viewModel.cargarReporteClientes(
				filtro = selectedSubFilter,
				desde = startDate,
				hasta = endDate
			)
			
			"Membresías" -> viewModel.cargarReporteMembresias(
				filtro = selectedSubFilter,
				desde = startDate,
				hasta = endDate
			)
			
			"Promociones" -> viewModel.cargarReportePromociones(
				filtro = selectedSubFilter,
				desde = startDate,
				hasta = endDate
			)
		}
	}
	
	Scaffold(
		snackbarHost = { SnackbarHost(snackbarHostState) },
		topBar = {
			TopAppBar(
				title = { Text("Reportes", color = MaterialTheme.colorScheme.onPrimary) },
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
										"${it.nombreCliente} ${it.apellidoCliente}".trim(),
										it.membresia,
										it.tipoPago,
										it.referencia ?: "-",
										buildString {
											append(formatDollars(it.monto))
											if (it.tipoPago.lowercase() == "mixto") {
												append(" (")
												append(formatDollars(it.montoDolar))
												append(" + ")
												append(formatBolivares(it.montoBolivares))
												append(")")
											}
										}
									)
								}
							)
							
							"Clientes" -> Triple(
								"Reporte de Clientes",
								listOf("Cliente", "Cédula", "Estado", "Teléfono"),
								clientes.map {
									listOf(
										"${it.nombre} ${it.apellido}".trim(),
										it.cedula,
										it.activo?.uppercase() ?: "Desconocido",
										it.telefono,
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
								listOf("Nombre", "Porcentaje", "Duración", "Activa", "Usrs. Tot."),
								promociones.map {
									listOf(
										it.nombre,
										"${it.porcentaje}%",
										"${it.duracion} días",
										if (it.activa) "Sí" else "No",
										"${it.cantidadUsuarios} usuarios",
									)
								}
							)
							
							else -> Triple("Reporte", emptyList(), emptyList())
						}
						
						if (headers.isEmpty() || rows.isEmpty()) return@FloatingActionButton
						
						// ✅ Generar PDF con datos del gimnasio
						val file = PdfReportGenerator.generateReportPdf(
							context = context,
							reportTitle = reportTitle,
							headers = headers,
							rows = rows,
							totalDolares = if (selectedReportType == "Pagos") viewModel.totalDolares else null,
							totalBolivares = if (selectedReportType == "Pagos") viewModel.totalBolivares else null,
							gymName = gymViewModel.currentGym?.name,
							gymRif = gymViewModel.currentGym?.rif
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
		
		Column(
			modifier = Modifier
				.padding(innerPadding)
				.verticalScroll(scrollState)
				.padding(vertical = 6.dp, horizontal = 6.dp)
				.fillMaxSize()
		) {
			
			ReportFilters(
				isPortrait = true,
				selectedReportType = selectedReportType,
				reportTypes = reportTypes,
				onReportTypeSelected = {
					selectedReportType = it
					selectedSubFilter = "Todos"
				},
				selectedSubFilter = selectedSubFilter,
				onSubFilterSelected = { selectedSubFilter = it },
				clientesFilters = clientesFilters,
				pagosFilters = pagosFilters,
				promotionFilter = promotionFilter,
				membershipFilter = membershipFilter,
				startDate = startDate,
				endDate = endDate,
				onStartDateClick = { showDatePicker(context) { date -> startDate = date } },
				onEndDateClick = { showDatePicker(context) { date -> endDate = date } },
				onClearFilters = {
					selectedReportType = "Todos"
					selectedSubFilter = "Todos"
					startDate = null
					endDate = null
				},
				dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"),
				context = context,
				pagos = pagos,
				clientes = clientes
			)
			
			Spacer(Modifier.height(16.dp))
			
			when {
				selectedReportType == "Todos" -> {
					Box(
						modifier = Modifier.fillMaxSize(),
						contentAlignment = Alignment.Center
					) {
						Text(
							"Seleccione un tipo de reporte para mostrar datos",
							textAlign = TextAlign.Center,
							style = MaterialTheme.typography.bodyMedium
						)
					}
				}
				
				isLoading -> {
					Box(
						modifier = Modifier.fillMaxSize(),
						contentAlignment = Alignment.Center
					) {
						CircularProgressIndicator()
					}
				}
				
				errorMessage != null -> {
					Text(
						"Error: $errorMessage",
						color = MaterialTheme.colorScheme.error,
						textAlign = TextAlign.Center
					)
				}
				
				selectedReportType == "Pagos" -> {
					pagos.forEach {
						SimpleReportItem(
							title = "${it.nombreCliente} ${it.apellidoCliente}",
							subtitle = it.membresia,
							extra = it.tipoPago
						)
					}
				}
				
				selectedReportType == "Clientes" -> {
					clientes.forEach {
						SimpleReportItem(
							title = "${it.nombre} ${it.apellido}",
							subtitle = "C.I: ${it.cedula}",
							extra = (it.activo ?: "Desconocido").uppercase(), extraColor = estadoColor(it.activo)
						)
					}
				}
				
				selectedReportType == "Membresías" -> {
					membresias.forEach {
						SimpleReportItem(
							title = it.name,
							subtitle = "${"%.2f".format(it.price)} $",
							extra = if (it.activo) "Activa" else "Inactiva",
							extraColor = if (it.activo) Color(0xFF2E7D32) else Color.Red
						
						)
					}
				}
				
				selectedReportType == "Promociones" -> {
					promociones.forEach {
						SimpleReportItem(
							title = it.nombre,
							subtitle = "${it.porcentaje}%",
							extra = if (it.activa) "Activa" else "Inactiva",
							extraColor = if (it.activa) Color(0xFF2E7D32) else Color.Red
						)
					}
				}
			}
		}
	}
}

@Composable
fun SimpleReportItem(
	title: String,
	subtitle: String,
	extra: String,
	extraColor: Color? = null,
) {
	
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 2.dp), // nuevo padding vertical externo
		shape = RoundedCornerShape(16.dp),
		elevation = CardDefaults.cardElevation(2.dp),
		colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 10.dp, vertical = 10.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Column(
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = title,
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.SemiBold,
					color = MaterialTheme.colorScheme.onSurface
				)
				Text(
					text = subtitle,
					style = MaterialTheme.typography.bodySmall,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
			
			Text(
				text = extra,
				style = MaterialTheme.typography.bodyMedium,
				color = extraColor ?: MaterialTheme.colorScheme.primary,
				modifier = Modifier.padding(start = 8.dp)
			)
		}
	}
	
}


@Composable
fun estadoColor(estado: String?): Color {
	return when (estado?.lowercase()) {
		"activo" -> Color(0xFF2E7D32)        // Verde
		"inactivo" -> Color.Red              // Rojo
		"pendiente" -> Color(0xFFF9A825)    // Amarillo
		else -> MaterialTheme.colorScheme.onSurface
	}
}

@Composable
fun <T> SimpleReportList(
	items: List<T>,
	modifier: Modifier = Modifier,
	field1: (T) -> String,
	field2: (T) -> String,
	extraField: ((T) -> String)? = null,
	extraFieldColor: @Composable ((T) -> Color)? = null,
	emptyMessage: String = "No hay datos para mostrar",
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
						val text = ef(item)
						val color = extraFieldColor?.invoke(item) ?: MaterialTheme.colorScheme.onSurface
						Text(
							text = text,
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
	isPortrait: Boolean,
	selectedReportType: String,
	reportTypes: List<String>,
	onReportTypeSelected: (String) -> Unit,
	selectedSubFilter: String,
	onSubFilterSelected: (String) -> Unit,
	clientesFilters: List<String>,
	promotionFilter: List<String>,
	membershipFilter: List<String>,
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
	
	val scrollState = rememberScrollState()
	
	Surface(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 6.dp, vertical = 4.dp),
		shape = RoundedCornerShape(12.dp),
		color = MaterialTheme.colorScheme.surface,
		shadowElevation = 2.dp
	) {
		Column(
			modifier = Modifier
				.padding(12.dp)
				.then(
					if (!isPortrait) Modifier.verticalScroll(scrollState)
					else Modifier
				)
		) {
			
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
					modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
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
			if (selectedReportType == "Clientes" || selectedReportType == "Pagos" || selectedReportType == "Promociones" || selectedReportType == "Membresías") {
				Spacer(modifier = Modifier.height(8.dp))
				Text(
					text = "Filtros",
					style = MaterialTheme.typography.labelSmall,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
				
				val filters = when (selectedReportType) {
					"Clientes" -> clientesFilters
					"Pagos" -> pagosFilters
					"Promociones" -> promotionFilter
					"Membresías" -> membershipFilter
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
						modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
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


@RequiresApi(Build.VERSION_CODES.O)
fun showDatePicker(context: Context, onDateSelected: (LocalDate) -> Unit) {
	val today = LocalDate.now()
	val datePicker = android.app.DatePickerDialog(
		context,
		{ _, year, month, dayOfMonth ->
			val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
			onDateSelected(selectedDate)
		},
		today.year, today.monthValue - 1, today.dayOfMonth
	)
	datePicker.show()
}
