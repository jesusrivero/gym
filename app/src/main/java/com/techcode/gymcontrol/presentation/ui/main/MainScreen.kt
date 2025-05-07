package com.techcode.gymcontrol.presentation.ui.main

import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.techcode.gymcontrol.presentation.navegation.AppRoutes
import com.techcode.gymcontrol.presentation.ui.commons.BottomNavigationBar
import com.techcode.gymcontrol.presentation.ui.commons.SlidableCardsRow
import com.techcode.gymcontrol.presentation.ui.commons.UltimosMovimientosScreen
import com.techcode.gymcontrol.presentation.ui.commons.WelcomeCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
	MainContent(
	
		navBottom = navController,
		navRegister = { navController.navigate(AppRoutes.RegPersonScreen) },

	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(

	navBottom: NavController,
	navRegister: () -> Unit,

) {
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = "Gym Control", color = Color.White, fontWeight = FontWeight.Bold) },
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = MaterialTheme.colorScheme.primary
				)
			)
		},
		floatingActionButton = {
			FloatingActionButton(
				shape = CircleShape,
				onClick = navRegister,
				containerColor = MaterialTheme.colorScheme.primary,
				contentColor = Color.White,

				) {
				Icon(
					modifier = Modifier.size(22.dp),
					imageVector = Icons.Default.Add,
					contentDescription = "Agregar",
				)
			}

		},
		bottomBar = {
			BottomNavigationBar(
				navController = navBottom,
			)
		}
	)
	{ innerPadding ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding),
			contentAlignment = Alignment.Center
		) {
			Text(text = "Lista de clientes", color = Color.White, fontWeight = FontWeight.Bold)
			
			
			Column(modifier = Modifier.padding(8.dp)) {
				WelcomeCard(name = "Alberto", date = "12/08/2023")
				
				Text("Resumen General", style = MaterialTheme.typography.headlineSmall)
				SlidableCardsRow()
				UltimosMovimientosScreen()
				
				
				
			}
		}
		

		
	}
}

@Preview(showBackground = true)
@Composable
fun HolaMainScreen() {
	MainContent(
		navBottom = NavController(LocalContext.current),
		navRegister = {}
	)
}
