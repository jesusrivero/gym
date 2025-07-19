package com.jesus.gymcontrol.presentation.ui.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun PendingOverlay() {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.zIndex(1f), // Asegura que está encima
		contentAlignment = Alignment.Center
	) {
		Box(
			modifier = Modifier
				.background(
					color = Color(0xCCFFF3CD),
					shape = RoundedCornerShape(8.dp)
				)
				.padding(16.dp)
		) {
			Text(
				text = "⚠️ Tu cuenta está pendiente de pago",
				color = Color(0xFF856404),
				style = MaterialTheme.typography.bodyLarge
			)
		}
	}
}
