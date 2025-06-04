package com.jesus.gymcontrol.presentation.ui.commons.CarsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
			.padding(14.dp),
		colors = CardDefaults.cardColors(
			containerColor = Color(0xE0447A9C),
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


