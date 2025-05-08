package com.techcode.gymcontrol.presentation.ui.commons.CarsScreen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SumaryCarsScreen() {
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
			.width(150.dp)
			.height(100.dp),
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