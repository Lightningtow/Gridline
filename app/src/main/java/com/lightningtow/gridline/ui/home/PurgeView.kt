package com.lightningtow.gridline.ui.home

import LoadingScreen
import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.adamratzman.spotify.models.SimplePlaylist
import com.lightningtow.gridline.data.PurgeData
import com.lightningtow.gridline.grid.purgePlaylist
import com.lightningtow.gridline.ui.components.GridlineButton
import com.lightningtow.gridline.ui.components.GridlineCoverImage
import com.lightningtow.gridline.ui.components.GridlineDivider
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/* private */ var listPicking by mutableStateOf(false)
/* private */ var shouldPurge by mutableStateOf(false)
private var displayLoadingScreen by mutableStateOf(false)
var loadingMessage by mutableStateOf("default")
// At the top level of your kotlin file:

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Composable
fun PurgeViewMaster() {
    if (listPicking) {// since `loading` is a mutableStateOf, PlaylistViewMaster automatically recomposes when loading changes


//        if (PurgeData.choosingPurgelist) { // choosing purgelist
//
//            PlaylistViewMaster("Choose purgelist", onPlaylistClick = {
//                PurgeData.purgename.value = it.name
//                PurgeData.purgeuri.value = it.uri.uri
//                PurgeData.purgecover.value = it.images.first().url
//
//                listPicking = false
//                PurgeData.choosingPurgelist = false
//
//            })
//        } else { // if picking victimlist

            PlaylistViewMaster("Choose playlist", onPlaylistClick = {
                PurgeData.namelist[PurgeData.currentSlot] = it.name
                PurgeData.urilist[PurgeData.currentSlot] = it.uri.uri
                PurgeData.coverlist[PurgeData.currentSlot] = it.images.first().url
                PurgeData.isEmptylist[PurgeData.currentSlot] = false
                listPicking = false
            })
//        }


    } else if (shouldPurge) {
        shouldPurge = false
        displayLoadingScreen = true

        val urisToPurge = PurgeData.urilist.filter { it != "default" } // don't purge empty slots


        for (item in urisToPurge) { // item is a uri from purgelist
            Log.e("purging", item)

            // trying to only get the purgelist once doesnt work cause each list is launched in its own coroutine
            // so either get the purgelist ahead of time (and hang all the others purging till its done)
            // or just launch all concurrently, and get purgelist multiple times
            // main issue is making sure A) spotify doesnt get pissed I'm making too many API calls
            // other issue is they're all dumping to TrackHolder2, even though they're writing the same thing,
            // could be bad to write to the same location 4 at a time

//            purgePlaylist(victim = item, purgelist = PurgeData.purgeuri.value,
            purgePlaylist(victim = item, purgelist = PurgeData.urilist[0],
                callback = {
                    displayLoadingScreen = false
                }
            )

            displayLoadingScreen = true
        }

    } else CheckLoading()

}

@Composable
private fun CheckLoading() {
    GridlineTheme {
        if (displayLoadingScreen)
            LoadingScreen(message = loadingMessage)
        else
            Homepage()
    }
}

private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

@Composable
private fun Homepage() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .padding(all = 8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.SpaceEvenly
//            .align(Alignment.CenterVertically)
    ) {
//        center = Arrangement.spacedBy(8.dp),

//        val purgeCallback: () -> Unit = {
//            listPicking = true // this redoes the whole screen
//            PurgeData.choosingPurgelist = true
//        }  // victim callback is the default


//        Slot(-42, empty = PurgeData.purgeEmpty.value, onClick = purgeCallback)
        Text("  purgelist")
        Slot(0)
        Text("  victims")
//        Slot(0, empty = PurgeData.isEmptylist[0])
        Slot(2)
        Slot(3)
        Slot(4)
        Slot(5)

        Text("  afterpurge")
        Slot(1)

        GridlineButton(
            modifier = Modifier
                .padding(all = 8.dp),
            onClick = {
                shouldPurge = true
                displayLoadingScreen = true

            })
        { Text("purge!") }


        ButtonRow(Modifier
            .align(alignment = Alignment.End)
        )
    }
}

