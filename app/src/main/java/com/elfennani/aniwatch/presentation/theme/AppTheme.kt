package com.elfennani.aniwatch.presentation.theme


import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val darkColorScheme = AppColorScheme(
    primary = Blue40,
    primaryAlt = Zinc70,
    onPrimary = Blue05,
    secondary = Zinc60,
    onSecondary = Zinc30,
    background = Color.Black,
    onBackground = Zinc05,
    card = Zinc90,
    onCard = Zinc05,
    primaryContainer = Zinc90,
    onPrimaryContainer = Zinc20
)

private val lightColorScheme = AppColorScheme(
    primary = Blue50,
    primaryAlt = Blue20,
    onPrimary = Blue05,
    secondary = Slate40,
    onSecondary = Slate70,
    background = Slate05,
    onBackground = Slate95,
    card = Color.White,
    onCard = Slate95,
    primaryContainer = Blue05,
    onPrimaryContainer = Blue95
)

private val typography = AppTypography(
    titleLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    titleNormal = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    body = TextStyle(
        fontFamily = Inter,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    labelNormal = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Inter,
        fontSize = 12.sp
    )
)

private val shape = AppShape(
    card = RoundedCornerShape(16.dp),
    button = RoundedCornerShape(6.dp),
    thumbnail=RoundedCornerShape(6.dp),
    input = RoundedCornerShape(10.dp)
)

private val size = AppSize(
    large = 24.dp,
    medium = 16.dp,
    normal = 12.dp,
    small = 8.dp,
    smaller = 4.dp
)

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkTheme) darkColorScheme else lightColorScheme
    val rippleIndication = rememberRipple()

    MaterialTheme {
        CompositionLocalProvider(
            LocalAppColorScheme provides colorScheme,
            LocalAppTypography provides typography,
            LocalAppShape provides shape,
            LocalAppSize provides size,
            LocalIndication provides rippleIndication,
            content = content
        )
    }

}

object AppTheme {

    val colorScheme: AppColorScheme
        @Composable get() = LocalAppColorScheme.current

    val typography: AppTypography
        @Composable get() = LocalAppTypography.current

    val shapes: AppShape
        @Composable get() = LocalAppShape.current

    val sizes: AppSize
        @Composable get() = LocalAppSize.current

}