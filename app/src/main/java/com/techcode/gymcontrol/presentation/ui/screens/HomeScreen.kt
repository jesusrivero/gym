package com.techcode.gymcontrol.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.techcode.gymcontrol.presentation.ui.componentes.BottomNavigationBar



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
	val context = LocalContext.current
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = "Gym Control") },
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = MaterialTheme.colorScheme.primary
				)
			)
		},
		
		floatingActionButton = {
			FloatingActionButton(
				shape = CircleShape,
				onClick = {},
				containerColor = MaterialTheme.colorScheme.primary,
				
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
				navController = navController,
				modifier = Modifier)
		}
		
				
		
	) { innerPadding ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding),
			contentAlignment = Alignment.Center
		) {
			Text(text = "Lista de clientes")
		}
	}
}

@Preview(showBackground = true)
@Composable
fun HolaMainScreen() {
	MainScreen(navController = NavController(LocalContext.current))
}
