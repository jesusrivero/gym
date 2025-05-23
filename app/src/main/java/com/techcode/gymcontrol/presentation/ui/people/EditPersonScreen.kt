package com.techcode.gymcontrol.presentation.ui.people

import android.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.techcode.gymcontrol.data.db.entity.PersonEntity
import com.techcode.gymcontrol.domain.model.Person


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPersonScreen(navController: NavController, viewModel: PeopleViewModel, id: Int, usuario: String? = null, email: String? = null, cedula: String? = null, numeroTelefono: String? = null) {
	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = {
					Text(text = "Editar Usuario", color = Color.White, fontWeight = FontWeight.Bold)
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = Color(0xBAA7D3DC)
				),
				navigationIcon = {
					IconButton(
						onClick = { navController.popBackStack() }
					) {
						Icon(
								painter = painterResource(id = com.techcode.gymcontrol.R.drawable.ic_back),
						contentDescription = "Regresar", tint = Color.White
						)
					}
				}
			)
		}
	) {
		ContenEditarView(it, navController, viewModel, id, usuario, email, cedula, numeroTelefono)
	}
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContenEditarView(
	it: PaddingValues,
	navController: NavController,
	viewModel: PeopleViewModel,
	id: Int,
	usuario: String?,
	email: String?,
	cedula: String?,
	numeroTelefono: String?
) {
	var usuario by remember { mutableStateOf("") }
	var email by remember { mutableStateOf("") }
	var cedula by remember { mutableStateOf("") }
	var numeroTelefono by remember { mutableStateOf("") }
	
	Column (
		modifier = Modifier
			.padding(it)
			.padding(top = 30.dp)
			.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally
	){
		OutlinedTextField(
			value = usuario,
			onValueChange = { usuario = it },
			label = { Text("usuario") },
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 30.dp)
				.padding(bottom = 15.dp)
		)
		
		OutlinedTextField(
			value = email,
			onValueChange = { email = it },
			label = { Text("email") },
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 30.dp)
				.padding(bottom = 15.dp)
		)
		
		Button(
			
			onClick = {
				val usuario = Person(id = id , usuario = usuario, email = email, cedula = cedula, numeroTelefono = numeroTelefono)
				viewModel.updateUser(usuario)
				navController.popBackStack()
			}, colors = ButtonDefaults.buttonColors(
				containerColor = Color(0xBAA7D3DC)
				
			)
		){
			Text(text = "Editar")
			
			
		}
		
	}
}
