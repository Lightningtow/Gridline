package com.lightningtow.gridline.ui.home

import android.util.Log
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
import com.adamratzman.spotify.models.Playable
import com.lightningtow.gridline.data.TrackHolder1
import com.lightningtow.gridline.data.TrackHolder2
import com.lightningtow.gridline.data.TrackHolder3
import com.lightningtow.gridline.grid.GetDiffByURI
import com.lightningtow.gridline.grid.PlaylistGetter
import com.lightningtow.gridline.grid.getcount
import com.lightningtow.gridline.grid.logging
import com.lightningtow.gridline.ui.components.GridlineButton
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.ui.theme.gridline_pink
import com.lightningtow.gridline.utils.Constants
import com.lightningtow.gridline.utils.Constants.TESTLIST
import com.lightningtow.gridline.utils.Constants.TESTLIST2



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
            GridlineButton(
                onClick = {
                    var returns: Int = 0

                    GetDiffByURI(
                        baselist = Constants.TESTLIST2,
                        removeTheseTracks = Constants.TESTLIST
                    )


                }) {
                Text("launch nukes")
            }

            Spacer(modifier = Modifier.padding(10.dp))

            GridlineButton(
                onClick = {
                    msg = if (toggle) "owie" else "oof"
                    toasty(context, msg)
                    toggle = !toggle

            }) {
                Text("poke me for lulz")

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