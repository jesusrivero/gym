package com.techcode.gymcontrol.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.techcode.gymcontrol.presentation.ui.people.PeopleViewModel

@Composable
fun PersonsScreen(
	viewModel: PeopleViewModel,
	navEdit: (Int) -> Unit,
){
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
						Text(text = user.usuario, color = Color.Black, fontWeight = FontWeight.Bold)
						Text(text = user.email, color = Color.Black, fontWeight = FontWeight.Bold)
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
