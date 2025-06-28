package com.jesus.gymcontrol.presentation.ui.main


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.domain.model.GymUserSummary
import com.jesus.gymcontrol.domain.model.MembershipWithCount
import com.jesus.gymcontrol.domain.model.Payment
import com.jesus.gymcontrol.domain.viewmodels.AdminViewModel
import com.jesus.gymcontrol.domain.viewmodels.MembershipViewModel
import com.jesus.gymcontrol.domain.viewmodels.MovementsViewModel
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import com.jesus.gymcontrol.presentation.theme.GymTheme
import com.jesus.gymcontrol.presentation.ui.commons.BottomNavigationBar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
	navController: NavController,
) {
	GymTheme {
		MainContent(
			navBottom = navController,
			navRegister = { navController.navigate(AppRoutes.RegPersonScreen) }
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
	navBottom: NavController,
	navRegister: () -> Unit,
	viewModel: AdminViewModel = hiltViewModel(),
	memberviewModel: MembershipViewModel = hiltViewModel(),
	mviewModel: MovementsViewModel = hiltViewModel()
) {
	val colorScheme = MaterialTheme.colorScheme
	val summary = viewModel.summary
	val errorMessage = viewModel.errorMessage
	val isLoadingAdmin = viewModel.isLoading
	val isLoadingMovements = mviewModel.isLoading
	val isLoadingMemberships = memberviewModel.isLoading
	
	val isAllDataLoaded = remember(isLoadingAdmin, isLoadingMovements, isLoadingMemberships) {
		!isLoadingAdmin && !isLoadingMovements && !isLoadingMemberships
	}
	
	val payments by remember { derivedStateOf { mviewModel.lastPayments } }
	
	LaunchedEffect(Unit) {
		memberviewModel.loadMemberships()
		memberviewModel.loadMembershipsSummary()
		viewModel.loadGymUserSummary()
	}
	
	if (errorMessage != null) {
		Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
			Text(
				text = "Error: $errorMessage",
				color = Color.Red,
				fontWeight = FontWeight.Bold
			)
		}
		return
	}
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = "Gym Control",
						color = colorScheme.onPrimary,
						fontWeight = FontWeight.Bold
					)
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = colorScheme.primary
				)
			)
		},
		floatingActionButton = {
			FloatingActionButton(
				shape = CircleShape,
				onClick = navRegister,
				containerColor = colorScheme.primary,
				contentColor = colorScheme.onPrimary
			) {
				Icon(
					modifier = Modifier.size(22.dp),
					imageVector = Icons.Default.Add,
					contentDescription = "Agregar"
				)
			}
		},
		bottomBar = {
			BottomNavigationBar(navController = navBottom)
		}
	) { innerPadding ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
		) {
			if (!isAllDataLoaded) {
				CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
			} else {
				Column(
					modifier = Modifier
						.fillMaxSize()
						.verticalScroll(rememberScrollState())
				) {
					
					NewClientsSection()
					
					SummaryAndMembershipCard(
						summary = summary,
						memberships = memberviewModel.membershipsSummary,
						isLoading = isLoadingAdmin
					)
					
					
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.padding(8.dp)
					) {
						Text(
							text = "Últimos Movimientos",
							style = MaterialTheme.typography.headlineSmall.copy(
								fontWeight = FontWeight.Bold
							),
							modifier = Modifier.padding(bottom = 16.dp)
						)
						PaymentsList(payments = payments)
					}
				}
			}
		}
	}
}


