package com.lightningtow.gridline.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.material.contentColorFor
//import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.compositeOver
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lightningtow.gridline.ui.components.GridlineSurface


@Composable
fun GridlineTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    val systemUiController = rememberSystemUiController()
    SideEffect {
        if (darkTheme) {
            systemUiController.setSystemBarsColor(
                color = Color.Black
            )
        } else {
            systemUiController.setSystemBarsColor(
                color = colors.uiBackground.copy(alpha = AlphaNearOpaque)
            )
        }


    }

    ProvideGridlineColors(colors) {
        GridlineSurface() {

            MaterialTheme(
//            colors = colors,
                colors = debugColors(darkTheme), // todo fix colors here
//            typography = Typography,
//            shapes = Shapes,
                content = content
            )
        }
    }
}

@Composable
fun GridlineSliderColors(): SliderColors = SliderDefaults.colors(
    activeTickColor = GridlineTheme.colors.brand,
    inactiveTickColor = GridlineTheme.colors.brand,
    inactiveTrackColor = GridlineTheme.colors.uiBorder,
    activeTrackColor = GridlineTheme.colors.brand,
    thumbColor = GridlineTheme.colors.brand,
)

object GridlineTheme {
    val colors: GridlineColors
        @Composable
        get() = LocalGridlineColors.current
}

@Composable
fun ProvideGridlineColors(
    colors: GridlineColors,
    content: @Composable () -> Unit
) {


    val colorPalette = remember {
        // Explicitly creating a new object here so we don't mutate the initial [colors]
        // provided, and overwrite the values set in it.
        colors.copy()
    }
    colorPalette.update(colors)
    CompositionLocalProvider(LocalGridlineColors provides colorPalette, content = content)
}

private val LocalGridlineColors = staticCompositionLocalOf<GridlineColors> {
    error("No GridlineColorPalette provided") // means you need to wrap with GridlineTheme
}

private val DarkColorPalette = GridlineColors(
//    background = gridline_pink,

    brand = gridline_pink,
    brandSecondary = Ocean2,
    uiBackground = Neutral8,
//    uiBorder = Neutral3,
    uiBorder = Color.DarkGray, // divider colors

//    uiFloated = Neutral7,
    uiFloated = Color.Black, // this fixes whatever makes the taskbar green
//    uiFloated =
//    uiFloated = gridline_pink,
    textPrimary = dark_white,
    textSecondary = Color.LightGray,
    textHelp = Color.Red,
    textInteractive = Neutral7,
    textLink = Ocean2,
    iconPrimary = Neutral0,
    iconSecondary = Neutral0,
    iconInteractive = Neutral7,
    iconInteractiveInactive = Neutral6,
    error = FunctionalRedDark,
    gradient6_1 = listOf(Shadow5, Ocean7, Shadow9, Ocean7, Shadow5),
    gradient6_2 = listOf(Rose11, Lavender7, Rose8, Lavender7, Rose11),
    gradient3_1 = listOf(Shadow9, Ocean7, Shadow5),
    gradient3_2 = listOf(Rose8, Lavender7, Rose11),
//    gradient2_1 = listOf(Ocean3, Shadow3),
    gradient2_1 = listOf(Rose5, gridline_pink),

    gradient2_2 = listOf(Ocean4, Shadow2),
    gradient2_3 = listOf(Lavender3, Rose3),
    tornado1 = listOf(Shadow4, Ocean3),
    isDark = true
)

/**
 * A Material [Colors] implementation which sets all colors to [debugColor] to discourage usage of
 * [MaterialTheme.colors] in preference to [GridlineTheme.colors].
 */
fun debugColors(
    darkTheme: Boolean,
    whyAreYouFlashingMe: Color = Neutral8,
    debugColor: Color = Color.Green // todo with flash when changing screens
) = Colors(
    primary = debugColor,
    primaryVariant = debugColor,
    secondary = debugColor,
    secondaryVariant = debugColor,
    background = whyAreYouFlashingMe,
    surface = debugColor,
    error = debugColor,
    onPrimary = debugColor,
    onSecondary = debugColor,
    onBackground = debugColor,
    onSurface = debugColor,
    onError = debugColor,
    isLight = !darkTheme
)

private val LightColorPalette = GridlineColors(
//    textMain


    brand = Shadow5,
    brandSecondary = Ocean3,
    uiBackground = Neutral0,
    uiBorder = Neutral4,
    uiFloated = FunctionalGrey,
    textSecondary = Neutral7,
    textHelp = Color.Red,
    textInteractive = Neutral0,
    textLink = Ocean11,
    iconSecondary = Neutral7,
    iconInteractive = Neutral0,
    iconInteractiveInactive = Neutral1,
    error = FunctionalRed,
    gradient6_1 = listOf(Shadow4, Ocean3, Shadow2, Ocean3, Shadow4),
    gradient6_2 = listOf(Rose4, Lavender3, Rose2, Lavender3, Rose4),
    gradient3_1 = listOf(Shadow2, Ocean3, Shadow4),
    gradient3_2 = listOf(Rose2, Lavender3, Rose4),
    gradient2_1 = listOf(Shadow4, Shadow11),
    gradient2_2 = listOf(Ocean3, Shadow3),
    gradient2_3 = listOf(Lavender3, Rose2),
    tornado1 = listOf(Shadow4, Ocean3),
    isDark = false
)

