package com.techcode.gymcontrol.presentation.ui.componentes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
	modifier: Modifier = Modifier
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
	
	if (bottomNavItems.any { it.route == currentRoute }) {
		
		Box (modifier = Modifier.fillMaxWidth()){
			NavigationBar(modifier = Modifier.fillMaxWidth().height(60.dp)) {
				
				bottomNavItems.forEach { item ->
					
					NavigationBarItem(
						modifier = modifier,
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
						onClick = {
							navController.navigate(item.route) {
								
								launchSingleTop = true
								
								restoreState = true
								
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

