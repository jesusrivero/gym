package com.techcode.gymcontrol.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.techcode.gymcontrol.presentation.ui.componentes.BottomNavigationBar
import com.techcode.gymcontrol.presentation.ui.viewmodels.UsuariosViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(it: PaddingValues, navController: NavController, viewModel: UsuariosViewModel) {
	val context = LocalContext.current
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = "Gym Control", color = Color.White, fontWeight = FontWeight.Bold) },
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = MaterialTheme.colorScheme.primary
				)
			)
			
		},
		
		floatingActionButton = {
			FloatingActionButton(
				shape = CircleShape,
				onClick = { navController.navigate(route = "agregar") },
				containerColor = MaterialTheme.colorScheme.primary,
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
				navController = navController,
				innerPadding = PaddingValues()
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
			
			Text(text = "Lista de clientes", color = Color.White, fontWeight = FontWeight.Bold)
			
			
		}
		
	
		ContenInicioView(it, navController, viewModel)
		
	}
}

@Composable
fun ContenInicioView(
	it: PaddingValues,
	navController: NavController,
	viewModel: UsuariosViewModel
) {
	
	
	val state = viewModel.state
	
	Column(
		modifier = Modifier.padding()
	) {
		LazyColumn {
			items(state.listaUsuarios) {
				Box(
					modifier = Modifier
						.padding(8.dp)
						.fillMaxWidth()
				
				) {
					Column(modifier = Modifier.padding(12.dp)) {
						Text(text = it.usuario)
						Text(text = it.email)
						IconButton(onClick = { navController.navigate("editar/${it.id}/${it.usuario}/${it.email}") }) {
							
							Icon(imageVector = Icons.Default.Add, contentDescription = "Editar")
						
						}
						IconButton(onClick = { viewModel.borrarUsuario(it) }) {
							
							Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
						
						}
						
					}
				}
				
			}
		}
	}
	
	
}

@Preview(showBackground = true)
@Composable
fun HolaMainScreen() {
	MainScreen(navController = NavController(LocalContext.current), viewModel = viewModel(), it = PaddingValues())
}
