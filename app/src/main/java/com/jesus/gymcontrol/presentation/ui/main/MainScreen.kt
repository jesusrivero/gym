package com.jesus.gymcontrol.presentation.ui.main


import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
	val scrollState = rememberScrollState()
	val isLoadingAdmin = viewModel.isLoading
	val isLoadingMembership = memberviewModel.isLoading
	val isLoadingMovements = mviewModel.isLoading
	val isAllDataLoaded = remember(isLoadingAdmin, isLoadingMovements) {
		!isLoadingAdmin &&  !isLoadingMovements
	}
	
	val payments  by remember { derivedStateOf { mviewModel.lastPayments } }
	
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
					Card(
						modifier = Modifier
							.fillMaxWidth()
							.padding(14.dp),
						colors = CardDefaults.cardColors(
							containerColor = Color(0xE0447A9C),
						),
						elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
					) {
						Column(
							modifier = Modifier
								.padding(24.dp)
								.fillMaxWidth(),
							verticalArrangement = Arrangement.Center,
							horizontalAlignment = Alignment.Start
						) {
							Text(
								text = "Bienvenido, Jesus",
								style = MaterialTheme.typography.headlineMedium.copy(
									fontWeight = FontWeight.Bold,
									color = Color.White
								)
							)
						}
					}
					
					Text(
						text = "Resumen General",
						style = MaterialTheme.typography.headlineSmall,
						modifier = Modifier.padding(start = 10.dp),
						color = colorScheme.onBackground
					)
					
					Row(
						modifier = Modifier
							.horizontalScroll(scrollState)
							.padding(horizontal = 16.dp, vertical = 8.dp),
						horizontalArrangement = Arrangement.spacedBy(16.dp)
					) {
						SummaryCard(color = Color(0xC92196F3)) {
							SummaryCardContent(title = "Todos", count = summary.total, isLoading = isLoadingAdmin)
						}
						SummaryCard(color = Color(0xCD4CAF50)) {
							SummaryCardContent(title = "Activos", count = summary.activos, isLoading = isLoadingAdmin)
						}
						SummaryCard(color = Color(0xB4E33E3E)) {
							SummaryCardContent(title = "Inactivos", count = summary.inactivos, isLoading = isLoadingAdmin)
						}
					}
					
					Text(
						text = "Resumen de Membresias",
						style = MaterialTheme.typography.headlineSmall,
						modifier = Modifier.padding(start = 10.dp),
						color = colorScheme.onBackground
					)
					
					MembersCardScreen()
					
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
fun SummaryCardContent(title: String, count: Int, isLoading: Boolean) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Text(
			text = title,
			color = Color.White,
			fontSize = 16.sp,
			fontWeight = FontWeight.Bold
		)
		
		Row(verticalAlignment = Alignment.CenterVertically) {
			Icon(
				imageVector = Icons.Default.Person,
				contentDescription = title,
				tint = Color.White,
				modifier = Modifier.size(20.dp)
			)
			Spacer(modifier = Modifier.width(4.dp))
			
			if (isLoading) {
				CircularProgressIndicator(
					color = Color.White,
					strokeWidth = 2.dp,
					modifier = Modifier.size(16.dp)
				)
			} else {
				Text(
					text = count.toString(),
					color = Color.White,
					fontSize = 16.sp
				)
			}
		}
	}
}

@Composable
fun SummaryCard(
	color: Color,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit,
) {
	Card(
		modifier = modifier
			.width(150.dp)
			.height(100.dp),
		colors = CardDefaults.cardColors(containerColor = color),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
		shape = RoundedCornerShape(12.dp)
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp),
			contentAlignment = Alignment.Center
		) {
			content()
		}
	}
}


@Composable
fun MembersCardScreen(
	viewModel: MembershipViewModel = hiltViewModel(),
) {
	val scrollState = rememberScrollState()
	val memberships = viewModel.memberships
	val isLoading = viewModel.isLoading
	val membershipsSummary = viewModel.membershipsSummary
	
	// Cargar membresías al abrir
	LaunchedEffect(Unit) {
		viewModel.loadMemberships()
		viewModel.loadMembershipsSummary()
	}
	
	Row(
		modifier = Modifier
			.horizontalScroll(scrollState)
			.padding(horizontal = 16.dp, vertical = 8.dp),
		horizontalArrangement = Arrangement.spacedBy(16.dp)
	) {
		if (isLoading && memberships.isEmpty()) {
			repeat(3) {
				LoadingMemberCard()
			}
		} else {
			membershipsSummary.forEach { membershipWithCount ->
				val membership = membershipWithCount.membership
				val userCount = membershipWithCount.userCount
				
				MembersCard(color = Color(0xCE447A9C)) {
					Column(
						horizontalAlignment = Alignment.CenterHorizontally,
						verticalArrangement = Arrangement.Center
					) {
						Text(
							text = membership.nombre,
							color = Color.White,
							fontSize = 16.sp,
							fontWeight = FontWeight.Bold
						)
						Row(verticalAlignment = Alignment.CenterVertically) {
							Icon(
								imageVector = Icons.Default.Person,
								contentDescription = "Clientes",
								tint = Color.White,
								modifier = Modifier.size(20.dp)
							)
							Spacer(modifier = Modifier.width(4.dp))
							Text(
								text = "$userCount",
								color = Color.White,
								fontSize = 16.sp
							)
						}
					}
				}
			}
		}
	}
}

@Composable
fun MembersCard(
	color: Color,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit,
) {
	Card(
		modifier = modifier
			.width(150.dp)
			.height(100.dp),
		colors = CardDefaults.cardColors(containerColor = color),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp),
			contentAlignment = Alignment.Center
		) {
			content()
		}
	}
}

@Composable
fun LoadingMemberCard() {
	MembersCard(color = Color(0xCE447A9C)) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			CircularProgressIndicator(
				color = Color.White,
				strokeWidth = 2.dp,
				modifier = Modifier.size(24.dp)
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
				containerColor = MaterialTheme.colorScheme.surfaceVariant,
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

