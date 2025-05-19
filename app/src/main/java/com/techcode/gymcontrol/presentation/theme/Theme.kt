package com.techcode.gymcontrol.presentation.theme

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
	primary = Color(0xFF006A60),
	onPrimary = Color(0xFFFFFFFF),
	primaryContainer = Color(0xFF80F2DD),
	onPrimaryContainer = Color(0xFF00201B),
	secondary = Color(0xFF4A635F),
	onSecondary = Color(0xFFFFFFFF),
	secondaryContainer = Color(0xFFCCE8E1),
	onSecondaryContainer = Color(0xFF05201B),
	tertiary = Color(0xFF3F6375),
	onTertiary = Color(0xFFFFFFFF),
	tertiaryContainer = Color(0xFFC3E8FF),
	onTertiaryContainer = Color(0xFF001E2C),
	error = Color(0xFFBA1A1A),
	onError = Color(0xFFFFFFFF),
	errorContainer = Color(0xFFFFDAD6),
	onErrorContainer = Color(0xFF410002),
	background = Color(0xFFFFFFFF),
	onBackground = Color(0xFF191C1B),
	surface = Color(0xFFFAFDFA),
	onSurface = Color(0xFF191C1B),
	surfaceVariant = Color(0xFFDBE5E1),
	onSurfaceVariant = Color(0xFF3F4946),
	outline = Color(0xFF6F7976)
)


private val DarkColorScheme = darkColorScheme(
	primary = Color(0xFF80F2DD),
	onPrimary = Color(0xFF00382F),
	primaryContainer = Color(0xFF005046),
	onPrimaryContainer = Color(0xFF80F2DD),
	secondary = Color(0xFFB0CCC5),
	onSecondary = Color(0xFF1C3530),
	secondaryContainer = Color(0xFF334B46),
	onSecondaryContainer = Color(0xFFCCE8E1),
	tertiary = Color(0xFFA7CCE8),
	onTertiary = Color(0xFF0A3447),
	tertiaryContainer = Color(0xFF264B60),
	onTertiaryContainer = Color(0xFFC3E8FF),
	error = Color(0xFFFFB4AB),
	onError = Color(0xFF690005),
	errorContainer = Color(0xFF93000A),
	onErrorContainer = Color(0xFFFFDAD6),
	background = Color(0xFF191C1B),
	onBackground = Color(0xFFE0E3E1),
	surface = Color(0xFF191C1B),
	onSurface = Color(0xFFE0E3E1),
	surfaceVariant = Color(0xFF3F4946),
	onSurfaceVariant = Color(0xFFBFC9C5),
	outline = Color(0xFF89938F)
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