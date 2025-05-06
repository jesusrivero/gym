package com.techcode.gymcontrol.presentation.ui.people

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.techcode.gymcontrol.presentation.ui.commons.BottomNavigationBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPersonScreen(nav: NavController) {
	LocalContext.current
	Scaffold(
		topBar = {
			TopAppBar(
				modifier = Modifier.fillMaxWidth(),
				title = { Text(text = "Registro de Pagos") },

				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = MaterialTheme.colorScheme.primary
				)
			)
		},

		bottomBar = {
			BottomNavigationBar(
				navController = nav,
				modifier = Modifier.fillMaxWidth(),
			)
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
fun RegistrosScreenPreview() {
	ListPersonScreen(nav = NavController(LocalContext.current))
}
