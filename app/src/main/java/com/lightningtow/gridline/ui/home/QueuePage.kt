package com.lightningtow.gridline.ui.home

import android.preference.PreferenceActivity
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.endpoints.client.ClientPlayerApi
import com.adamratzman.spotify.http.CacheState
import com.adamratzman.spotify.http.HttpHeader
import com.adamratzman.spotify.http.HttpRequestMethod
import com.adamratzman.spotify.http.HttpResponse
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.SpotifyRequest
//import com.adamratzman.spotify.models.CurrentUserQueue
import com.adamratzman.spotify.models.ErrorObject
import com.adamratzman.spotify.models.ErrorResponse
import com.adamratzman.spotify.models.Playable
import com.lightningtow.gridline.data.API_State
//import com.lightningtow.gridline.data.API_State.currentUserQueue
import com.lightningtow.gridline.data.TrackHolder1
import com.lightningtow.gridline.ui.components.GridlineCoverImage
import com.lightningtow.gridline.ui.components.GridlineHeader
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.utils.Constants
import com.lightningtow.gridline.utils.coroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

//inline fun <reified T> T.callPrivateFunc(name: String, vararg args: Any?): Any? =
//    T::class
//        .declaredMemberFunctions
//        .firstOrNull { it.name == name }
//        ?.apply { isAccessible = true }
//
//        ?.call(this, *args)
//
//inline fun <reified T : Any, R> T.getPrivateProperty(name: String): R? =
//    T::class
//        .memberProperties
//        .firstOrNull { it.name == name }
//        ?.apply { isAccessible = true }
//        ?.get(this) as? R
//
//public suspend fun getUserQueue(): CurrentUserQueue {
//    val clientPlayerApi = ClientPlayerApi(api)
////    val spotifyEndpoint = SpotifyEndpoint(api)
////    clientPlayerApi.
//    return  SpotifyEndpoint.(execute<String>(
//        clientPlayerApi.callPrivateFunc("endpointBuilder", "/me/player/queue").toString()
//    ).toObject(CurrentUserQueue.serializer(), api = api, json = json)
//
//}

//@Serializable
//public data class CurrentUserQueue(
//    @SerialName("currently_playing") val currentlyPlaying: Playable? = null,
//    @SerialName("queue") val queue: List<Playable>
//)

/*
public suspend fun getUserQueue(): CurrentUserQueue {
    return get(
        endpointBuilder("/me/player/queue").toString()
    ).toObject(CurrentUserQueue.serializer(), api = api, json = json)
}
 */
fun getQueue() {
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    scope.launch(coroutineExceptionHandler) {
//        currentUserQueue = API_State.kotlinApi.player.getUserQueue()
    }
}

object QueuePage {

}

@Composable
fun QueuePageEntry() {
//    API_State.kotlinApi.player.

//    QueuePage(currentUserQueue.queue)
}

@Composable
fun QueuePage(playables: List<Playable>) {
//    GridlineTheme() {
    Text("fuck you display something")
    LazyColumn(
//        modifier = Modifier
//        .padding(start = 8.dp)

    ) {

        items(
            items = playables,
            itemContent = { playable ->
//                TrackRow(track = track, onTrackClick = {
                PlayableRow(playable = playable)

            })
    }
//        Column( // padding here messes with the dividers too
//        ) {
//
//            Header("fuck you")//, context)
//
////        Divider(color = gridline_pink)
////            PlayableList(tracks)
//        }

//    }
}

@Composable
private fun Header(
    playlistName: String,
) {
    val context = LocalContext.current

    GridlineHeader(
        modifier = Modifier
//            .background(color = GridlineTheme.colors.uiFloated)
//            .fillMaxWidth()
            .padding(4.dp)
            .padding(start = 8.dp)

    ) { // header row

        Column() {
            Row( // text row
                modifier = Modifier
                    .padding(start = 12.dp) // padding to left of playlist title
            ) {
                Text(
                    text = playlistName, // playlist title
                    color = GridlineTheme.colors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h5,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,

                    )
            }
//            ButtonRow(playlistName)

        }
    }
}