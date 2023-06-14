package com.lightningtow.gridline.ui.home

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import com.lightningtow.gridline.utils.toasty
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.lightningtow.gridline.ui.components.GridlineButton
import com.lightningtow.gridline.ui.components.KotlinShortcut
import com.lightningtow.gridline.ui.components.SHORTCUT_TYPE
import com.lightningtow.gridline.ui.components.downloadShortcutData
import com.lightningtow.gridline.ui.components.uploadShortcutData
//import com.lightningtow.gridline.ui.components.uploadStuff
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.ui.theme.gridline_pink
import com.lightningtow.gridline.utils.Constants
import com.skydoves.landscapist.glide.GlideImage


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