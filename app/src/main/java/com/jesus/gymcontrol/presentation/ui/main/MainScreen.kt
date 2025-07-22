package com.jesus.gymcontrol.presentation.ui.main


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jesus.gymcontrol.domain.model.GymUserSummary
import com.jesus.gymcontrol.domain.model.ListUser
import com.jesus.gymcontrol.domain.model.MembershipWithCount
import com.jesus.gymcontrol.domain.model.Payment
import com.jesus.gymcontrol.domain.viewmodels.AdminViewModel
import com.jesus.gymcontrol.domain.viewmodels.GymViewModel
import com.jesus.gymcontrol.domain.viewmodels.MembershipViewModel
import com.jesus.gymcontrol.domain.viewmodels.MovementsViewModel
import com.jesus.gymcontrol.domain.viewmodels.UserListViewModel
import com.jesus.gymcontrol.domain.viewmodels.notification.NotificacionesViewModel
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import com.jesus.gymcontrol.presentation.theme.GymTheme
import com.jesus.gymcontrol.presentation.ui.commons.BottomNavigationBar
import com.jesus.gymcontrol.presentation.ui.commons.NotificationPanel
import java.text.NumberFormat
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
			navController = navController,
			navRegister = { navController.navigate(AppRoutes.RegPersonScreen) }
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
	navBottom: NavController,
	navController: NavController,
	navRegister: () -> Unit,
	viewModel: AdminViewModel = hiltViewModel(),
	memberviewModel: MembershipViewModel = hiltViewModel(),
	mviewModel: MovementsViewModel = hiltViewModel(),
	userListViewModel: UserListViewModel = hiltViewModel(),
	gymViewModel: GymViewModel = hiltViewModel(),
	notificacionesViewModel: NotificacionesViewModel = hiltViewModel(),
) {
	val colorScheme = MaterialTheme.colorScheme
	val summary = viewModel.summary
	val errorMessage = viewModel.errorMessage
	val isLoadingAdmin = viewModel.isLoading
	val isLoadingMovements = mviewModel.isLoading
	val isLoadingMemberships = memberviewModel.isLoading
	val notificationCount by notificacionesViewModel.notificationCount.collectAsState()
	val isAllDataLoaded = remember(isLoadingAdmin, isLoadingMovements, isLoadingMemberships) {
		!isLoadingAdmin && !isLoadingMovements && !isLoadingMemberships
	}
	
	val unreadCount by notificacionesViewModel.unreadCount.collectAsState()
	val payments by remember { derivedStateOf { mviewModel.lastPayments } }
	
	var showNotifications by remember { mutableStateOf(false) }
	
	// 🚀 Notificaciones reales desde tu ViewModel
	val notifications by notificacionesViewModel.notifications.collectAsState()
	
	
	val userUid = FirebaseAuth.getInstance().currentUser?.uid
	LaunchedEffect(userUid) {
		if (userUid != null) {
			gymViewModel.loadCurrentUserGymCode(userUid)
			gymViewModel.loadCurrentUserRole(userUid)
		}
	}
	
	
	LaunchedEffect(Unit) {
		memberviewModel.loadMemberships()
		viewModel.loadGymUserSummary()
		userListViewModel.loadUsers()
		notificacionesViewModel.loadNotifications()
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
	
	Box(modifier = Modifier.fillMaxSize()) {
		Scaffold(
			topBar = {
				TopAppBar(
					title = {
						Text(
							text = "Bienvenido 💪🏻",
							color = colorScheme.onPrimary,
							fontWeight = FontWeight.Bold
						)
					},
					actions = {
						IconButton(onClick = { showNotifications = !showNotifications }) {
							Box {
								Icon(
									imageVector = Icons.Default.Notifications,
									contentDescription = "Notificaciones",
									tint = colorScheme.onPrimary
								)
								
								if (unreadCount > 0) {
									Box(
										modifier = Modifier
											.align(Alignment.TopEnd)
											.offset(x = 4.dp, y = (-4).dp)
											.size(16.dp)
											.background(Color.Red, shape = CircleShape),
										contentAlignment = Alignment.Center
									) {
										Text(
											text = unreadCount.toString(),
											color = Color.White,
											style = MaterialTheme.typography.labelSmall,
											fontSize = 10.sp
										)
									}
								}
							}
						}
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
						
						NewClientsSection(navController = navController)
						
						SummaryAndMembershipCard(
							navController = navController,
							summary = summary,
							memberships = memberviewModel.membershipsSummary,
							isLoading = isLoadingAdmin
						)
						
						Column(
							modifier = Modifier
								.fillMaxWidth()
								.padding(8.dp)
						) {
							Row(
								modifier = Modifier
									.fillMaxWidth()
									.padding(bottom = 16.dp),
								horizontalArrangement = Arrangement.SpaceBetween,
								verticalAlignment = Alignment.CenterVertically
							) {
								Row(modifier = Modifier.padding(start = 8.dp)) {
									Text(
										text = "Últimos Movimientos",
										style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
									)
								}
								
								TextButton(
									onClick = { navController.navigate(AppRoutes.ListPaymentsScreen) },
									contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
								) {
									Text(
										text = "Ver más",
										style = MaterialTheme.typography.labelMedium,
										color = MaterialTheme.colorScheme.primary
									)
								}
							}
							PaymentsList(payments = payments)
						}
					}
				}
			}
		}
	}
	
	NotificationPanel(
		navController = navController,
		isVisible = showNotifications,
		notifications = notifications,
		onDismiss = { showNotifications = false },
		onDeleteAll = { notificacionesViewModel.deleteAllNotifications() },
		onMarkAsRead = { notificacion -> notificacionesViewModel.markNotificationAsRead(notificacion) }
	)
}


@Composable
fun NewClientAvatar(name: String, photoUrl: String? = null, onClick: () -> Unit) {
	Column(
		modifier = Modifier
			.width(72.dp)
			.padding(horizontal = 4.dp)
			.clickable { onClick() }, // Aquí
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
				Text("Img", color = Color.White)
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
fun NewClientsSection(
	viewModel: UserListViewModel = hiltViewModel(),
	navController: NavController,
) {
	var selectedUser by remember { mutableStateOf<ListUser?>(null) }
	
	val users = viewModel.listUsers.take(8)
	
	Column(modifier = Modifier.padding(start = 10.dp, top = 8.dp)) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 8.dp),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = "Clientes Nuevos",
				style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
			)
			TextButton(
				onClick = { navController.navigate(AppRoutes.PersonasScreen.route) },
				contentPadding = PaddingValues(horizontal = 8.dp)
			) {
				Text(
					text = "Ver más",
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.primary
				)
			}
		}
		
		Spacer(modifier = Modifier.height(8.dp))
		
		if (users.isEmpty()) {
			EmptyState(
				mensaje = "Aún no tienes clientes registrados",
				onAccion = { navController.navigate(AppRoutes.RegPersonScreen) }
			)
		} else {
			LazyRow {
				items(users) { user ->
					NewClientAvatar(
						name = "${user.name} ${user.lastname}".trim(),
						onClick = { selectedUser = user }
					)
				}
			}
		}
		
		Spacer(modifier = Modifier.height(16.dp))
	}
	
	// Mostrar AlertDialog si hay un usuario seleccionado
	selectedUser?.let { user ->
		AlertDialog(
			onDismissRequest = { selectedUser = null },
			confirmButton = {
				TextButton(onClick = { selectedUser = null }) {
					Text("Cerrar")
				}
			},
			title = { Text("Información del cliente") },
			text = {
				Column {
					Text("Nombre: ${user.name}")
					Text("Apellido: ${user.lastname}")
					Text("Cédula: ${user.idcard}")
					Text("Teléfono: ${user.phone}")
					Text("Correo: ${user.email}")
					Text("Estado: ${user.state}")
					Text("Rol: ${user.rol}")
				}
			}, containerColor = Color.White
		)
	}
}


