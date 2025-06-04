package com.jesus.gymcontrol.presentation.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import com.jesus.gymcontrol.presentation.theme.GymTheme
import com.jesus.gymcontrol.presentation.ui.commons.BottomNavigationBar
import com.jesus.gymcontrol.presentation.ui.commons.CarsScreen.MembersCardScreen
import com.jesus.gymcontrol.presentation.ui.commons.CarsScreen.SumaryCarsScreen
import com.jesus.gymcontrol.presentation.ui.commons.CarsScreen.MovementsCarScreen
import com.jesus.gymcontrol.presentation.ui.commons.CarsScreen.WelcomeCard
import com.jesus.gymcontrol.presentation.ui.people.PeopleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
	navController: NavController,
	viewModel: PeopleViewModel = hiltViewModel()
) {
	GymTheme {
		MainContent(
			navBottom = navController,
			navRegister = { navController.navigate(AppRoutes.RegPersonScreen) }
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
	navBottom: NavController,
	navRegister: () -> Unit,
) {
	val colorScheme = MaterialTheme.colorScheme

	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = "Gym Control",
						color = colorScheme.onPrimary,
						fontWeight = FontWeight.Bold
					)
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = colorScheme.primary
				)
			)
		},
		floatingActionButton = {
			FloatingActionButton(
				shape = CircleShape,
				onClick = navRegister,
				containerColor = colorScheme.primary,
				contentColor = colorScheme.onPrimary
			) {
				Icon(
					modifier = Modifier.size(22.dp),
					imageVector = Icons.Default.Add,
					contentDescription = "Agregar"
				)
			}
		},
		bottomBar = {
			BottomNavigationBar(navController = navBottom)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.verticalScroll(rememberScrollState())
		) {
			WelcomeCard(name = "Alberto", date = "12/08/2023")

			Text(
				text = "Resumen General",
				style = MaterialTheme.typography.headlineSmall,
				modifier = Modifier.padding(start = 10.dp),
				color = colorScheme.onBackground
			)

			SumaryCarsScreen()

			Text(
				text = "Resumen de Membresias",
				style = MaterialTheme.typography.headlineSmall,
				modifier = Modifier.padding(start = 10.dp),
				color = colorScheme.onBackground
			)

			MembersCardScreen()

			MovementsCarScreen()
		}
	}
}

@Preview(showBackground = true)
@Composable
fun HolaMainScreen() {
	MainContent(
		navBottom = rememberNavController(),
		navRegister = {}
	)
}
