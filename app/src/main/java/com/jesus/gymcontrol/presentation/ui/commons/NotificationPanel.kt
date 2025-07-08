package com.jesus.gymcontrol.presentation.ui.commons


import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jesus.gymcontrol.domain.model.notification.Notificacion
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun NotificationPanel(
	navController: NavController,
	isVisible: Boolean,
	notifications: List<Notificacion>,
	onDismiss: () -> Unit,
	onDeleteAll: () -> Unit,
	onMarkAsRead: (Notificacion) -> Unit
) {
	val offsetY = remember { Animatable(0f) }
	val coroutineScope = rememberCoroutineScope()
	val screenHeightDp = LocalConfiguration.current.screenHeightDp
	val panelHeight = (screenHeightDp * 0.5f).dp
	val panelHeightPx = with(LocalDensity.current) { panelHeight.toPx() }
	
	var isOverlayVisible by remember { mutableStateOf(isVisible) }
	
	LaunchedEffect(isVisible) {
		if (isVisible) {
			isOverlayVisible = true
			coroutineScope.launch {
				offsetY.snapTo(panelHeightPx)
				offsetY.animateTo(0f)
			}
		}
	}
	
	if (isOverlayVisible) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Color.Black.copy(alpha = 0.4f))
				.pointerInput(Unit) {
					detectVerticalDragGestures { _, dragAmount ->
						if (dragAmount > 15) {
							coroutineScope.launch {
								offsetY.snapTo(panelHeightPx)
								isOverlayVisible = false
								onDismiss()
							}
						}
					}
				}
		) {
			Card(
				modifier = Modifier
					.align(Alignment.BottomCenter)
					.fillMaxWidth()
					.height(panelHeight)
					.offset { IntOffset(0, offsetY.value.toInt()) }
					.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
				colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
				elevation = CardDefaults.cardElevation(6.dp)
			) {
				Column(modifier = Modifier.fillMaxSize()) {
					Box(
						modifier = Modifier
							.padding(top = 12.dp)
							.size(width = 40.dp, height = 4.dp)
							.background(
								MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
								RoundedCornerShape(2.dp)
							)
							.align(Alignment.CenterHorizontally)
					)
					
					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 16.dp),
						horizontalArrangement = Arrangement.SpaceBetween,
						verticalAlignment = Alignment.CenterVertically
					) {
						Text(
							text = "Notificaciones",
							style = MaterialTheme.typography.titleMedium,
							fontWeight = FontWeight.Bold
						)
						
						TextButton(onClick = { navController.navigate(AppRoutes.NotificationsScreen) }) {
							Text("Ver más")
						}
					}
					
					Column(
						modifier = Modifier
							.fillMaxSize()
							.verticalScroll(rememberScrollState())
							.padding(horizontal = 16.dp, vertical = 4.dp)
					) {
						if (notifications.isEmpty()) {
							Text("No hay notificaciones", style = MaterialTheme.typography.bodyMedium)
						} else {
							notifications.forEach { notification ->
								val dismissState = rememberDismissState()
								
								LaunchedEffect(dismissState.currentValue) {
									if (dismissState.currentValue == DismissValue.DismissedToStart ||
										dismissState.currentValue == DismissValue.DismissedToEnd
									) {
										onMarkAsRead(notification)
									}
								}
								
								SwipeToDismiss(
									state = dismissState,
									directions = setOf(
										DismissDirection.StartToEnd,
										DismissDirection.EndToStart
									),
									background = {
										Box(
											modifier = Modifier
												.fillMaxSize()
												.background(Color.Transparent)
												.padding(8.dp),
											contentAlignment = Alignment.Center
										) {
											Text(
												"Marcar como leída",
												color = MaterialTheme.colorScheme.onPrimaryContainer
											)
										}
									},
									dismissContent = {
										Card(
											modifier = Modifier
												.fillMaxWidth()
												.padding(vertical = 4.dp),
											colors = CardDefaults.cardColors(
												containerColor = if (notification.leido == false) {
													MaterialTheme.colorScheme.surfaceVariant
												} else {
													MaterialTheme.colorScheme.surface
												}
											)
										) {
											Row(
												modifier = Modifier
													.fillMaxWidth()
													.padding(8.dp),
												verticalAlignment = Alignment.CenterVertically
											) {
												Icon(
													imageVector = Icons.Default.Notifications,
													contentDescription = null,
													tint = MaterialTheme.colorScheme.primary,
													modifier = Modifier.size(20.dp)
												)
												
												Spacer(modifier = Modifier.width(8.dp))
												
												Column(
													modifier = Modifier.weight(1f)
												) {
													Text(
														text = notification.titulo,
														style = MaterialTheme.typography.bodyMedium,
														fontWeight = FontWeight.SemiBold,
														maxLines = 1
													)
													Text(
														text = notification.mensaje,
														style = MaterialTheme.typography.bodySmall,
														maxLines = 1,
														color = MaterialTheme.colorScheme.onSurfaceVariant
													)
												}
												
												val date = remember(notification.fecha) {
													java.text.SimpleDateFormat(
														"dd/MM/yyyy HH:mm",
														java.util.Locale.getDefault()
													).format(java.util.Date(notification.fecha))
												}
												
												Text(
													text = date,
													style = MaterialTheme.typography.labelSmall,
													color = MaterialTheme.colorScheme.onSurfaceVariant
												)
											}
										}
									}
								)
							}
						}
						Spacer(modifier = Modifier.height(12.dp))
					}
				}
			}
		}
	}
}
