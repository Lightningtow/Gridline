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
import com.adamratzman.spotify.models.CurrentUserQueue
import com.lightningtow.gridline.data.API_State
import com.lightningtow.gridline.data.API_State.currentUserQueue
import com.lightningtow.gridline.grid.PlaylistGetter
import com.lightningtow.gridline.ui.components.GridlineButton
import com.lightningtow.gridline.ui.components.SHORTCUT_TYPE
import com.lightningtow.gridline.ui.components.downloadShortcutData
import com.lightningtow.gridline.ui.components.uploadShortcutData
//import com.lightningtow.gridline.ui.components.uploadStuff
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.ui.theme.gridline_pink
import com.lightningtow.gridline.utils.Constants
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


@Composable
fun LandingScreen() {
    val context = LocalContext.current
    var toggle = true
    var msg = "default"
    GridlineTheme() {
        AuthPageInner()

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
        ) {

            GridlineButton(onClick = {
                context.startActivity(Intent(context, Broadcasts::class.java))
            }) {
                Text("view broadcasts")
            }


            GridlineButton(
                onClick = {
                    msg = if (toggle) "owie" else "ouch"
                    toasty(context, msg)
                    toggle = !toggle

                }) {
                Text("poke me")

            }

            GridlineButton(
                onClick = {

                    uploadShortcutData()

                    toasty(context, "uploaded")
                }) {
                Text("upload shortcuts")
            }

            GridlineButton(
                onClick = {
                    downloadShortcutData()

                    toasty(context, "downloaded")
                }) {
                Text("download shortcuts")
            }
            GridlineButton(
                onClick = {
                    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
                    scope.launch {
                        val album = API_State.kotlinApi.albums.getAlbum("spotify:album:6YGXlFehYvg5MuzMPfgkdp")
                        API_State.kotlinApi.player.startPlayback(contextUri = album!!.uri) //null, null, null, null, null)
                    }
                    toasty(context, "bugs squashed")
                }) {
                Text("debug func")
            }
        }
    }
}