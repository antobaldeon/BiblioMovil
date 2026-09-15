package pe.edu.upeu.bibliomobil.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Claro = lightColorScheme(
    primary = Color(0xFF8B3D16),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFDBC9),
    onPrimaryContainer = Color(0xFF341100),
    inversePrimary = Color(0xFFFFB694),
    secondary = Color(0xFF735A00),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFE17C),
    onSecondaryContainer = Color(0xFF241A00),
    tertiary = Color(0xFF2D6177),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFCDEBFF),
    onTertiaryContainer = Color(0xFF001E2A),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFF8F5),
    onBackground = Color(0xFF231A16),
    surface = Color(0xFFFFF8F5),
    onSurface = Color(0xFF231A16),
    surfaceVariant = Color(0xFFF4DED5),
    onSurfaceVariant = Color(0xFF53433D),
    surfaceTint = Color(0xFF8B3D16),
    inverseSurface = Color(0xFF392F2A),
    inverseOnSurface = Color(0xFFFFEDE6),
    outline = Color(0xFF85736B),
    outlineVariant = Color(0xFFD7C2B9),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFFFFF8F5),
    surfaceDim = Color(0xFFE4D8D2),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFEF2EC),
    surfaceContainer = Color(0xFFF8EBE5),
    surfaceContainerHigh = Color(0xFFF2E5DF),
    surfaceContainerHighest = Color(0xFFECE0DA)
)

private val Oscuro = darkColorScheme(
    primary = Color(0xFFFFB694),
    onPrimary = Color(0xFF542000),
    primaryContainer = Color(0xFF703000),
    onPrimaryContainer = Color(0xFFFFDBC9),
    inversePrimary = Color(0xFF8B3D16),
    secondary = Color(0xFFE8C34E),
    onSecondary = Color(0xFF3C2F00),
    secondaryContainer = Color(0xFF574500),
    onSecondaryContainer = Color(0xFFFFE17C),
    tertiary = Color(0xFFB1D4EC),
    onTertiary = Color(0xFF123443),
    tertiaryContainer = Color(0xFF294A5A),
    onTertiaryContainer = Color(0xFFCDEBFF),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF1A120E),
    onBackground = Color(0xFFF0E0D9),
    surface = Color(0xFF1A120E),
    onSurface = Color(0xFFF0E0D9),
    surfaceVariant = Color(0xFF53433D),
    onSurfaceVariant = Color(0xFFD7C2B9),
    surfaceTint = Color(0xFFFFB694),
    inverseSurface = Color(0xFFF0E0D9),
    inverseOnSurface = Color(0xFF392F2A),
    outline = Color(0xFFA08C84),
    outlineVariant = Color(0xFF53433D),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFF413732),
    surfaceDim = Color(0xFF1A120E),
    surfaceContainerLowest = Color(0xFF140D09),
    surfaceContainerLow = Color(0xFF231A16),
    surfaceContainer = Color(0xFF271E1A),
    surfaceContainerHigh = Color(0xFF322824),
    surfaceContainerHighest = Color(0xFF3D332E)
)

@Composable
fun BiblioMobilTheme(
    modoOscuro: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (modoOscuro) Oscuro else Claro,
        content = content
    )
}
