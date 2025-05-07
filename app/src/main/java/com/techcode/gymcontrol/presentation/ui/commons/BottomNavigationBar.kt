package com.techcode.gymcontrol.presentation.ui.commons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.techcode.gymcontrol.presentation.navegation.AppRoutes


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
) {
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute = navBackStackEntry?.destination?.route

	

	NavigationBar {
			NavigationBarItem(
				icon =  {
						Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
					},
				label = { Text("Home") },
				selected = currentRoute == AppRoutes.MainScreen.toString(),
				onClick = {
					navController.navigate(AppRoutes.MainScreen) {
					
						launchSingleTop = true
						
						restoreState = true
					
						popUpTo(navController.graph.startDestinationId) {
							saveState = true
						}
					}
				}
			)
		NavigationBarItem(
			icon = {
					Icon(imageVector = Icons.Default.AccountBox, contentDescription = "Registro Pagos")
				
			},
			label = {  Text("Registro Pago") },
			selected = currentRoute == AppRoutes.RegPersonScreen.toString(),
			onClick = {
				navController.navigate(AppRoutes.PersonasScreen) {
					
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


@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
	BottomNavigationBar(
		navController = NavController(LocalContext.current),
	)
}

