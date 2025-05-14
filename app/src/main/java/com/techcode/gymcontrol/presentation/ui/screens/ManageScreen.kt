package com.techcode.gymcontrol.presentation.ui.screens

import android.R
import android.R.attr.text
import android.graphics.drawable.Icon
import android.icu.text.CaseMap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.techcode.gymcontrol.presentation.ui.commons.BottomNavigationBar

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ManageScreen(navController: NavController) {
//	ManagerContent(

//		navBottom = navController,
//		navRegister = { navController.navigate(AppRoutes.RegPersonScreen) },

//		)
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageScreen(
    navBottom: NavController,

    ) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Panel de Administracion",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xBAA7D3DC)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navBottom,
            )
        }
    )
    { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                SettingsSectionTitle("General")
                Spacer(modifier = Modifier.padding(top = 10.dp))
                SettingsItem("Informacion de la cuenta", Icons.Default.AccountCircle)
                Spacer(modifier = Modifier.padding(top = 8.dp))

                HorizontalDivider(thickness = 1.dp)

                Spacer(modifier = Modifier.padding(top = 10.dp))

                SettingsItem("Membresias", Icons.Default.Menu)

                Spacer(modifier = Modifier.padding(top = 10.dp))

                HorizontalDivider(thickness = 1.dp)

                Spacer(modifier = Modifier.padding(top = 10.dp))

                SettingsItem("Reportes", Icons.Default.Settings)

                Spacer(modifier = Modifier.padding(top = 10.dp))

                HorizontalDivider(thickness = 1.dp)
                Spacer(modifier = Modifier.padding(top = 10.dp))

                SettingsItem("Bitaroca de acciones", Icons.Default.Build)
                Spacer(modifier = Modifier.padding(top = 10.dp))

                HorizontalDivider(thickness = 1.dp)

                Spacer(modifier = Modifier.padding(top = 15.dp))

                SettingsSectionTitle("General")
                Spacer(modifier = Modifier.padding(top = 8.dp))

                SettingsItem("Reportar errores", Icons.Default.Email)
                Spacer(modifier = Modifier.padding(top = 10.dp))

                HorizontalDivider(thickness = 1.dp)

                Spacer(modifier = Modifier.padding(top = 10.dp))

                SettingsItem("Sobre nosotros", Icons.Default.Info)

                Spacer(modifier = Modifier.padding(top = 10.dp))

                HorizontalDivider(thickness = 1.dp)


            }


        }
    }


}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        ),
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 8.dp)

    )

}

@Composable
fun SettingsItem(text: String, icon: ImageVector) {
    MaterialTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.padding(start = 6.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

        }

    }


}


/*@Preview(showBackground = true)
@Composable
fun ManagerScreenPreview() {
	ManageScreen(
		navBottom = NavController(LocalContext.current),
		
	)
}*/