@Composable
fun NewClientAvatar(name: String, photoUrl: String? = null) {
	Column(
		modifier = Modifier
			.width(72.dp)
			.padding(horizontal = 4.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Box(
			modifier = Modifier
				.size(60.dp)
				.clip(CircleShape)
				.background(MaterialTheme.colorScheme.primary),
			contentAlignment = Alignment.Center
		) {
			if (photoUrl != null) {
				// Aquí podrías cargar la imagen con Coil o Glide
				// AsyncImage(model = photoUrl, contentDescription = "Foto de $name", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
				Text(
					text = "Img", // Placeholder para imagen
					color = Color.White
				)
			} else {
				Text(
					text = name.firstOrNull()?.uppercase() ?: "",
					style = MaterialTheme.typography.titleMedium.copy(color = Color.White)
				)
			}
		}
		
		Spacer(modifier = Modifier.height(4.dp))
		
		Text(
			text = name,
			style = MaterialTheme.typography.labelMedium,
			maxLines = 1,
			overflow = TextOverflow.Ellipsis
		)
	}
}

@Composable
fun NewClientsSection() {
	val sampleClients = listOf(
		"Jesús Rivero",
		"Ana Gómez",
		"Carlos Pérez",
		"María López",
		"Juan Martínez",
		"Laura Sánchez",
		"Pedro Gómez"
	)
	Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp)) {
		Text(
			text = "Clientes Nuevos",
			style = MaterialTheme.typography.titleMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
		)
		
		Spacer(modifier = Modifier.height(8.dp))
		
		LazyRow {
			items(sampleClients) { clientName ->
				NewClientAvatar(name = clientName)
			}
			}
	Spacer(modifier = Modifier.height(16.dp))}
}
@Composable
fun SummaryAndMembershipCard(
	summary: GymUserSummary,
	memberships: List<MembershipWithCount>,
	isLoading: Boolean
) {
	var isExpanded by remember { mutableStateOf(false) }
	val cardColor = MaterialTheme.colorScheme.background
	
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp),
		colors = CardDefaults.cardColors(containerColor = cardColor),
		elevation = CardDefaults.cardElevation(6.dp),
		shape = RoundedCornerShape(16.dp)
	) {
		Column(modifier = Modifier.padding(16.dp)) {
			
			// Título
			Text(
				text = "Resumen General",
				style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
				color = MaterialTheme.colorScheme.onSurface
			)
			
			Spacer(modifier = Modifier.height(16.dp))
			
			// Fila de resumen: total, activos, inactivos
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				SummaryItem("Todos", summary.total, isLoading)
				SummaryItem("Activos", summary.activos, isLoading)
				SummaryItem("Inactivos", summary.inactivos, isLoading)
			}
			
			Spacer(modifier = Modifier.height(16.dp))
			
			// Botón expandir
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.clickable { isExpanded = !isExpanded },
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					text = "Membresías",
					style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
					color = MaterialTheme.colorScheme.primary
				)
				Icon(
					imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
					contentDescription = "Expandir",
					tint = MaterialTheme.colorScheme.primary
				)
			}
			
			// Contenido expandible
			AnimatedVisibility(visible = isExpanded) {
				Column(modifier = Modifier.padding(top = 8.dp)) {
					memberships.forEach { item ->
						MembershipItem(item)
					}
				}
			}
		}
	}
}

@Composable
fun SummaryItem(title: String, count: Int, isLoading: Boolean) {
	Column(horizontalAlignment = Alignment.CenterHorizontally) {
		Text(
			text = title,
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurface
		)
		if (isLoading) {
			CircularProgressIndicator(
				modifier = Modifier.size(18.dp),
				strokeWidth = 2.dp
			)
		} else {
			Text(
				text = count.toString(),
				style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
				color = MaterialTheme.colorScheme.primary
			)
		}
	}
}


@Composable
fun MembershipItem(item: MembershipWithCount) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 6.dp),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(
			text = item.membership.nombre,
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onSurface
		)
		Row(verticalAlignment = Alignment.CenterVertically) {
			Icon(
				imageVector = Icons.Default.Person,
				contentDescription = null,
				tint = MaterialTheme.colorScheme.primary,
				modifier = Modifier.size(18.dp)
			)
			Spacer(modifier = Modifier.width(4.dp))
			Text(
				text = "${item.userCount}",
				style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
				color = MaterialTheme.colorScheme.primary
			)
		}
	}
}



@Composable
fun PaymentsList(payments: List<Payment>) {
	if (payments.isEmpty()) {
		Text(
			text = "Sin movimientos recientes",
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
	} else {
		payments.forEach { payment ->
			MovementsCard(color = Color(0xFF4CAF50)) {
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					Column(modifier = Modifier
						.weight(1f)
						.padding(horizontal = 6.dp)) {
						Row(verticalAlignment = Alignment.CenterVertically) {
							Text(
								text = "$",
								style = MaterialTheme.typography.bodyLarge.copy(
									fontWeight = FontWeight.Bold,
									color = MaterialTheme.colorScheme.primary
								),
								modifier = Modifier.padding(end = 8.dp)
							)
							Text(
								text = "Pago de ${payment.name}",
								style = MaterialTheme.typography.bodyLarge.copy(
									fontWeight = FontWeight.SemiBold
								)
							)
						}
						
						Text(
							text = formatDate(payment.date),
							style = MaterialTheme.typography.bodySmall,
							color = MaterialTheme.colorScheme.onSurfaceVariant,
							modifier = Modifier.padding(top = 4.dp)
						)
					}
					
					Text(
						text = "${payment.amount}$",
						style = MaterialTheme.typography.bodyLarge.copy(
							fontWeight = FontWeight.Bold,
							color = MaterialTheme.colorScheme.primary
						)
					)
				}
			}
			Spacer(modifier = Modifier.height(6.dp))
		}
	}
}


@Composable
fun MovementsCard(
	color: Color,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit,
) {
	Column(modifier = Modifier.padding(horizontal = 4.dp)) {
		Card(
			modifier = modifier.fillMaxWidth(),
			colors = CardDefaults.cardColors(
				containerColor = MaterialTheme.colorScheme.background,
			),
			elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
		) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
			
			)
			
			content()
		}
	}
}

@Composable
fun formatDate(timestamp: Long): String {
	val sdf = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
	return sdf.format(Date(timestamp))
}

