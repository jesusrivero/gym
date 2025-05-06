package com.techcode.gymcontrol.presentation.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.techcode.gymcontrol.presentation.navegation.AppRoutes
import com.techcode.gymcontrol.presentation.ui.commons.BottomNavigationBar
import com.techcode.gymcontrol.presentation.ui.people.PeopleViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: PeopleViewModel) {
	MainContent(
		viewModel = viewModel,
		navBottom = navController,
		navRegister = { navController.navigate(AppRoutes.RegPersonScreen) },
		navEdit = { navController.navigate(AppRoutes.EditPersonScreen(it)) }
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
	viewModel: PeopleViewModel,
	navBottom: NavController,
	navRegister: () -> Unit,
	navEdit: (Int) -> Unit,
) {
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
				onClick = navRegister,
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
			Text(text = "Lista de clientes", color = Color.White, fontWeight = FontWeight.Bold)
		}
		val state = viewModel.state

		Column(
			modifier = Modifier.padding()
		) {
			LazyColumn {
				items(state.userList) { user ->
					Box(
						modifier = Modifier
							.padding(8.dp)
							.fillMaxWidth()

					) {
						Column(modifier = Modifier.padding(12.dp)) {
							Text(text = user.usuario)
							Text(text = user.email)
							IconButton(
								onClick = {
									navEdit.invoke(user.id)
								}
							) {
								Icon(imageVector = Icons.Default.Add, contentDescription = "Editar")

							}
							IconButton(onClick = { viewModel.deleteUser(user) }) {
								Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
							}
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
	MainContent(
		viewModel = viewModel(),
		navEdit = {},
		navBottom = NavController(LocalContext.current),
		navRegister = {}
	)
}
