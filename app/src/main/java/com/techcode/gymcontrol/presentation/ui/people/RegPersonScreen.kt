package com.techcode.gymcontrol.presentation.ui.people
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.techcode.gymcontrol.domain.model.Person

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegPersonScreen(
	navController: NavController,
	viewModel: PeopleViewModel = hiltViewModel()
) {
	val colorScheme = MaterialTheme.colorScheme

	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = {
					Text(
						text = "Gym Control",
						color = colorScheme.onPrimary,
						fontWeight = FontWeight.Bold
					)
				},
				colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
					containerColor = colorScheme.primary
				),
				navigationIcon = {
					IconButton(onClick = { navController.popBackStack() }) {
						Icon(
							painter = painterResource(id = com.techcode.gymcontrol.R.drawable.ic_back),
							contentDescription = "Regresar",
							tint = colorScheme.onPrimary
						)
					}
				}
			)
		}
	) { paddingValues ->
		RegPersonContent(
			modifier = Modifier.padding(paddingValues),
			viewModel = viewModel,
			navController = navController
		)
	}
}

@Composable
fun RegPersonContent(
	modifier: Modifier = Modifier,
	viewModel: PeopleViewModel,
	navController: NavController
) {
	var usuario by remember { mutableStateOf("") }
	var email by remember { mutableStateOf("") }
	var cedula by remember { mutableStateOf("") }
	var numeroTelefono by remember { mutableStateOf("") }

	val colorScheme = MaterialTheme.colorScheme

	Column(
		modifier = modifier
			.padding(16.dp)
			.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		OutlinedTextField(
			value = usuario,
			onValueChange = { usuario = it },
			label = { Text("Nombre y apellido") },
			modifier = Modifier.fillMaxWidth()
		)

		Spacer(modifier = Modifier.height(8.dp))

		OutlinedTextField(
			value = email,
			onValueChange = { email = it },
			label = { Text("Introduzca su email") },
			modifier = Modifier.fillMaxWidth()
		)

		Spacer(modifier = Modifier.height(8.dp))

		OutlinedTextField(
			value = cedula,
			onValueChange = { cedula = it },
			label = { Text("Cédula de identidad") },
			modifier = Modifier.fillMaxWidth()
		)

		Spacer(modifier = Modifier.height(8.dp))

		OutlinedTextField(
			value = numeroTelefono,
			onValueChange = { numeroTelefono = it },
			label = { Text("Número de teléfono") },
			modifier = Modifier.fillMaxWidth()
		)

		Spacer(modifier = Modifier.height(16.dp))

		Button(
			onClick = {
				val person = Person(
					usuario = usuario,
					email = email,
					cedula = cedula,
					numeroTelefono = numeroTelefono
				)
				viewModel.saveUser(person)
				navController.popBackStack()
			},
			colors = ButtonDefaults.buttonColors(
				containerColor = colorScheme.primary,
				contentColor = colorScheme.onPrimary
			),
			modifier = Modifier.fillMaxWidth()
		) {
			Text("Agregar", style = MaterialTheme.typography.labelLarge)
		}
	}
}


