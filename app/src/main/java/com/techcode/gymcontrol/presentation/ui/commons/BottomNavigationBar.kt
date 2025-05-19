package com.techcode.gymcontrol.presentation.ui.commons

import android.view.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.techcode.gymcontrol.presentation.navegation.AppRoutes
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.navigation.compose.rememberNavController
import com.techcode.gymcontrol.presentation.theme.GymTheme

@Composable
fun BottomNavigationBar(
	navController: NavController,
	modifier: Modifier = Modifier,
) {
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute = navBackStackEntry?.destination?.route

	NavigationBar(
		containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
		contentColor = MaterialTheme.colorScheme.onSurface
	) {
		NavigationBarItem(
			icon = {
				Icon(
					imageVector = Icons.Default.Home,
					contentDescription = "Home",
					tint = if (currentRoute == AppRoutes.MainScreen.toString()) {
						MaterialTheme.colorScheme.primary
					} else {
						MaterialTheme.colorScheme.onSurfaceVariant
					}
				)
			},
			label = {
				Text(
					"Home",
					color = if (currentRoute == AppRoutes.MainScreen.toString()) {
						MaterialTheme.colorScheme.primary
					} else {
						MaterialTheme.colorScheme.onSurfaceVariant
					}
				)
			},
			selected = currentRoute == AppRoutes.MainScreen.toString(),
			onClick = {
				navController.navigate(AppRoutes.MainScreen) {
					launchSingleTop = true
					restoreState = true
					popUpTo(navController.graph.startDestinationId) {
						saveState = true
					}
				}
			},
			colors = NavigationBarItemDefaults.colors(
				selectedIconColor = MaterialTheme.colorScheme.primary,
				selectedTextColor = MaterialTheme.colorScheme.primary,
				unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
				unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
				indicatorColor = MaterialTheme.colorScheme.primaryContainer
			)
		)

		NavigationBarItem(
			icon = {
				Icon(
					painter = painterResource(id = com.techcode.gymcontrol.R.drawable.ic_payments),
					contentDescription = "Registro de pago",
					tint = if (currentRoute == AppRoutes.PaymentsScreen.toString()) {
						MaterialTheme.colorScheme.primary
					} else {
						MaterialTheme.colorScheme.onSurfaceVariant
					}
				)
			},
			label = {
				Text(
					"Registrar Pago",
					color = if (currentRoute == AppRoutes.PaymentsScreen.toString()) {
						MaterialTheme.colorScheme.primary
					} else {
						MaterialTheme.colorScheme.onSurfaceVariant
					}
				)
			},
			selected = currentRoute == AppRoutes.PaymentsScreen.toString(),
			onClick = {
				navController.navigate(AppRoutes.PaymentsScreen) {
					launchSingleTop = true
					restoreState = true
					popUpTo(navController.graph.startDestinationId) {
						saveState = true
					}
				}
			},
			colors = NavigationBarItemDefaults.colors(
				selectedIconColor = MaterialTheme.colorScheme.primary,
				selectedTextColor = MaterialTheme.colorScheme.primary,
				unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
				unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
				indicatorColor = MaterialTheme.colorScheme.primaryContainer
			)
		)

		NavigationBarItem(
			icon = {
				Icon(
					imageVector = Icons.Default.AccountBox,
					contentDescription = "Administrar",
					tint = if (currentRoute == AppRoutes.ManageScreen.toString()) {
						MaterialTheme.colorScheme.primary
					} else {
						MaterialTheme.colorScheme.onSurfaceVariant
					}
				)
			},
			label = {
				Text(
					"Administrar",
					color = if (currentRoute == AppRoutes.ManageScreen.toString()) {
						MaterialTheme.colorScheme.primary
					} else {
						MaterialTheme.colorScheme.onSurfaceVariant
					}
				)
			},
			selected = currentRoute == AppRoutes.ManageScreen.toString(),
			onClick = {
				navController.navigate(AppRoutes.ManageScreen) {
					launchSingleTop = true
					restoreState = true
					popUpTo(navController.graph.startDestinationId) {
						saveState = true
					}
				}
			},
			colors = NavigationBarItemDefaults.colors(
				selectedIconColor = MaterialTheme.colorScheme.primary,
				selectedTextColor = MaterialTheme.colorScheme.primary,
				unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
				unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
				indicatorColor = MaterialTheme.colorScheme.primaryContainer
			)
		)

		NavigationBarItem(
			icon = {
				Icon(
					painter = painterResource(id = com.techcode.gymcontrol.R.drawable.ad_admin),
					contentDescription = "Preferencias",
					tint = if (currentRoute == AppRoutes.PreferencesScreen.toString()) {
						MaterialTheme.colorScheme.primary
					} else {
						MaterialTheme.colorScheme.onSurfaceVariant
					}
				)
			},
			label = {
				Text(
					"Preferencias",
					color = if (currentRoute == AppRoutes.PreferencesScreen.toString()) {
						MaterialTheme.colorScheme.primary
					} else {
						MaterialTheme.colorScheme.onSurfaceVariant
					}
				)
			},
			selected = currentRoute == AppRoutes.PreferencesScreen.toString(),
			onClick = {
				navController.navigate(AppRoutes.PreferencesScreen) {
					launchSingleTop = true
					restoreState = true
					popUpTo(navController.graph.startDestinationId) {
						saveState = true
					}
				}
			},
			colors = NavigationBarItemDefaults.colors(
				selectedIconColor = MaterialTheme.colorScheme.primary,
				selectedTextColor = MaterialTheme.colorScheme.primary,
				unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
				unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
				indicatorColor = MaterialTheme.colorScheme.primaryContainer
			)
		)
	}
}

