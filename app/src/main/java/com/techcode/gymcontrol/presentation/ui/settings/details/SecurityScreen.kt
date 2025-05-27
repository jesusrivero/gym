package com.techcode.gymcontrol.presentation.ui.settings.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.techcode.gymcontrol.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityScreen(
	navController: NavController,
) {
	SecurityContent(
		navBottom = navController
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityContent(
	navBottom: NavController,
) {
	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = "Seguridad",
						color = Color.White,
						fontWeight = FontWeight.Medium
					)
				},
				navigationIcon = {
					IconButton(
						onClick = { navBottom.popBackStack() }
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_back),
							contentDescription = "Regresar",
							tint = Color.White
						)
					}
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = Color(0xBAA7D3DC)
				)
			)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Top
		) {
			
			
			Text(text = "PEPE MARICO")
			
			
		}
	}
}





