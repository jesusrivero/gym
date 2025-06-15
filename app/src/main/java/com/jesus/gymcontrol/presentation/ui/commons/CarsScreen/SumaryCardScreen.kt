package com.jesus.gymcontrol.presentation.ui.commons.CarsScreen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jesus.gymcontrol.domain.viewmodels.AdminViewModel

@Composable
fun SummaryCardsScreen(
	viewModel: AdminViewModel = hiltViewModel()
) {
	val summary = viewModel.summary
	val isLoading = viewModel.isLoading
	val errorMessage = viewModel.errorMessage
	val scrollState = rememberScrollState()



	// Llamamos la carga al iniciar
	LaunchedEffect(Unit) {
		viewModel.loadGymUserSummary()
	}

	if (isLoading) {
		Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
			CircularProgressIndicator()
		}
		return
	}

	if (errorMessage != null) {
		Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
			Text(
				text = "Error: $errorMessage",
				color = Color.Red,
				fontWeight = FontWeight.Bold
			)
		}
		return
	}

	Row(
		modifier = Modifier
			.horizontalScroll(scrollState)
			.padding(horizontal = 16.dp, vertical = 8.dp),
		horizontalArrangement = Arrangement.spacedBy(16.dp)
	) {

		SummaryCard(color = Color(0xC92196F3)) {
			SummaryCardContent(title = "Todos", count = summary.total)
		}

		SummaryCard(color = Color(0xCD4CAF50)) {
			SummaryCardContent(title = "Activos", count = summary.activos)
		}

		SummaryCard(color = Color(0xB4E33E3E)) {
			SummaryCardContent(title = "Inactivos", count = summary.inactivos)
		}
	}
}

@Composable
fun SummaryCardContent(title: String, count: Int) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Text(
			text = title,
			color = Color.White,
			fontSize = 16.sp,
			fontWeight = FontWeight.Bold
		)
		Row(verticalAlignment = Alignment.CenterVertically) {
			Icon(
				imageVector = Icons.Default.Person,
				contentDescription = title,
				tint = Color.White,
				modifier = Modifier.size(20.dp)
			)
			Spacer(modifier = Modifier.width(4.dp))
			Text(
				text = count.toString(),
				color = Color.White,
				fontSize = 16.sp
			)
		}
	}
}

@Composable
fun SummaryCard(
	color: Color,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit,
) {
	Card(
		modifier = modifier
			.width(150.dp)
			.height(100.dp),
		colors = CardDefaults.cardColors(containerColor = color),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
		shape = RoundedCornerShape(12.dp)
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp),
			contentAlignment = Alignment.Center
		) {
			content()
		}
	}
}