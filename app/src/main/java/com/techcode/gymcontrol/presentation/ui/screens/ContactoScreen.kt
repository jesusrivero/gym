package com.techcode.gymcontrol.presentation.ui.screens
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(navController: NavController) {
	ManagerContent(
		navController = navController,
		navBottom = navController,
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactContent(
	navController: NavController,
	navBottom: NavController,
	
	
	) {
	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = "",
						color = Color.White,
						fontWeight = FontWeight.Bold
					)
				},navigationIcon = {
					IconButton(
						onClick = { }
					) {
						Icon(
							painter = painterResource(id = com.techcode.gymcontrol.R.drawable.ic_back),
							contentDescription = "Regresar", tint = Color.White
						)
					}
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = Color(0xBAA7D3DC)
				)
			)
		},
	)
	{ innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.verticalScroll(
					rememberScrollState()
				)
		) {
			Row(
				modifier = Modifier
					.padding(horizontal = 4.dp, vertical = 4.dp),
				horizontalArrangement = Arrangement.spacedBy(16.dp)
			){

			
			
			
			
			
			
			}
			
			}
		
	}
	
	
}



@Preview(showBackground = true)
@Composable
fun ContactoScreenPreview() {
	ContactContent(
		navBottom = NavController(LocalContext.current),
		navController = NavController(LocalContext.current)
	)
}
