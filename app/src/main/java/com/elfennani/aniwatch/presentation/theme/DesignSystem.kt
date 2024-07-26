package com.elfennani.aniwatch.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp

data class AppColorScheme(
    val primary: Color,
    val primaryAlt: Color,
    val secondary: Color,
    val background: Color,
    val onPrimary: Color,
    val onSecondary: Color,
    val onBackground: Color,
    val card: Color,
    val onCard: Color,
    val primaryContainer:Color,
    val onPrimaryContainer:Color,
)

data class AppTypography(
    val titleLarge: TextStyle,
    val titleNormal: TextStyle,
    val body: TextStyle,
    val labelLarge: TextStyle,
    val labelNormal: TextStyle,
    val labelSmall: TextStyle,
    val labelSmallBold: TextStyle = labelSmall.copy(fontWeight = FontWeight.SemiBold),
)

data class AppShape(
    val card: Shape,
    val button: Shape,
    val input: Shape,
    val thumbnail: Shape
)

data class AppSize(
    val large: Dp,
    val medium: Dp,
    val normal: Dp,
    val small: Dp,
    val smaller: Dp,
)

val LocalAppColorScheme = staticCompositionLocalOf {
    AppColorScheme(
        primary = Color.Unspecified,
        secondary = Color.Unspecified,
        background = Color.Unspecified,
        onPrimary = Color.Unspecified,
        onSecondary = Color.Unspecified,
        onBackground = Color.Unspecified,
        card = Color.Unspecified,
        onCard = Color.Unspecified,
        primaryAlt = Color.Unspecified,
        primaryContainer = Color.Unspecified,
        onPrimaryContainer = Color.Unspecified,
    )
}

val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(
        titleLarge = TextStyle.Default,
        titleNormal = TextStyle.Default,
        body = TextStyle.Default,
        labelLarge = TextStyle.Default,
        labelNormal = TextStyle.Default,
        labelSmall = TextStyle.Default,
    )
}

val LocalAppShape = staticCompositionLocalOf {
    AppShape(
        card = RectangleShape,
        button = RectangleShape,
        input = RectangleShape,
        thumbnail = RectangleShape
    )
}

val LocalAppSize = staticCompositionLocalOf {
    AppSize(
        large = Dp.Unspecified,
        medium = Dp.Unspecified,
        normal = Dp.Unspecified,
        small = Dp.Unspecified,
        smaller = Dp.Unspecified,
    )
}
