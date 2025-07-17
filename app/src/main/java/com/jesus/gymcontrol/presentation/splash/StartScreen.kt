package com.jesus.gymcontrol.presentation.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.data.sharedPreferences.PreferencesManager
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import com.jesus.gymcontrol.domain.viewmodels.AuthViewModel
import kotlinx.coroutines.delay



@Composable
fun StartScreen(
	navController: NavController,
	viewModel: AuthViewModel = hiltViewModel()
) {
	val context = LocalContext.current
	val prefs = remember { PreferencesManager(context) }
	
	var visible by remember { mutableStateOf(false) }
	
	// Animación tipo "bounce" para el logo
	val bounceAnim = rememberInfiniteTransition()
	val bounceOffset by bounceAnim.animateFloat(
		initialValue = 0f,
		targetValue = 10f,
		animationSpec = infiniteRepeatable(
			animation = tween(1000, easing = LinearOutSlowInEasing),
			repeatMode = RepeatMode.Reverse
		)
	)
	
	LaunchedEffect(Unit) {
		visible = true
		delay(600)
		viewModel.loadSessionState()
		delay(600)
		
		val isTutorialAlreadyShown = prefs.isTutorialShown()
		val isLoggedIn = viewModel.isLoggedInState
		val hasRole = viewModel.isRoleAssignedState
		
		when {
			!isTutorialAlreadyShown -> {
				navController.navigate(AppRoutes.TutorialScreen) {
					popUpTo(AppRoutes.StartScreen) { inclusive = true }
				}
			}
			
			isLoggedIn && hasRole -> {
				// ✅ Nuevo: verificar estado del usuario en Firestore
				viewModel.checkUserStatusOnStart { state ->
					when (state) {
						"activo" -> {
							viewModel.navigateBasedOnRole(navController)
						}
						"inactivo" -> {
							navController.navigate(AppRoutes.InactiveScreen) {
								popUpTo(AppRoutes.StartScreen) { inclusive = true }
							}
						}
//						"pendiente" -> {
//							navController.navigate(AppRoutes.PendingScreen) {
//								popUpTo(AppRoutes.StartScreen) { inclusive = true }
//							}
//						}
						else -> {
							// estado desconocido o error
							navController.navigate(AppRoutes.LoginScreen) {
								popUpTo(AppRoutes.StartScreen) { inclusive = true }
							}
						}
					}
				}
			}
			
			isLoggedIn && !hasRole -> {
				navController.navigate(AppRoutes.SelectedRolScreen) {
					popUpTo(AppRoutes.StartScreen) { inclusive = true }
				}
			}
			
			else -> {
				navController.navigate(AppRoutes.LoginScreen) {
					popUpTo(AppRoutes.StartScreen) { inclusive = true }
				}
			}
		}
	}
	
	// UI elegante
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background), // fondo blanco por defecto
		contentAlignment = Alignment.Center
	) {
		AnimatedVisibility(
			visible = visible,
			enter = fadeIn(tween(1000)) + scaleIn(initialScale = 0.8f, animationSpec = tween(1000))
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center
			) {
				// Logo con rebote
				Box(contentAlignment = Alignment.Center) {
					Image(
						painter = painterResource(id = R.drawable.ic_background),
						contentDescription = "Logo GymControl",
						modifier = Modifier
							.size(100.dp)
							.offset(y = bounceOffset.dp)
					)
				}
				
				Spacer(modifier = Modifier.height(16.dp))
				
				Text(
					text = "Cargando...",
					style = MaterialTheme.typography.bodyLarge.copy(
						fontWeight = FontWeight.Medium,
						color = MaterialTheme.colorScheme.onSurface
					)
				)
			}
		}
	}
}

