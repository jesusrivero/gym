package com.techcode.gymcontrol.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.painter.Painter
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
				.verticalScroll(
					rememberScrollState()
				)
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
						shape = RoundedCornerShape(8.dp)
					) {
						Box(modifier = Modifier.fillMaxSize()) {
							Row(
								modifier = Modifier
									.align(Alignment.BottomStart)
									.padding(16.dp)
									.fillMaxWidth(),
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.SpaceBetween
							) {
								Column {
									Text("Personas", fontWeight = FontWeight.Bold)
									Text("Listado de personas")
								}
								Icon(Icons.Default.Menu, contentDescription = "Icono")
							}
						}
					}

					Card(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 6.dp)
							.height(170.dp),
						onClick = { navController.navigate(AppRoutes.ListPaymentsScreen) },
						shape = RoundedCornerShape(8.dp)
					) {
						Box(modifier = Modifier.fillMaxSize()) {
							Row(
								modifier = Modifier
									.align(Alignment.BottomStart)
									.padding(16.dp)
									.fillMaxWidth(),
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.SpaceBetween
							) {
								Column {
									Text("Pagos", fontWeight = FontWeight.Bold)
									Text("Listado de pagos")
								}
								Icon(Icons.Default.Menu, contentDescription = "Icono")
							}
						}
					}
					
					Card(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 6.dp)
							.height(170.dp),
						 onClick = { navController.navigate(AppRoutes.MembershipScreen) },
						shape = RoundedCornerShape(8.dp)
					) {
						Box(modifier = Modifier.fillMaxSize()) {
							Row(
								modifier = Modifier
									.align(Alignment.BottomStart)
									.padding(16.dp)
									.fillMaxWidth(),
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.SpaceBetween
							) {
								Column {
									Text("Membresias", fontWeight = FontWeight.Bold)
									Text("Listado de membresias")
									Text("Listado de membresias")
								}
								Icon(Icons.Default.Menu, contentDescription = "Icono")
							}
						}
					}
					

					
					Card(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 6.dp)
							.height(170.dp),
						onClick = { navController.navigate(AppRoutes.ReportScreen) },
						shape = RoundedCornerShape(8.dp)
					) {
						Box(modifier = Modifier.fillMaxSize()) {
							Row(
								modifier = Modifier
									.align(Alignment.BottomStart)
									.padding(16.dp)
									.fillMaxWidth(),
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.SpaceBetween
							) {
								Column {
									Text("Reportes", fontWeight = FontWeight.Bold)
									Text("Listado de reportes")
								}
								Icon(Icons.Default.Menu, contentDescription = "Icono")
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


