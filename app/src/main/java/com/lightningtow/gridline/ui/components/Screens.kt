package com.lightningtow.gridline.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.ui.theme.gridline_pink
import com.lightningtow.gridline.utils.Constants
import kotlinx.coroutines.Dispatchers


@Composable
fun SearchScreen() {
    GridlineTheme() {

        // Column Composable,
        Column(
            modifier = Modifier
                .fillMaxSize(),
//                .background(Color.DarkGray),
            // parameters set to place the items in center
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon Composable
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "search",
                tint = Color(0xFF0F9D58)
            )
            // Text to Display the current Screen
            Text(text = "Search", color = Color.Black)


            GridlineButton(
                onClick = {
                    Log.e("coroutine?", Dispatchers.toString())

//                    PurgeActivity

//                helloworld(msg = "hello fuckin world")
                /*TODO*/
            }) {
                Text("poke me")

            }
        }
    }
}

@Composable
fun helloworld(msg: String) {
    Box() {
        Text(text = msg, color = gridline_pink)

    }
}