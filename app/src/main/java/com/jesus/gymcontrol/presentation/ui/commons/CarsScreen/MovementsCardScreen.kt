package com.jesus.gymcontrol.presentation.ui.commons.CarsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jesus.gymcontrol.domain.model.Payment
import com.jesus.gymcontrol.domain.viewmodels.MovementsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MovementsCarScreen(viewModel: MovementsViewModel = hiltViewModel()) {
	val payments  by remember { derivedStateOf { viewModel.lastPayments } }
	
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp)
	) {
		Text(
			text = "Últimos Movimientos",
			style = MaterialTheme.typography.headlineSmall.copy(
				fontWeight = FontWeight.Bold
			),
			modifier = Modifier.padding(bottom = 16.dp)
		)
		
		PaymentsList(payments =payments)
	}
}


@Composable
fun PaymentsList(payments: List<Payment>) {
	if (payments.isEmpty()) {
		Text(
			text = "Sin movimientos recientes",
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
	} else {
		payments.forEach { payment ->
			MovementsCard(color = Color(0xFF4CAF50)) {
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					Column(modifier = Modifier.weight(1f)) {
						Row(verticalAlignment = Alignment.CenterVertically) {
							Text(
								text = "$",
								style = MaterialTheme.typography.bodyLarge.copy(
									fontWeight = FontWeight.Bold,
									color = MaterialTheme.colorScheme.primary
								),
								modifier = Modifier.padding(end = 8.dp)
							)
							Text(
								text = "Pago de ${payment.name}",
								style = MaterialTheme.typography.bodyLarge.copy(
									fontWeight = FontWeight.SemiBold
								)
							)
						}
						
						Text(
							text = formatDate(payment.date),
							style = MaterialTheme.typography.bodySmall,
							color = MaterialTheme.colorScheme.onSurfaceVariant,
							modifier = Modifier.padding(top = 4.dp)
						)
					}
					
					Text(
						text = "${payment.amount}$",
						style = MaterialTheme.typography.bodyLarge.copy(
							fontWeight = FontWeight.Bold,
							color = MaterialTheme.colorScheme.primary
						)
					)
				}
			}
			Spacer(modifier = Modifier.height(6.dp))
		}
	}
}


@Composable
fun MovementsCard(
    color: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()

            )

            content()
        }
    }

@Composable
fun formatDate(timestamp: Long): String {
	val sdf = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
	return sdf.format(Date(timestamp))
}