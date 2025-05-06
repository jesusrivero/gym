package com.techcode.gymcontrol.presentation.ui.componentes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.techcode.gymcontrol.presentation.ui.screens.AppScreens


data class BottomNavItem(
	val name: String,
	val route: String,
	val icon: ImageVector,
	val badgeCount: Int = 0
)

@Composable
fun BottomNavigationBar(
	
	navController: NavController,
	modifier: Modifier = Modifier,
	innerPadding: PaddingValues
) {
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute = navBackStackEntry?.destination?.route
	val bottomNavItems = listOf(
		BottomNavItem(
			name = "Home",
			route = AppScreens.HomeScreen.route,
			icon = Icons.Default.Home,
			badgeCount = 0
		),
		BottomNavItem(
			name = "Registros",
			route = AppScreens.RegistrosScreen.route,
			icon = Icons.Default.Home,
			badgeCount = 0
		
		)
	)
	
	Box(){
		if (bottomNavItems.any { it.route == currentRoute }) {
			
			
			NavigationBar( modifier = modifier.height(60.dp)
			
			) {
				
				bottomNavItems.forEach { item ->
					NavigationBarItem(
						icon = {
							BadgedBox(badge = {
								if (item.badgeCount > 0) {
									Badge { Text(item.badgeCount.toString()) }
								}
							}) {
								Icon(item.icon, contentDescription = item.name)
							}
						},
						label = { Text(item.name) },
						selected = currentRoute == item.route,
						modifier = modifier.height(10.dp),
						onClick = {
							navController.navigate(item.route) {
								// Evita múltiples copias del mismo destino
								launchSingleTop = true
								// Restaura el estado al volver a un destino existente
								restoreState = true
								// Configura el comportamiento de navegación
								popUpTo(navController.graph.startDestinationId) {
									saveState = true
								}
							}
						}
					)
				}
			}
			
			
		}
	
	}
	
	
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
	BottomNavigationBar(
		navController = NavController(LocalContext.current),
		innerPadding = PaddingValues()
	)
}

