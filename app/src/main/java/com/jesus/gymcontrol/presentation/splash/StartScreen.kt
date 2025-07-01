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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import com.jesus.gymcontrol.domain.viewmodels.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun StartScreen(
	navController: NavController,
	viewModel: AuthViewModel = hiltViewModel()
) {
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
		
		val isLoggedIn = viewModel.isLoggedInState
		val hasRole = viewModel.isRoleAssignedState
		
		when {
			isLoggedIn && hasRole -> {
				viewModel.navigateBasedOnRole(navController)
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
	
	// UI con animación de entrada y rebote
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(
				Brush.verticalGradient(
					colors = listOf(
						MaterialTheme.colorScheme.primary,
						MaterialTheme.colorScheme.surface
					)
				)
			),
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
				// Logo animado con rebote
				Image(
					painter = painterResource(id = R.drawable.ic_google),
					contentDescription = "Logo",
					modifier = Modifier
						.size(120.dp)
						.offset(y = bounceOffset.dp)
				)
				
				Spacer(modifier = Modifier.height(32.dp))
				
				CircularProgressIndicator(
					color = MaterialTheme.colorScheme.primary
				)
				
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