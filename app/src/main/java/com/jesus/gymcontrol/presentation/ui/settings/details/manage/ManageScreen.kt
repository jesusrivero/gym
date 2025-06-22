package com.jesus.gymcontrol.presentation.ui.settings.details.manage

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import com.jesus.gymcontrol.presentation.theme.GymTheme
import com.jesus.gymcontrol.presentation.ui.commons.BottomNavigationBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageScreen(navController: NavController) {
	GymTheme {
		ManagerContent(
			navController = navController,
			navBottom = navController,
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerContent(
	navController: NavController,
	navBottom: NavController,
) {
	val colorScheme = MaterialTheme.colorScheme

	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = "Panel de administración",
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
			BottomNavigationBar(
				navController = navBottom,
			)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.verticalScroll(rememberScrollState())
		) {
			Row(
				modifier = Modifier
					.padding(horizontal = 8.dp, vertical = 8.dp),
				horizontalArrangement = Arrangement.spacedBy(16.dp)
			) {
				Column {
					MenuCard(
						title = "Personas",
						subtitle = "Listado de personas",
						onClick = { navController.navigate(AppRoutes.PersonasScreen) }
					)
					MenuCard(
						title = "Pagos",
						subtitle = "Listado de pagos",
						onClick = { navController.navigate(AppRoutes.ListPaymentsScreen) }
					)
					MenuCard(
						title = "Membresías",
						subtitle = "Listado de membresías",
						onClick = { navController.navigate(AppRoutes.MembershipScreen) }
					)
					MenuCard(
						title = "Promociones",
						subtitle = "Listado de membresías",
						onClick = { navController.navigate(AppRoutes.PromotionsScreen) }
					)
					MenuCard(
						title = "Reportes",
						subtitle = "Listado de reportes",
						onClick = { navController.navigate(AppRoutes.ReportScreen) }
					)
				}
			}
		}
	}
}

@Composable
fun MenuCard(
	title: String,
	subtitle: String,
	onClick: () -> Unit
) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 8.dp)
			.height(170.dp),
		onClick = onClick,
		shape = RoundedCornerShape(12.dp)
	) {
		Box(modifier = Modifier.fillMaxSize()) {

			Image(
				painter = painterResource(id = R.drawable.ic_background),
				contentDescription = null,
				modifier = Modifier.fillMaxSize(),
				contentScale = ContentScale.Crop
			)


			Box(
				modifier = Modifier
					.fillMaxSize()
					.background(Color.Black.copy(alpha = 0.5f))
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
					Text(
						text = title,
						fontWeight = FontWeight.Bold,
						color = Color.White,
						style = MaterialTheme.typography.titleMedium
					)
					Text(
						text = subtitle,
						color = Color.White,
						style = MaterialTheme.typography.bodyMedium
					)
				}
				Icon(
					imageVector = Icons.Default.Menu,
					contentDescription = null,
					tint = Color.White
				)
			} 
		}
	}
}

@Preview(showBackground = true)
@Composable
fun ManagerScreenPreview() {
	GymTheme {
		ManagerContent(
			navBottom = rememberNavController(),
			navController = rememberNavController()
		)
	}
}