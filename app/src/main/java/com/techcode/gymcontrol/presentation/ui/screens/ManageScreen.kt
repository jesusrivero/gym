package com.techcode.gymcontrol.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.techcode.gymcontrol.presentation.ui.commons.BottomNavigationBar

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ManageScreen(navController: NavController) {
//	ManagerContent(

//		navBottom = navController,
//		navRegister = { navController.navigate(AppRoutes.RegPersonScreen) },

//		)
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageScreen(
	navBottom: NavController,
	
) {
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = "Panel de Administracion", color = Color.White, fontWeight = FontWeight.Bold) },
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = Color(0xBAA7D3DC)
				)
			)
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
				Text(text = "Hola")
				
			}
		}
		
		
	}
}

@Preview(showBackground = true)
@Composable
fun ManagerScreenPreview() {
	ManageScreen(
		navBottom = NavController(LocalContext.current),
		
	)
}