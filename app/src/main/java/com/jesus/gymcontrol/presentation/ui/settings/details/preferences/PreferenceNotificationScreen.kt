package com.jesus.gymcontrol.presentation.ui.settings.details.preferences

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.data.sharedPreferences.PreferencesManager
import com.jesus.gymcontrol.presentation.theme.GymTheme

@Composable
fun NotificationScreen(
    navController: NavController,
) {
    GymTheme {
        val preferencesManager = rememberPreferencesManager()
        NotificationScreenContent(
            navBottom = navController,
            preferencesManager = preferencesManager
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreenContent(
    navBottom: NavController,
    preferencesManager: PreferencesManager
) {
    var notificationSettings by remember {
        mutableStateOf(preferencesManager.getNotificationSettings())
    }

    fun updateSettings(
        newClient: Boolean = notificationSettings.newClientEnabled,
        paymentRegistered: Boolean = notificationSettings.paymentRegisteredEnabled,
        membershipExpiration: Boolean = notificationSettings.membershipExpirationEnabled,
        weekStart: Boolean = notificationSettings.weekStartEnabled,
        pushNotifications: Boolean = notificationSettings.pushNotificationsEnabled
    ) {
        notificationSettings = notificationSettings.copy(
            newClientEnabled = newClient,
            paymentRegisteredEnabled = paymentRegistered,
            membershipExpirationEnabled = membershipExpiration,
            weekStartEnabled = weekStart,
            pushNotificationsEnabled = pushNotifications
        )
        preferencesManager.saveNotificationSettings(
            newClient,
            paymentRegistered,
            membershipExpiration,
            weekStart,
            pushNotifications
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notificaciones",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navBottom.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = "Tipos de notificaciones",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            NotificationOptionItem(
                title = "Cliente nuevo",
                enabled = notificationSettings.newClientEnabled,
                onCheckedChange = { updateSettings(newClient = it) }
            )

            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

            NotificationOptionItem(
                title = "Pago registrado",
                enabled = notificationSettings.paymentRegisteredEnabled,
                onCheckedChange = { updateSettings(paymentRegistered = it) }
            )

            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

            NotificationOptionItem(
                title = "Vencimiento de membresía",
                enabled = notificationSettings.membershipExpirationEnabled,
                onCheckedChange = { updateSettings(membershipExpiration = it) }
            )

            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

            NotificationOptionItem(
                title = "Inicio de semana",
                enabled = notificationSettings.weekStartEnabled,
                onCheckedChange = { updateSettings(weekStart = it) }
            )

            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Canal de notificaciones",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            NotificationOptionItem(
                title = "Notificaciones push",
                enabled = notificationSettings.pushNotificationsEnabled,
                onCheckedChange = { updateSettings(pushNotifications = it) }
            )
        }
    }
}

@Composable
fun NotificationOptionItem(
    title: String,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = colorScheme.onSurface
        )

        Spacer(modifier = Modifier.width(8.dp))

        Switch(
            checked = enabled,
            onCheckedChange = onCheckedChange,
            thumbContent = if (enabled) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                        tint = colorScheme.primary
                    )
                }
            } else null,
            colors = SwitchDefaults.colors(
                checkedThumbColor = colorScheme.primary,
                uncheckedThumbColor = colorScheme.outline,
                checkedTrackColor = colorScheme.primary.copy(alpha = 0.54f),
                uncheckedTrackColor = colorScheme.surfaceVariant
            )
        )
    }
}


@Composable
fun rememberPreferencesManager(): PreferencesManager {
    val context = LocalContext.current
    return remember { PreferencesManager(context) }
}


// Data class para las configuraciones de notificación
//data class NotificationSettings(
//    val newClientEnabled: Boolean,
//    val paymentRegisteredEnabled: Boolean,
//    val membershipExpirationEnabled: Boolean,
//    val weekStartEnabled: Boolean,
//    val pushNotificationsEnabled: Boolean
//)