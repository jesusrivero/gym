package com.techcode.gymcontrol.presentation.ui.people

import android.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.techcode.gymcontrol.data.db.entity.PersonEntity
import com.techcode.gymcontrol.domain.model.Person
import com.techcode.gymcontrol.presentation.theme.GymTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPersonScreen(
	navController: NavController,
	viewModel: PeopleViewModel,
	id: Int,
	usuario: String? = null,
	email: String? = null,
	cedula: String? = null,
	numeroTelefono: String? = null
) {
	val colorScheme = MaterialTheme.colorScheme

	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = {
					Text(
						text = "Editar Usuario",
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
		EditPersonContent(
			modifier = Modifier.padding(paddingValues),
			viewModel = viewModel,
			navController = navController,
			id = id,
			initialUsuario = usuario.orEmpty(),
			initialEmail = email.orEmpty(),
			initialCedula = cedula.orEmpty(),
			initialNumeroTelefono = numeroTelefono.orEmpty()
		)
	}
}

@Composable
fun EditPersonContent(
	modifier: Modifier = Modifier,
	viewModel: PeopleViewModel,
	navController: NavController,
	id: Int,
	initialUsuario: String,
	initialEmail: String,
	initialCedula: String,
	initialNumeroTelefono: String
) {
	var usuario by remember { mutableStateOf(initialUsuario) }
	var email by remember { mutableStateOf(initialEmail) }
	var cedula by remember { mutableStateOf(initialCedula) }
	var numeroTelefono by remember { mutableStateOf(initialNumeroTelefono) }

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
			label = { Text("Correo electrónico") },
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
				val updatedPerson = Person(
					id = id,
					usuario = usuario,
					email = email,
					cedula = cedula,
					numeroTelefono = numeroTelefono
				)
				viewModel.updateUser(updatedPerson)
				navController.popBackStack()
			},
			colors = ButtonDefaults.buttonColors(
				containerColor = colorScheme.primary,
				contentColor = colorScheme.onPrimary
			),
			modifier = Modifier.fillMaxWidth()
		) {
			Text("Guardar cambios", style = MaterialTheme.typography.labelLarge)
		}
	}


}




