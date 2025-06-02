package com.techcode.gymcontrol.presentation.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.techcode.gymcontrol.presentation.navegation.AppRoutes
import com.techcode.gymcontrol.presentation.ui.commons.BottomNavigationBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageScreen(navController: NavController) {
	ManagerContent(
		navController = navController,
		navBottom = navController,
		)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerContent(
	navController: NavController,
	navBottom: NavController,
	) {
	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = "Panel de administracion",
						color = Color.White,
						fontWeight = FontWeight.Bold
					)
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = Color(0xBAA7D3DC)
				)
			)
		},
		bottomBar = {
			BottomNavigationBar(
				navController = navBottom,
			)
		}
	)
	{ innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.verticalScroll(rememberScrollState())
		) {
			Row(
				modifier = Modifier
					.padding(horizontal = 4.dp, vertical = 4.dp),
				horizontalArrangement = Arrangement.spacedBy(16.dp)
			){
				Column (){
					
					Card(
						modifier = Modifier
							.fillMaxWidth()
							.height(170.dp),
						onClick = { navController.navigate(AppRoutes.PersonasScreen) },
						shape = RoundedCornerShape(8.dp),

					) {
						Box(modifier = Modifier.fillMaxSize()) {
							Image(
								painter = painterResource(id = com.techcode.gymcontrol.R.drawable.ic_background),
								contentDescription = "Fondo de la tarjeta",
								modifier = Modifier.fillMaxSize(),
								contentScale = ContentScale.Crop
							)
							Row(
								modifier = Modifier
									.align(Alignment.BottomStart)
									.padding(16.dp)
									.fillMaxWidth(),
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.SpaceBetween
							) {
								Column {
									Text("Personas", fontWeight = FontWeight.Bold, color = Color.White)
									Text("Listado de personas", color = Color.White)
								}
								Icon(Icons.Default.Menu, contentDescription = "Icono", tint = Color.White)
							}
						}
					}

					Card(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 6.dp)
							.height(170.dp)
							.background(Color.White),
						onClick = { navController.navigate(AppRoutes.ListPaymentsScreen) },
						shape = RoundedCornerShape(8.dp)
					) {
						Box(modifier = Modifier.fillMaxSize()) {
							Image(
								painter = painterResource(id = com.techcode.gymcontrol.R.drawable.ic_background),
								contentDescription = "Fondo de la tarjeta",
								modifier = Modifier.fillMaxSize(),
								contentScale = ContentScale.Crop
							)
							Row(
								modifier = Modifier
									.align(Alignment.BottomStart)
									.padding(16.dp)
									.fillMaxWidth(),
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.SpaceBetween
							) {
								Column {
									Text("Pagos", fontWeight = FontWeight.Bold, color = Color.White)
									Text("Listado de pagos", color = Color.White)
								}
								Icon(Icons.Default.Menu, contentDescription = "Icono", tint = Color.White)
							}
						}
					}
					
					Card(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 6.dp)
							.height(170.dp)
						   .background(Color.White),
						 onClick = { navController.navigate(AppRoutes.MembershipScreen) },
						shape = RoundedCornerShape(8.dp)
					) {
						Box(modifier = Modifier.fillMaxSize()) {
							Image(
								painter = painterResource(id = com.techcode.gymcontrol.R.drawable.ic_background),
								contentDescription = "Fondo de la tarjeta",
								modifier = Modifier.fillMaxSize(),
								contentScale = ContentScale.Crop
							)
							Row(
								modifier = Modifier
									.align(Alignment.BottomStart)
									.padding(16.dp)
									.fillMaxWidth(),
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.SpaceBetween
							) {
								Column {
									Text("Membresias", fontWeight = FontWeight.Bold, color = Color.White)
									Text("Listado de membresias", color = Color.White)
								}
								Icon(Icons.Default.Menu, contentDescription = "Icono", tint = Color.White)
							}
						}
					}
					

					
					Card(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 6.dp)
							.height(170.dp)
							.background(Color.White),
						onClick = { navController.navigate(AppRoutes.ReportScreen) },
						shape = RoundedCornerShape(8.dp)
					) {
						Box(modifier = Modifier.fillMaxSize()) {
							Image(
								painter = painterResource(id = com.techcode.gymcontrol.R.drawable.ic_background),
								contentDescription = "Fondo de la tarjeta",
								modifier = Modifier.fillMaxSize(),
								contentScale = ContentScale.Crop
							)
							Row(
								modifier = Modifier
									.align(Alignment.BottomStart)
									.padding(16.dp)
									.fillMaxWidth(),
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.SpaceBetween
							) {
								Column {
									Text("Reportes", fontWeight = FontWeight.Bold, color = Color.White)
									Text("Listado de reportes", color = Color.White)
								}
								Icon(Icons.Default.Menu, contentDescription = "Icono", tint = Color.White)
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
fun ManagerScreenPreview() {
	ManagerContent(
		navBottom = NavController(LocalContext.current),
		navController = NavController(LocalContext.current)
	)
}


