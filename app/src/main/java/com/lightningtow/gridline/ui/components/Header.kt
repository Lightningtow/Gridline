package com.lightningtow.gridline.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.lightningtow.gridline.ui.theme.GridlineTheme

@Preview
//@PreviewParameter
@Composable
fun GridlineHeader(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable() (RowScope.() -> Unit)
) {

    // the double row keeps things from getting messed up if people pass padding as a Modifier arg
    Row(
        modifier = Modifier // new Modifier
            .fillMaxWidth()
            .background(color = GridlineTheme.colors.uiFloated),
    ) {
        Row(
            modifier = modifier, // uses modifier passed as arg
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            content = content
        )
    }
}