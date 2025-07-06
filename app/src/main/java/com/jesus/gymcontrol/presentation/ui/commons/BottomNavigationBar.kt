package com.jesus.gymcontrol.presentation.ui.commons


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jesus.gymcontrol.presentation.navegation.AppRoutes

fun navigateIfNeeded(
	navController: NavController,
	destination: String,
	currentRoute: String?
) {
	if (currentRoute != destination) {
		navController.navigate(destination) {
			launchSingleTop = true
			restoreState = true
			popUpTo(navController.graph.startDestinationId) {
				saveState = true
			}
		}
	}
}

@Composable
fun BottomNavigationBar(
	navController: NavController,
	modifier: Modifier = Modifier,
) {
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute = navBackStackEntry?.destination?.route
	
	// Usa las rutas tal como las espera NavHost
	val mainRoute = AppRoutes.MainScreen::class.qualifiedName!!
	val manageRoute = AppRoutes.ManageScreen::class.qualifiedName!!
	val preferencesRoute = AppRoutes.PreferencesScreen::class.qualifiedName!!
	
	NavigationBar(
		containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
		contentColor = MaterialTheme.colorScheme.onSurface
	) {
		NavigationBarItem(
			icon = {
				Icon(Icons.Default.Home, contentDescription = "Home")
			},
			label = { Text("Home") },
			selected = currentRoute == mainRoute,
			onClick = {
				navigateIfNeeded(navController, mainRoute, currentRoute)
			}
		)
		
		NavigationBarItem(
			icon = {
				Icon(Icons.Default.AccountBox, contentDescription = "Administrar")
			},
			label = { Text("Administrar") },
			selected = currentRoute == manageRoute,
			onClick = {
				navigateIfNeeded(navController, manageRoute, currentRoute)
			}
		)
		
		NavigationBarItem(
			icon = {
				Icon(
					painter = painterResource(id = com.jesus.gymcontrol.R.drawable.ad_admin),
					contentDescription = "Preferencias"
				)
			},
			label = { Text("Preferencias") },
			selected = currentRoute == preferencesRoute,
			onClick = {
				navigateIfNeeded(navController, preferencesRoute, currentRoute)
			}
		)
	}
}