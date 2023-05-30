package com.lightningtow.gridline.ui.components

import com.lightningtow.gridline.utils.toasty
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.ui.theme.gridline_pink


@Composable
fun HomeScreen() {
    val context = LocalContext.current
    var toggle = true
    var msg = "default"
    GridlineTheme() {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon Composable
//            Icon(
//                imageVector = Icons.Default.Search,
//                contentDescription = "search",
//                tint = Color(0xFF0F9D58)
//            )
//            // Text to Display the current Screen
//            Text(text = "Search", color = Color.Black)


            GridlineButton(
                onClick = {
                    msg = if (toggle) "owie" else "oof"
                    toasty(context, msg)
                    toggle = !toggle

//                helloworld(msg = "hello fuckin world")
            }) {
                Text("poke me")

            }
        }
    }
}

@Composable
fun HelloWorld() {
    GridlineTheme() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
//                .background(Color.DarkGray),
            // parameters set to place the items in center
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            Text(text = "Hello fuckin world", color = gridline_pink)

        }
    }

}