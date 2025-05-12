package com.techcode.gymcontrol.presentation.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.techcode.gymcontrol.presentation.navegation.AppRoutes
import com.techcode.gymcontrol.presentation.ui.commons.BottomNavigationBar
import com.techcode.gymcontrol.presentation.ui.commons.CarsScreen.MembersCardScreen
import com.techcode.gymcontrol.presentation.ui.commons.CarsScreen.SumaryCarsScreen
import com.techcode.gymcontrol.presentation.ui.commons.CarsScreen.MovementsCarScreen
import com.techcode.gymcontrol.presentation.ui.commons.CarsScreen.WelcomeCard


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
					containerColor = Color(0xBAA7D3DC)
				)
			)
		},
		floatingActionButton = {
			FloatingActionButton(
				shape = CircleShape,
				onClick = navRegister,
				containerColor = Color(0xFFA7D3DC),
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
			
			
			Column(modifier = Modifier.padding(8.dp)) {
				WelcomeCard(name = "Alberto", date = "12/08/2023")
				Text("Resumen General", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(start = 15.dp))
				SumaryCarsScreen()
				Text("Resumen de Membresias", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(start = 15.dp))
				MembersCardScreen()
				MovementsCarScreen()
				
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
