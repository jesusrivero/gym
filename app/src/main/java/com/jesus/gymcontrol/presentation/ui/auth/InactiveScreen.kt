package com.jesus.gymcontrol.presentation.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.domain.viewmodels.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InactiveScreen(
	navController: NavController,
	viewModel: AuthViewModel = hiltViewModel(),
) {
	val colorScheme = MaterialTheme.colorScheme
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = "Cuenta Inactiva",
						color = colorScheme.onPrimary,
						fontWeight = FontWeight.Bold
					)
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = colorScheme.primary
				)
			)
		},
		bottomBar = {
			// Aquí solo la barra de navegación inferior (si la usas)
			// BottomNavigationBar(navController = navController)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.padding(24.dp),
			verticalArrangement = Arrangement.SpaceBetween,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			// Contenido central
			Column(
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Icon(
					imageVector = Icons.Default.Block,
					contentDescription = null,
					tint = MaterialTheme.colorScheme.error,
					modifier = Modifier.size(80.dp)
				)
				
				Spacer(modifier = Modifier.height(16.dp))
				
				Text(
					text = "Cuenta Inactiva",
					style = MaterialTheme.typography.headlineSmall,
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.onBackground
				)
				
				Spacer(modifier = Modifier.height(8.dp))
				
				Text(
					text = "Tu cuenta ha sido suspendida o está inactiva.\nContacta al administrador para más información.",
					style = MaterialTheme.typography.bodyMedium,
					textAlign = TextAlign.Center,
					color = MaterialTheme.colorScheme.onBackground
				)
				Text(
					text = "Importante:\n" +
							"Tu cuenta ha sido suspendida por falta de pago. Mientras no regularices tu situación, no podrás acceder a tus datos ni utilizar las funcionalidades de la aplicación. Además, los reportes y registros asociados a tu cuenta podrían perderse permanentemente si no se cancela el pago a tiempo.\n" +
							"\n" +
							"Por favor, contacta al administrador y realiza el pago correspondiente para reactivar tu cuenta.",
					style = MaterialTheme.typography.bodyMedium,
					textAlign = TextAlign.Center,
					color = MaterialTheme.colorScheme.onBackground
				)
			}
			
		}
	}
}