@Composable
private fun PurgeSlot() {

}

private fun getCover(
    slot: Int,
    callback: () -> Unit = {

    }) {

}


@Composable
private fun Slot(
    slot: Int,
    defaultText: String = "Choose victim",
    onClick: () -> Unit = {
        listPicking = true // default is victim
        PurgeData.currentSlot = slot;
    }
) {

//    val rowname = if (slot == -42) { // if purgelist
//        if (PurgeData.purgename.value == "default") "Choose purgelist"
//        else PurgeData.purgename.value
//
//    } else { // if victim
//        if (PurgeData.namelist[slot] == "default") "Choose victim"
//        else PurgeData.namelist[slot]
//    }

    val rowname: String = if(PurgeData.isEmptylist[slot]) defaultText // if empty
    else PurgeData.namelist[slot]


//    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
//    val api = Model.credentialStore.getSpotifyClientPkceApi()!!


    // todo don't validate via picture, validate at purge



    Row(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(16.dp)
            .padding(4.dp) // padding between rows
            .padding(start = 8.dp) // padding between playlist name and left edge -- 2

            .clickable(onClick = {
                onClick()
            })

    ) {
        GridlineCoverImage(
//            imageModel = "https://picsum.photos/300/300",
//            imageModel = if (slot == -42) PurgeData.purgecover.value
//            else (PurgeData.coverlist[slot]),

            image_url = if(PurgeData.isEmptylist[slot]) "https://picsum.photos/300/300"
            else PurgeData.coverlist[slot],

            deeplink_url = "www.example.com" // todo
        )
        Box(
            modifier = Modifier
                .padding(start = 8.dp)  // padding between picture and name
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = rowname,
                color = GridlineTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }




//        Row(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(PurgeData.urilist[slot]) }
    GridlineDivider()

//}
}


@Composable
private fun ButtonRow(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Row(
        modifier = modifier // lowercase uses argument
//            .align(alignment = Alignment.End)
            .padding(all = 8.dp)
    ) {
        GridlineButton(
            onClick = {
                scope.launch {

                    PurgeData.getSavedPurges(context)
                }
            })
        {
            Text("get")
        }

        GridlineButton(
            onClick = {

                PurgeData.setSavedPurges(context)
            })
        {
            Text("set as default")
        }
        GridlineButton(
            onClick = {
                PurgeData.resetStore(context)
            })
        {
            Text("reset")
        }
    }
}

@Composable
private fun PlaylistRow(playlistItem: SimplePlaylist, onPlaylistClick: (SimplePlaylist) -> Unit) {

    val context = LocalContext.current


    Row(
        modifier = Modifier
            .clickable(onClick = {


                onPlaylistClick(playlistItem)
            })
//            .clickable(onClick = {
//                val intent = Intent(context, TrackViewActivity::class.java)
////                        val intent = Intent(context, LoadingScreenActivity::class.java)
//                val id = playlistItem.uri.uri
////                        intent.putExtra("uri", id)
//                TrackHolder1.uri = id;
//                TrackHolder1.playlistName = playlistItem.name
//                startActivity(context, intent, null)
//            })

            .fillMaxWidth()
            .padding(4.dp) // padding between rows
            .padding(start = 8.dp) // padding between playlist name and left edge -- 2

    ) {

// todo find better default image
        GlideImage(
            imageModel = (playlistItem.images.firstOrNull()?.url
                ?: "https://picsum.photos/300/300"),
//                    else {
//                        Log.e("ctrlfme", "???")
//                        "???" // text =
//                    }),
//            contentDescription = null,
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
        )
        Box(
            modifier = Modifier
                .padding(start = 8.dp)  // padding between picture and name
                .align(Alignment.CenterVertically)
        ) {

            Text(  // playlist name
                color = GridlineTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
                text = playlistItem.name,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}