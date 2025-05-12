package com.techcode.gymcontrol.presentation.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.techcode.gymcontrol.presentation.ui.commons.BottomNavigationBar
import com.techcode.gymcontrol.presentation.ui.people.PeopleViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonsScreen(
	navBottom: NavController,
	viewModel: PeopleViewModel,
	navEdit: (Int) -> Unit,
) {

	var searchText by remember { mutableStateOf("") }
	
	Scaffold(
		topBar = {
			Column {
				TopAppBar(
					title = {
						Text(
							text = "Listado de Personas",
							color = Color.White,
							fontWeight = FontWeight.Bold
						)
					},
					colors = TopAppBarDefaults.topAppBarColors(
						containerColor = Color(0xBAA7D3DC)
					)
				)
				
				SearchBar(
					modifier = Modifier
						.fillMaxWidth(),
					query = searchText,
					onQueryChange = { searchText = it },
					onSearch = { },
					active = false,
					onActiveChange = {},
					placeholder = { Text("Buscar personas...") },
					leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
					shape = MaterialTheme.shapes.large,
					colors = SearchBarDefaults.colors(
						containerColor = MaterialTheme.colorScheme.surface,
						inputFieldColors = SearchBarDefaults.inputFieldColors(
							focusedTextColor = MaterialTheme.colorScheme.onSurface,
							unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
							focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
							unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
							focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
							unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
						)
					)
				) {}
			}
		},
		bottomBar = {
			BottomNavigationBar(
				navController = navBottom,
			)
		}
	) { innerPadding ->
		val state = viewModel.state
		

		val filteredList = if (searchText.isBlank()) {
			state.userList
		} else {
			state.userList.filter { user ->
				user.usuario.contains(searchText, ignoreCase = true) ||
						user.email.contains(searchText, ignoreCase = true)
			}
		}
		
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding),
			contentAlignment = Alignment.Center
		) {
			if (filteredList.isEmpty()) {
				Text(
					text = if (searchText.isNotEmpty()) "No se encontraron resultados" else "No hay personas registradas",
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			} else {
				LazyColumn(
					modifier = Modifier
						.fillMaxSize()
						.padding(horizontal = 12.dp),
					verticalArrangement = Arrangement.spacedBy(8.dp)
				) {
					items(filteredList) { user ->
						Surface(
							modifier = Modifier
								.fillMaxWidth()
								.padding(vertical = 4.dp),
							shape = RoundedCornerShape(12.dp),
							color = MaterialTheme.colorScheme.surface,
							border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
						) {
							Box(
								modifier = Modifier.padding(12.dp)
							) {
								Row(
									modifier = Modifier.fillMaxWidth(),
									verticalAlignment = Alignment.CenterVertically
								) {
									Column(
										modifier = Modifier.weight(1f)
									) {
										Text(
											text = user.usuario,
											style = MaterialTheme.typography.titleMedium,
											fontWeight = FontWeight.SemiBold,
											color = MaterialTheme.colorScheme.onSurface
										)
										
										Spacer(modifier = Modifier.height(4.dp))
										
										Text(
											text = user.email,
											style = MaterialTheme.typography.bodyMedium,
											color = MaterialTheme.colorScheme.onSurfaceVariant
										)
									}
									
									Row {
										IconButton(
											onClick = { navEdit(user.id) },
											modifier = Modifier.size(40.dp)
										) {
											Icon(
												imageVector = Icons.Default.Edit,
												contentDescription = "Editar",
												tint = MaterialTheme.colorScheme.primary
											)
										}
										
										IconButton(
											onClick = { viewModel.deleteUser(user) },
											modifier = Modifier.size(40.dp)
										) {
											Icon(
												imageVector = Icons.Default.Delete,
												contentDescription = "Eliminar",
												tint = MaterialTheme.colorScheme.error
											)
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}