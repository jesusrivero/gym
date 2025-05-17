package com.techcode.gymcontrol.presentation.ui.commons.CarsScreen

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MembersCardScreen() {
	val scrollState = rememberScrollState()
	
	Row(
		modifier = Modifier
			.horizontalScroll(scrollState)
			.padding(horizontal = 16.dp, vertical = 8.dp),
		horizontalArrangement = Arrangement.spacedBy(16.dp)
	) {
		MembersCard(
			color = Color(0xCE447A9C),
			modifier = Modifier
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center
			) {
				Text(
					text = "Semanal",
					color = Color.White,
					fontSize = 16.sp,
					fontWeight = FontWeight.Bold
				)
				Row(verticalAlignment = Alignment.CenterVertically) {
					
					Icon(
						imageVector = Icons.Default.Person,
						contentDescription = "Car",
						tint = Color.White,
						modifier = Modifier.size(20.dp)
					)
					
					Spacer(modifier = Modifier.height(4.dp))
					
					
					Text(
						text = "50",
						color = Color.White,
						fontSize = 16.sp
					)
				}
			}
		}
		
		
		MembersCard(
			color = Color(0xCE447A9C),
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center
			) {
				
				Text(
					text = "Quincenal",
					color = Color.White,
					fontSize = 16.sp,
					fontWeight = FontWeight.Bold
				)
				Row(verticalAlignment = Alignment.CenterVertically) {
					Icon(
						imageVector = Icons.Default.Person,
						contentDescription = "Activos",
						tint = Color.White,
						modifier = Modifier.size(20.dp)
					)
					Spacer(modifier = Modifier.height(4.dp))
					
					Text(
						text = "25",
						color = Color.White,
						fontSize = 16.sp
					)
				}
			}
			
		}
		
		
		
		MembersCard(
			color = Color(0xCE447A9C),
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center
			) {
				Text(
					text = "Mensual",
					color = Color.White,
					fontSize = 16.sp,
					fontWeight = FontWeight.Bold
				)
				Row(verticalAlignment = Alignment.CenterVertically) {
					Icon(
						imageVector = Icons.Default.Person,
						contentDescription = "Inactivos",
						tint = Color.White,
						modifier = Modifier.size(20.dp)
					)
					Spacer(modifier = Modifier.height(4.dp))
					
					Text(
						text = "25",
						color = Color.White,
						fontSize = 16.sp
					)
					
				}
				
				
			}
		}
	}
}

@Composable
fun MembersCard(
	color: Color,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit,
) {
	Card(
		modifier = modifier
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
			content()
		}
	}
}
