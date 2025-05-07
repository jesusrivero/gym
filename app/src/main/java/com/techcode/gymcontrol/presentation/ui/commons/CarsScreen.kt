package com.techcode.gymcontrol.presentation.ui.commons

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun WelcomeCard(name: String, date: String) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp),
		colors = CardDefaults.cardColors(
			containerColor = Color(0xFF6200EE),
		),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
	) {
		Column(
			modifier = Modifier
				.padding(24.dp)
				.fillMaxWidth(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.Start
		) {
			Text(
				text = "Bienvenido, $name",
				style = MaterialTheme.typography.headlineMedium.copy(
					fontWeight = FontWeight.Bold,
					color = Color.White
				)
			)
			Text(
				text = "Hoy es $date",
				style = MaterialTheme.typography.bodyLarge.copy(
					color = Color.White.copy(alpha = 0.9f),
					fontSize = 16.sp
				),
				modifier = Modifier.padding(top = 8.dp)
			)
		}
	}
}

@Composable
fun SlidableCardsRow() {
	val scrollState = rememberScrollState()
	
	Row(
		modifier = Modifier
			.horizontalScroll(scrollState)
			.padding(horizontal = 16.dp, vertical = 8.dp),
		horizontalArrangement = Arrangement.spacedBy(16.dp)
	) {
		
		SummaryCard(title = "Resumen 1", color = Color(0xFF4CAF50))
		
		
		SummaryCard(title = "Resumen 2", color = Color(0xFF2196F3))
		
		
		SummaryCard(title = "Resumen 3", color = Color(0xFF9C27B0))
	}
}

@Composable
fun SummaryCard(title: String, color: Color) {
	Card(
		modifier = Modifier
			.width(280.dp)
			.height(120.dp),
		colors = CardDefaults.cardColors(containerColor = color),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp),
			contentAlignment = Alignment.Center
		) {
			Text(
				text = title,
				color = Color.White,
				fontSize = 20.sp,
				fontWeight = FontWeight.Bold
			)
		}
	}
}
@Composable
fun UltimosMovimientosScreen() {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
	) {
		
		Text(
			text = "Últimos Movimientos",
			style = MaterialTheme.typography.headlineSmall.copy(
				fontWeight = FontWeight.Bold
			),
			modifier = Modifier.padding(bottom = 16.dp)
		)
		
		
		MovimientosList(movimientos = getSampleMovimientos())
	}
}

@Composable
fun MovimientosList(movimientos: List<Movimiento>) {
	LazyColumn(
		verticalArrangement = Arrangement.spacedBy(12.dp)
	) {
		items(movimientos.size) { movimiento ->
			MovimientoItem(movimiento = movimientos[movimiento])
		}
	}
}

@Composable
fun MovimientoItem(movimiento: Movimiento) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant,
		),
		elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Column(
				modifier = Modifier.weight(1f)
			) {
				
				Row(
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = "$",
						style = MaterialTheme.typography.bodyLarge.copy(
							fontWeight = FontWeight.Bold,
							color = MaterialTheme.colorScheme.primary
						),
						modifier = Modifier.padding(end = 8.dp)
					)
					Text(
						text = movimiento.descripcion,
						style = MaterialTheme.typography.bodyLarge.copy(
							fontWeight = FontWeight.SemiBold
						)
					)
				}
				
				
				Text(
					text = movimiento.fecha,
					style = MaterialTheme.typography.bodySmall,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					modifier = Modifier.padding(top = 4.dp)
				)
			}
			
			
			Text(
				text = movimiento.monto,
				style = MaterialTheme.typography.bodyLarge.copy(
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.primary
				)
			)
		}
	}
}


data class Movimiento(
	val descripcion: String,
	val fecha: String,
	val monto: String
)

fun getSampleMovimientos(): List<Movimiento> {
	return listOf(
		Movimiento("Pago de Jesus Rivero", "05/06/2025", "15$"),
		Movimiento("Inscripcion Nueva - P1", "03/01/2025", "4$"),
		Movimiento("Inscripción nueva - P3", "05/05/2025", "4$"),
		Movimiento("Pago de cuota mensual", "04/05/2025", "15$"),
		Movimiento("Renovación membresía", "03/05/2025", "15$")
	)
}