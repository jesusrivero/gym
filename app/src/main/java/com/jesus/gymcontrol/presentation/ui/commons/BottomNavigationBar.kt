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
import androidx.compose.material3.NavigationBarItemDefaults


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
					painter = painterResource(id = com.jesus.gymcontrol.R.drawable.ad_admin),
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