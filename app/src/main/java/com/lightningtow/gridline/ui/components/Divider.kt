package com.lightningtow.gridline.ui.components

import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lightningtow.gridline.ui.theme.GridlineTheme

@Composable
fun GridlineDivider(
    modifier: Modifier = Modifier,
    color: Color = GridlineTheme.colors.uiBorder,
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp
) {
    Divider(
        modifier = modifier,
        color = color,
        thickness = thickness,
        startIndent = startIndent
    )

}