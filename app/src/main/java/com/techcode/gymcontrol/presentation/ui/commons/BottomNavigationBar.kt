package com.techcode.gymcontrol.presentation.ui.commons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
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
			icon =  {
				Icon( painter = painterResource(id = com.techcode.gymcontrol.R.drawable.ic_payments), contentDescription = "Registro de pago")
			},
			label = { Text("Registrar Pago") },
			selected = currentRoute == AppRoutes.PaymentsScreen.toString(),
			onClick = {
				navController.navigate(AppRoutes.PaymentsScreen) {
					
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
					Icon(imageVector = Icons.Default.AccountBox, contentDescription = "Administrar")
				
			},
			label = {  Text("Administrar") },
			selected = currentRoute == AppRoutes.ManageScreen.toString(),
			onClick = {
				navController.navigate(AppRoutes.ManageScreen) {
					
					launchSingleTop = true
					
					restoreState = true
					
					popUpTo(navController.graph.startDestinationId) {
						saveState = true
					}
				}
			}
		)
		
		NavigationBarItem(
			icon =  {
				Icon( painter = painterResource(id = com.techcode.gymcontrol.R.drawable.ad_admin), contentDescription = "Preferencias")
			},
			label = { Text("Preferencias") },
			selected = currentRoute == AppRoutes.PreferencesScreen.toString(),
			onClick = {
				navController.navigate(AppRoutes.PreferencesScreen) {
					
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