@Composable
fun PaymentsList(payments: List<Payment>) {
	if (payments.isEmpty()) {
		EmptyState(
			mensaje = "Aún no hay pagos recientes registrados",
			onAccion = { }
		)
	} else {
		payments.forEach { payment ->
			// Determinar el texto del monto según el tipo de pago
			val formattedAmount = when (payment.paymentType.lowercase()) {
				"bolívares" -> "Bs${formatBolivares(payment.amountBs)}"
				"dólares" -> "$${payment.amountDollar}"
				"mixto" -> "$${payment.amountDollar} - Bs${formatBolivares(payment.amountBs)}"
				else -> "${payment.amount}"
			}
			
			MovementsCard(color = Color(0xFF4CAF50)) {
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					Column(
						modifier = Modifier
							.weight(1f)
							.padding(horizontal = 6.dp)
					) {
						Row(verticalAlignment = Alignment.CenterVertically) {
							Text(
								text = "${payment.name} ${payment.lastname}",
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
					
					Box(
						modifier = Modifier
							.widthIn(max = 140.dp), // Limita el ancho del texto
						contentAlignment = Alignment.CenterEnd
					) {
						Text(
							text = formattedAmount,
							maxLines = 1,
							overflow = TextOverflow.Ellipsis,
							style = MaterialTheme.typography.bodyLarge.copy(
								fontWeight = FontWeight.Bold,
								color = MaterialTheme.colorScheme.primary
							)
						)
					}
				}
			}
			Spacer(modifier = Modifier.height(6.dp))
		}
	}
}

// 📌 NUEVO COMPOSABLE: EMPTY STATE
@Composable
fun EmptyState(
	mensaje: String,
	onAccion: () -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = mensaje,
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = androidx.compose.ui.text.style.TextAlign.Center
		)
		Spacer(modifier = Modifier.height(16.dp))
	}
}


@Composable
fun SummaryAndMembershipCard(
	navController: NavController,
	summary: GymUserSummary,
	memberships: List<MembershipWithCount>,
	isLoading: Boolean,
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
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(
					text = "Resumen de General",
					style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
				)
				TextButton(
					onClick = { navController.navigate(AppRoutes.MembershipScreen) },
				) {
					Text(
						text = "Ver más",
						style = MaterialTheme.typography.labelMedium,
						color = MaterialTheme.colorScheme.primary
					)
				}
			}
			
			Spacer(modifier = Modifier.height(16.dp))
			
		
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
					memberships
						.filter { it.membership.activo }
						.forEach { item ->
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
fun formatBolivares(amount: Double): String {
	val format = NumberFormat.getInstance(Locale("es", "VE"))
	format.minimumFractionDigits = 2
	format.maximumFractionDigits = 2
	return format.format(amount)
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