@Stable
class GridlineColors(
    gradient6_1: List<Color>,
    gradient6_2: List<Color>,
    gradient3_1: List<Color>,
    gradient3_2: List<Color>,
    gradient2_1: List<Color>,
    gradient2_2: List<Color>,
    gradient2_3: List<Color>,
    brand: Color,
    brandSecondary: Color,
    uiBackground: Color,
    uiBorder: Color,
    uiFloated: Color,
//    interactivePrimary: List<Color> = gradient2_1,
    interactivePrimary: List<Color> = gradient2_1,

    interactiveSecondary: List<Color> = gradient2_2,
    interactiveMask: List<Color> = gradient6_1,
    textPrimary: Color = brand,
    textSecondary: Color,
    textHelp: Color,
    textInteractive: Color,
    textLink: Color,
    tornado1: List<Color>,
    iconPrimary: Color = brand,
    iconSecondary: Color,
    iconInteractive: Color,
    iconInteractiveInactive: Color,
    error: Color,
    notificationBadge: Color = error,
    isDark: Boolean
) {
    var gradient6_1 by mutableStateOf(gradient6_1)
        private set
    var gradient6_2 by mutableStateOf(gradient6_2)
        private set
    var gradient3_1 by mutableStateOf(gradient3_1)
        private set
    var gradient3_2 by mutableStateOf(gradient3_2)
        private set
    var gradient2_1 by mutableStateOf(gradient2_1)
        private set
    var gradient2_2 by mutableStateOf(gradient2_2)
        private set
    var gradient2_3 by mutableStateOf(gradient2_3)
        private set
    var brand by mutableStateOf(brand)
        private set
    var brandSecondary by mutableStateOf(brandSecondary)
        private set
    var uiBackground by mutableStateOf(uiBackground)
        private set
    var uiBorder by mutableStateOf(uiBorder)
        private set
    var uiFloated by mutableStateOf(uiFloated)
        private set
    var interactivePrimary by mutableStateOf(interactivePrimary)
        private set
    var interactiveSecondary by mutableStateOf(interactiveSecondary)
        private set
    var interactiveMask by mutableStateOf(interactiveMask)
        private set
    var textPrimary by mutableStateOf(textPrimary)
        private set
    var textSecondary by mutableStateOf(textSecondary)
        private set
    var textHelp by mutableStateOf(textHelp)
        private set
    var textInteractive by mutableStateOf(textInteractive)
        private set
    var tornado1 by mutableStateOf(tornado1)
        private set
    var textLink by mutableStateOf(textLink)
        private set
    var iconPrimary by mutableStateOf(iconPrimary)
        private set
    var iconSecondary by mutableStateOf(iconSecondary)
        private set
    var iconInteractive by mutableStateOf(iconInteractive)
        private set
    var iconInteractiveInactive by mutableStateOf(iconInteractiveInactive)
        private set
    var error by mutableStateOf(error)
        private set
    var notificationBadge by mutableStateOf(notificationBadge)
        private set
    var isDark by mutableStateOf(isDark)
        private set

    fun update(other: GridlineColors) {
        gradient6_1 = other.gradient6_1
        gradient6_2 = other.gradient6_2
        gradient3_1 = other.gradient3_1
        gradient3_2 = other.gradient3_2
        gradient2_1 = other.gradient2_1
        gradient2_2 = other.gradient2_2
        gradient2_3 = other.gradient2_3
        brand = other.brand
        brandSecondary = other.brandSecondary
        uiBackground = other.uiBackground
        uiBorder = other.uiBorder
        uiFloated = other.uiFloated
        interactivePrimary = other.interactivePrimary
        interactiveSecondary = other.interactiveSecondary
        interactiveMask = other.interactiveMask
        textPrimary = other.textPrimary
        textSecondary = other.textSecondary
        textHelp = other.textHelp
        textInteractive = other.textInteractive
        textLink = other.textLink
        tornado1 = other.tornado1
        iconPrimary = other.iconPrimary
        iconSecondary = other.iconSecondary
        iconInteractive = other.iconInteractive
        iconInteractiveInactive = other.iconInteractiveInactive
        error = other.error
        notificationBadge = other.notificationBadge
        isDark = other.isDark
    }

    fun copy(): GridlineColors = GridlineColors(
        gradient6_1 = gradient6_1,
        gradient6_2 = gradient6_2,
        gradient3_1 = gradient3_1,
        gradient3_2 = gradient3_2,
        gradient2_1 = gradient2_1,
        gradient2_2 = gradient2_2,
        gradient2_3 = gradient2_3,
        brand = brand,
        brandSecondary = brandSecondary,
        uiBackground = uiBackground,
        uiBorder = uiBorder,
        uiFloated = uiFloated,
        interactivePrimary = interactivePrimary,
        interactiveSecondary = interactiveSecondary,
        interactiveMask = interactiveMask,
        textPrimary = textPrimary,
        textSecondary = textSecondary,
        textHelp = textHelp,
        textInteractive = textInteractive,
        textLink = textLink,
        tornado1 = tornado1,
        iconPrimary = iconPrimary,
        iconSecondary = iconSecondary,
        iconInteractive = iconInteractive,
        iconInteractiveInactive = iconInteractiveInactive,
        error = error,
        notificationBadge = notificationBadge,
        isDark = isDark,
    )
}
