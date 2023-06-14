package com.lightningtow.gridline.ui.home

import com.lightningtow.gridline.utils.toasty
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lightningtow.gridline.ui.components.GridlineButton
import com.lightningtow.gridline.ui.components.downloadShortcutData
import com.lightningtow.gridline.ui.components.uploadShortcutData
//import com.lightningtow.gridline.ui.components.uploadStuff
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.ui.theme.gridline_pink


@Composable
fun LandingScreen() {
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

            GridlineButton(
                onClick = {
                    msg = if (toggle) "owie" else "ouch"
                    toasty(context, msg)
                    toggle = !toggle

                }) {
                Text("send memes")

            }

            Spacer(modifier = Modifier.padding(10.dp))
            GridlineButton(
                onClick = {

//                    GetDiffByURI(
//                        baselist = Constants.TESTLIST2,
//                        removeTheseTracks = Constants.TESTLIST
//                    )
                    uploadShortcutData()

                    toasty(context, "(.)(.)")
                }) {
                Text("send nudes")
            }
            Spacer(modifier = Modifier.padding(10.dp))

            GridlineButton(
                onClick = {


                    downloadShortcutData()

                    toasty(context, "(_|_)")
                }) {
                Text("send more nudes")
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