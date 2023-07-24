package com.lightningtow.gridline.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.Refresh
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.lightningtow.gridline.data.API_State
import com.lightningtow.gridline.player.Player
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.utils.coroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import com.lightningtow.gridline.data.API_State.kotlinApi
import com.lightningtow.gridline.s
import com.lightningtow.gridline.ui.components.GridlineButton
import com.lightningtow.gridline.utils.toasty
import com.spotify.protocol.types.PlayerContext
import com.spotify.protocol.types.PlayerState

@Composable
fun DebugText(
    text: String,
    modifier: Modifier = Modifier,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = 1,
) {
//    color: Color,
//    fontSize: TextUnit,
//    fontStyle: FontStyle?,
//    fontWeight: FontWeight?,
//    fontFamily: FontFamily?,
//    letterSpacing: TextUnit,
//    textDecoration: TextDecoration?,
//    textAlign: TextAlign?,
//    lineHeight: TextUnit,
//    softWrap: Boolean,
//    minLines: Int,
//    onTextLayout: (TextLayoutResult) -> Unit,
//    style: TextStyle

    GridlineTheme() {
        Text(text, modifier = modifier, maxLines = maxLines, overflow = TextOverflow.Ellipsis)
        Spacer(modifier = Modifier.size(40.dp))
    }
}

//object DebugInfo {
    val isTokenValid: MutableState<String?> = mutableStateOf(null)
    val Drones: MutableState<String?> = mutableStateOf(null)
//}

fun refreshDebugInfo() {
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    scope.launch(coroutineExceptionHandler) {
        isTokenValid.value = kotlinApi.isTokenValid().isValid.toString()
        val getDrones = kotlinApi.tracks.getTrack("spotify:track:5JvspoRjPwqMmPV5anGYZj")
//            Log.e(s, "testing KotlinAPI: should be Drones: ${drones?.asTrack?.name}")
        Drones.value = getDrones?.asTrack?.name
    }
}

//data class DebugInfo(
//) {

//}

@Composable
fun DebugPage() {
    val context = LocalContext.current
    GridlineTheme() {


//    if (!Player.debugging.value) return
//    Column(horizontalAlignment = Alignment.End) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                GridlineButton(
                    onClick = {
                        Drones.value = "..."
                        isTokenValid.value = "..."
                        refreshDebugInfo()
//                        toasty(context, "clicked")
                    }
                ) {
                    Text("Refresh")
                }
            }

//        Text("rand skip enabled: " + Player.randSkipEnabled.value.toString())
            DebugText("kotlinAPI: ${kotlinApi.toString()}")
//            Text("player state: " + API_State.currentPlayerState.value.toString(), overflow = TextOverflow.Clip)
//            Text("player context: " + API_State.currentPlayerContext.value.toString(), overflow = TextOverflow.Clip)
            DebugText("appRemote connected:  ${API_State.spotifyAppRemote?.isConnected}")
            DebugText("kotlin token: " + kotlinApi.clientId)
            DebugText("pkce? " + kotlinApi.usesPkceAuth)
            DebugText("kotlin expires in: " + kotlinApi.expireTime)
            DebugText("is kotlin valid: " + (isTokenValid.value ?: "null"))
            DebugText("drones: ${Drones.value ?: "null"}")

//            val drones = kotlinApi.tracks.getTrack("spotify:track:5JvspoRjPwqMmPV5anGYZj")
//            Log.e(s, "testing KotlinAPI: should be Drones: ${drones?.asTrack?.name}")
        }
    }
}