import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.techcode.gymcontrol.data.db.entity.PersonEntity
import com.techcode.gymcontrol.presentation.ui.people.PeopleViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    navController: NavController,

) {
    // Estados para la UI
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Estados para los filtros
    var selectedReportType by remember { mutableStateOf("Todos") }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var expandedFilter by remember { mutableStateOf(false) }

    // Opciones para el filtro de tipo de reporte
    val reportTypes = listOf(
        "Todos",
        "Clientes",
        "Pagos",
        "Vencimientos"
    )

    // Formateador de fechas
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // Mostrar snackbar si hay errores en las fechas
    LaunchedEffect(startDate, endDate) {
        if (startDate != null && endDate != null && endDate!!.isBefore(startDate)) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    "La fecha final no puede ser anterior a la inicial"
                )
            }
            endDate = startDate
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Reportes",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                painter = painterResource(id = com.techcode.gymcontrol.R.drawable.ic_back),
                                contentDescription = "Regresar",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xBAA7D3DC)
                    )
                )

                // Sección de filtros
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                ) {


                    // Filtro por tipo de reporte
                    FilterDropdown(
                        label = "Tipo de Reporte",
                        selectedValue = selectedReportType,
                        options = reportTypes,
                        expanded = expandedFilter,
                        onExpandedChange = { expandedFilter = it },
                        icon = Icons.Default.ArrowDropDown
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Filtro por rango de fechas
                    Text(
                        text = "Rango de Fechas",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    DateRangeSelector(
                        startDate = startDate,
                        endDate = endDate,
                        dateFormatter = dateFormatter,
                        onStartDateClick = { showStartDatePicker = true },
                        onEndDateClick = {
                            if (startDate == null) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Seleccione primero la fecha inicial"
                                    )
                                }
                                showStartDatePicker = true
                            } else {
                                showEndDatePicker = true
                            }
                        }
                    )

                    // Botones de acción
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                // Aquí iría la lógica para aplicar los filtros
                                // viewModel.filterReports(selectedReportType, startDate, endDate)
                            },
                            enabled = selectedReportType != "Todos" || startDate != null || endDate != null,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Aplicar Filtros")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                selectedReportType = "Todos"
                                startDate = null
                                endDate = null
                                // viewModel.clearFilters()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpiar",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Limpiar")
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {






            // Lista de reportes filtrados
            if (selectedReportType == "Todos" && startDate == null && endDate == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Seleccione filtros para ver los reportes",
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Aquí iría la lista de reportes filtrados
                LazyColumn {
                    // items(viewModel.filteredReports) { report ->
                    //     ReportItem(report = report)
                    // }
                    item {
                        Text("Lista de reportes filtrados aparecerá aquí")
                    }
                }
            }
        }
    }

    // Date Pickers
    val datePickerState = rememberDatePickerState()
    if (showStartDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {

                        datePickerState.selectedDateMillis?.let {
                            startDate = Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            // Si ya había una fecha final y ahora es menor que la inicial, resetear
                            if (endDate != null && endDate!!.isBefore(startDate)) {
                                endDate = null
                            }
                        }
                        showStartDatePicker = false
                    }
                ) {
                    Text("Seleccionar")
                }
            }
        ) {
            DatePicker(state = rememberDatePickerState())
        }
    }

    if (showEndDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {

                        datePickerState.selectedDateMillis?.let {
                            endDate = Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            // Asegurar que no sea menor que la fecha inicial
                            if (startDate != null && endDate!!.isBefore(startDate)) {
                                endDate = startDate
                            }
                        }
                        showEndDatePicker = false
                    }
                ) {
                    Text("Seleccionar")
                }
            }
        ) {
            DatePicker(state = rememberDatePickerState())
        }
    }
}

@Composable
private fun FilterDropdown(
    label: String,
    selectedValue: String,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box {
            Button(
                onClick = { onExpandedChange(true) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(selectedValue)
                    Icon(
                        imageVector = icon,
                        contentDescription = "Desplegar opciones"
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onExpandedChange(false)
                            // Aquí normalmente actualizarías el estado del padre
                        }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DateRangeSelector(
    startDate: LocalDate?,
    endDate: LocalDate?,
    dateFormatter: DateTimeFormatter,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Selector de fecha inicial
        DateSelectorButton(
            label = "Desde",
            date = startDate,
            formatter = dateFormatter,
            onClick = onStartDateClick,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Selector de fecha final
        DateSelectorButton(
            label = "Hasta",
            date = endDate,
            formatter = dateFormatter,
            onClick = onEndDateClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DateSelectorButton(
    label: String,
    date: LocalDate?,
    formatter: DateTimeFormatter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = date?.format(formatter) ?: "Seleccionar",
                    color = if (date != null) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                )
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha"
                )
            }
        }
    }
}