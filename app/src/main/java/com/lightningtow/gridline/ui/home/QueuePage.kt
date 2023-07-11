package com.lightningtow.gridline.ui.home

import LoadingScreen
import android.preference.PreferenceActivity
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.lightningtow.gridline.data.API_State.currentUserQueue
import com.lightningtow.gridline.data.PlaylistsHolder
//import com.lightningtow.gridline.data.API_State.currentUserQueue
import com.lightningtow.gridline.data.TrackHolder1
import com.lightningtow.gridline.ui.components.GridlineButton
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
import kotlinx.serialization.json.JsonNull.content
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

fun getQueue() {
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    API_State.loadingQueue = true

    scope.launch(coroutineExceptionHandler) {

        API_State.currentUserQueue = API_State.kotlinApi.player.getUserQueue()
        API_State.loadingQueue = false

    }
}

fun clearQueue1() {
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    scope.launch(coroutineExceptionHandler) {
        val uriThing = API_State.kotlinApi.tracks.getTrack("spotify:track:2xZlBSmWu87B5Os7eQL7LL")!!.uri
        API_State.kotlinApi.player.addItemToEndOfQueue(uriThing)
//        API_State.kotlinApi.player.startPlayback(offsetPlayableUri = uriThing)
    }
}
fun clearQueue2() {
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    scope.launch(coroutineExceptionHandler) {
        val uriThing = API_State.kotlinApi.tracks.getTrack("spotify:track:2xZlBSmWu87B5Os7eQL7LL")!!.uri
        val contexturi = API_State.kotlinApi.playlists.getPlaylist("spotify:playlist:7pKIuF5jnS7lgB22AZGqAL")
//        API_State.kotlinApi.player.addItemToEndOfQueue(uriThing)
//        API_State.kotlinApi.player.startPlayback(offsetPlayableUri = uriThing)
        val device = API_State.kotlinApi.player.getDevices()
        API_State.kotlinApi.player.startPlayback(contextUri = contexturi!!.uri, offsetIndex = 3, deviceId = device[0].id)

    }
}

@Composable
fun QueuePageEntry() {
//    API_State.kotlinApi.player.
    GridlineTheme {

        if (API_State.loadingQueue) // since `loadingQueue` is a mutableStateOf, this automatically recomposes when loadingQueue changes
            LoadingScreen(message = "Loading queue...")
        else
//            QU()
            QueuePage(API_State.currentUserQueue?.queue)
    }
}

@Composable
fun QueuePage(playables: List<Playable>?) {
//    GridlineTheme() {
//    Text("fuck you display something")
    Column(modifier = Modifier.fillMaxSize()) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Text(
                "Queue",
                color = GridlineTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h5,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 12.dp)
            )
            GridlineButton(
                onClick = { getQueue() },
            ) {
                Text("Refresh")
            }
            GridlineButton(
                onClick = { clearQueue1() },
            ) {
                Text("Clear 1")
            }
            GridlineButton(
                onClick = { clearQueue2() },
            ) {
                Text("Clear 2")
            }
//            Spacer(modifier = Modifier.size(150.dp))
            Text(playables?.size.toString() ?: "null")
        }
        if (playables != null) {

            VerticalReorderList()

        }

    }
}

@Composable
fun VerticalReorderList() {
//    val data = remember { mutableStateOf(List(100) { "Item $it" }) }
    val data = remember { mutableStateOf(API_State.currentUserQueue!!.queue) }

    val state = rememberReorderableLazyListState(onMove = { from, to ->
        data.value = data.value.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    })
    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .reorderable(state)
            .detectReorderAfterLongPress(state)
    ) {

        items(items = data.value, /*{ it }*/ key = null ) { item: Playable ->
//            ReorderableItem(state, key = null,
//                defaultDraggingModifier = Modifier
//                    .background(GridlineTheme.colors.uiFloated)) { isDragging -> // key = item
//                Box(
//                    modifier = Modifier
////                        .aspectRatio(1f)
//                        .background(GridlineTheme.colors.uiBackground)
//                ) {
////                    Text(text = item.asTrack!!.name,
////                        modifier = Modifier.detectReorderAfterLongPress(state)
////                    )
//                    PlayableRow(item)
//
//                }
//            }

            ReorderableItem(state, key = item.uri.uri) { isDragging -> // key = item
                val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                val dragColor = (if (isDragging) GridlineTheme.colors.error else GridlineTheme.colors.uiBackground)
//                val dragColor = when (isDragging) {
//                    true ->  { GridlineTheme.colors.error }
//                    false -> {GridlineTheme.colors.uiBackground}
//                }
//                val hi = if (isD)

                Column(
                    modifier = Modifier
                        .shadow(elevation.value)
//                        .background(dragColor)
                ) {
//                    Text(item)
                    PlayableRow(item, )//modifier = Modifier.background(dragColor))
                }
            }
        }
    }
}

