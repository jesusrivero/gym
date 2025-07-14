package com.jesus.gymcontrol.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Light mode: colores deportivos, modernos y no tan "fríos"
private val LightColorScheme = lightColorScheme(
	primary = Color(0xFF1565C0), // Azul fuerte (acento)
	onPrimary = Color.White,
	primaryContainer = Color(0xFFBBDEFB),
	onPrimaryContainer = Color(0xFF003c8f),
	
	secondary = Color(0xFF00897B), // Verde-petróleo
	onSecondary = Color.White,
	secondaryContainer = Color(0xFFB2DFDB),
	onSecondaryContainer = Color(0xFF004D40),
	
	tertiary = Color(0xFFF9A825), // Amarillo deportivo
	onTertiary = Color.Black,
	tertiaryContainer = Color(0xFFFFF59D),
	onTertiaryContainer = Color(0xFF795548),
	
	error = Color(0xFFD32F2F),
	onError = Color.White,
	errorContainer = Color(0xFFFFCDD2),
	onErrorContainer = Color(0xFFB71C1C),
	
	// 👇 aquí el nuevo fondo profesional
	background = Color(0xFFEAEDF0), // Gris piedra suave, elegante
	onBackground = Color(0xFF121212),
	
	surface = Color(0xFFFFFFFF), // Blanco puro para superficies
	onSurface = Color(0xFF121212),
	
	surfaceVariant = Color(0xFFE0E0E0),
	onSurfaceVariant = Color(0xFF424242),
	
	outline = Color(0xFF9E9E9E)
)


// Dark mode: más neutro y menos saturado para no cansar la vista
private val DarkColorScheme = darkColorScheme(
	primary = Color(0xFF90CAF9),
	onPrimary = Color(0xFF002f6c),
	primaryContainer = Color(0xFF1565C0),
	onPrimaryContainer = Color(0xFFBBDEFB),
	
	secondary = Color(0xFF4DB6AC),
	onSecondary = Color(0xFF00332C),
	secondaryContainer = Color(0xFF00695C),
	onSecondaryContainer = Color(0xFFB2DFDB),
	
	tertiary = Color(0xFFFFD54F),
	onTertiary = Color.Black,
	tertiaryContainer = Color(0xFFFBC02D),
	onTertiaryContainer = Color(0xFFFFF9C4),
	
	error = Color(0xFFEF5350),
	onError = Color(0xFF370001),
	errorContainer = Color(0xFF8C1D18),
	onErrorContainer = Color(0xFFFFDAD4),
	
	background = Color(0xFF121212),
	onBackground = Color(0xFFECECEC),
	
	surface = Color(0xFF1E1E1E),
	onSurface = Color(0xFFECECEC),
	
	surfaceVariant = Color(0xFF2C2C2C),
	onSurfaceVariant = Color(0xFFBDBDBD),
	
	outline = Color(0xFF757575)
)

@Composable
fun GymTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	dynamicColor: Boolean = true,
	content: @Composable () -> Unit
) {
	val colorScheme = when {
		dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
			val context = LocalContext.current
			if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
		}
		darkTheme -> DarkColorScheme
		else -> LightColorScheme
	}
	
	MaterialTheme(
		colorScheme = colorScheme,
		typography = Typography,
		content = content
	)
}
