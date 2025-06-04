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

private val LightColorScheme = lightColorScheme(
	primary = Color(0xFF1E88E5), // Azul sobrio y moderno
	onPrimary = Color.White,
	primaryContainer = Color(0xFFD6E4FF),
	onPrimaryContainer = Color(0xFF001E3C),

	secondary = Color(0xFF6D6D6D),
	onSecondary = Color.White,
	secondaryContainer = Color(0xFFE0E0E0),
	onSecondaryContainer = Color(0xFF1A1A1A),

	tertiary = Color(0xFF546E7A),
	onTertiary = Color.White,
	tertiaryContainer = Color(0xFFD0DAE0),
	onTertiaryContainer = Color(0xFF0A1F29),

	error = Color(0xFFD32F2F),
	onError = Color.White,
	errorContainer = Color(0xFFFFDAD4),
	onErrorContainer = Color(0xFF410002),

	background = Color(0xFFF9F9F9),
	onBackground = Color(0xFF121212),

	surface = Color.White,
	onSurface = Color(0xFF121212),

	surfaceVariant = Color(0xFFE0E0E0),
	onSurfaceVariant = Color(0xFF3E3E3E),

	outline = Color(0xFFB0B0B0)
)

private val DarkColorScheme = darkColorScheme(
	primary = Color(0xFF90CAF9), // Azul claro elegante
	onPrimary = Color(0xFF00315D),
	primaryContainer = Color(0xFF1565C0),
	onPrimaryContainer = Color(0xFFD6E4FF),

	secondary = Color(0xFFB0BEC5),
	onSecondary = Color(0xFF263238),
	secondaryContainer = Color(0xFF37474F),
	onSecondaryContainer = Color(0xFFCFD8DC),

	tertiary = Color(0xFFB0C4D0),
	onTertiary = Color(0xFF102631),
	tertiaryContainer = Color(0xFF263238),
	onTertiaryContainer = Color(0xFFD0DAE0),

	error = Color(0xFFEF5350),
	onError = Color(0xFF370001),
	errorContainer = Color(0xFF8C1D18),
	onErrorContainer = Color(0xFFFFDAD4),

	background = Color(0xFF121212),
	onBackground = Color(0xFFECECEC),

	surface = Color(0xFF1E1E1E),
	onSurface = Color(0xFFECECEC),

	surfaceVariant = Color(0xFF2C2C2C),
	onSurfaceVariant = Color(0xFFB0B0B0),

	outline = Color(0xFF757575)
)

@Composable
fun GymTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	// Dynamic color is available on Android 12+
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