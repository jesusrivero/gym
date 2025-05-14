package com.techcode.gymcontrol.presentation.ui.commons.CarsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MovementsCarScreen() {
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

		getSampleMovimientos().forEach{ item ->
			MovimientoItem(movimiento = item)
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