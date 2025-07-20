package com.jesus.gymcontrol.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.viewmodels.TutorialViewModel
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import kotlinx.coroutines.launch



@Composable
fun TutorialScreen(
	navController: NavController,
	viewModel: TutorialViewModel = hiltViewModel()
) {
	val pagerState = rememberPagerState(pageCount = { tutorialPages.size })
	val scope = rememberCoroutineScope()
	
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colorScheme.background
	) {
		Box(modifier = Modifier.fillMaxSize()) {
			
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(16.dp)
					.navigationBarsPadding(),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				// 🔷 Espacio para bajar el botón "Omitir"
				Spacer(modifier = Modifier.height(24.dp))
				
				Box(
					Modifier
						.fillMaxWidth()
						.padding(top = 8.dp)
				) {
					Text(
						text = "Omitir",
						style = MaterialTheme.typography.bodyMedium.copy(
							color = MaterialTheme.colorScheme.primary,
							fontWeight = FontWeight.SemiBold
						),
						modifier = Modifier
							.align(Alignment.CenterEnd)
							.clickable {
								viewModel.markTutorialAsShown()
								navController.navigate(AppRoutes.LoginScreen) {
									popUpTo(AppRoutes.TutorialScreen) { inclusive = true }
								}
							}
							.padding(end = 8.dp, top = 8.dp)
					)
				}
				
				HorizontalPager(
					state = pagerState,
					modifier = Modifier
						.weight(1f)
						.fillMaxWidth()
				) { page ->
					val item = tutorialPages[page]
					Column(
						modifier = Modifier
							.fillMaxSize()
							.padding(16.dp),
						horizontalAlignment = Alignment.CenterHorizontally,
						verticalArrangement = Arrangement.Top
					) {
						
						Image(
							painter = painterResource(id = item.imageRes),
							contentDescription = null,
							modifier = Modifier
								.fillMaxWidth()
								.weight(1f)
						)
						Spacer(Modifier.height(32.dp))
						
						Column(
							Modifier.fillMaxWidth(),
							horizontalAlignment = Alignment.Start
						) {
							Text(
								text = item.title,
								style = MaterialTheme.typography.titleLarge.copy(
									fontWeight = FontWeight.Bold
								)
							)
							Spacer(Modifier.height(8.dp))
							Text(
								text = item.description,
								style = MaterialTheme.typography.bodyMedium
							)
						}
					}
				}
				
				Spacer(Modifier.height(16.dp))
				
				DotsIndicator(
					totalDots = tutorialPages.size,
					selectedIndex = pagerState.currentPage,
					modifier = Modifier.align(Alignment.CenterHorizontally)
				)
				
				Spacer(Modifier.height(24.dp))
				
				Button(
					onClick = {
						scope.launch {
							if (pagerState.currentPage < tutorialPages.lastIndex) {
								pagerState.animateScrollToPage(pagerState.currentPage + 1)
							} else {
								viewModel.markTutorialAsShown()
								navController.navigate(AppRoutes.LoginScreen) {
									popUpTo(AppRoutes.TutorialScreen) { inclusive = true }
								}
							}
						}
					},
					modifier = Modifier
						.padding(horizontal = 16.dp, vertical = 16.dp)
						.navigationBarsPadding()
						.fillMaxWidth()
						.height(50.dp)
				) {
					Text(
						text = if (pagerState.currentPage == tutorialPages.lastIndex) "Comenzar" else "Siguiente",
						style = MaterialTheme.typography.bodyLarge.copy(
							fontWeight = FontWeight.Medium
						)
					)
				}
				
				Spacer(Modifier.height(8.dp))
			}
		}
	}
}



data class TutorialPage(
	val imageRes: Int,
	val title: String,
	val description: String
)

val tutorialPages = listOf(
	TutorialPage(
		imageRes = R.drawable.ic_tutorial_1,
		title = "¡Bienvenido a GymControl!",
		description = "La herramienta definitiva para administrar tu gimnasio de forma rápida, eficiente y profesional."
	),
	TutorialPage(
		imageRes = R.drawable.ic_tutorial_2,
		title = "Registra tus clientes",
		description = "Agrega fácilmente nuevos clientes, gestiona su información y mantén un control total sobre su estado y membresía."
	),
	TutorialPage(
		imageRes = R.drawable.ic_tutorial_3,
		title = "Gestiona los pagos",
		description = "Registra y consulta pagos de manera sencilla. Visualiza reportes para mantener tus cuentas al día."
	),
	TutorialPage(
		imageRes = R.drawable.ic_tutorial_4,
		title = "Controla tus promociones",
		description = "Crea, asigna y supervisa promociones especiales para atraer más clientes y fidelizar a los actuales."
	),
	TutorialPage(
		imageRes = R.drawable.ic_tutorial_5,
		title = "Visualiza estadísticas",
		description = "Obtén un resumen visual y detallado del rendimiento de tu gimnasio, con métricas clave al instante."
	)
)



@Composable
fun DotsIndicator(
	totalDots: Int,
	selectedIndex: Int,
	modifier: Modifier = Modifier,
	selectedColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
	unSelectedColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
	dotSize: Dp = 8.dp,
	dotSpacing: Dp = 4.dp
) {
	Row(
		horizontalArrangement = Arrangement.Center,
		modifier = modifier
	) {
		repeat(totalDots) { index ->
			Box(
				modifier = Modifier
					.size(dotSize)
					.padding(horizontal = dotSpacing / 2)
					.background(
						color = if (index == selectedIndex) selectedColor else unSelectedColor,
						shape = CircleShape
					)
			)
		}
	}
}
