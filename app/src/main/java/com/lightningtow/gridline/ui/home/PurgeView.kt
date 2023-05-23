package com.lightningtow.gridline.ui.home

import LoadingScreen
import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
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
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lightningtow.gridline.data.PurgeData
import com.lightningtow.gridline.grid.purgePlaylist
import com.lightningtow.gridline.ui.components.GridlineButton
import com.lightningtow.gridline.ui.theme.GridlineTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private var trackPicking by mutableStateOf(false)
private var shouldPurge by mutableStateOf(false)
private var displayLoadingScreen by mutableStateOf(false)
var loadingMessage by mutableStateOf("default")
// At the top level of your kotlin file:

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Composable
fun PurgeViewMaster() {
    if (trackPicking) // since `loading` is a mutableStateOf, PlaylistViewMaster automatically recomposes when loading changes

        if (PurgeData.choosingPurgelist) { // choosing purgelist

            PlaylistViewMaster(onPlaylistClick = {
                PurgeData.purgename.value = it.name
                PurgeData.purgeuri.value = it.uri.uri
                trackPicking = false
                PurgeData.choosingPurgelist = false

            })


        } else { // if picking victimlist

            PlaylistViewMaster(onPlaylistClick = {
                PurgeData.namelist[PurgeData.currentSlot] = it.name
                PurgeData.urilist[PurgeData.currentSlot] = it.uri.uri
                trackPicking = false
            })
        }
    else if (shouldPurge) {
        shouldPurge = false
        displayLoadingScreen = true

        val uris_to_purge = PurgeData.urilist.filter { it != "default" }


        for (item in uris_to_purge) { // item is a uri from purgelist
            Log.e("purging", item)

            // trying to only get the purgelist once doesnt work cause each list is launched in its own coroutine
            // so either get the purgelist ahead of time (and hang all the others purging till its done)
            // or just launch all concurrently, and get purgelist multiple times
            // main issue is making sure A) spotify doesnt get pissed I'm making too many API calls
            // other issue is they're all dumping to SecTrackHolder, even though they're writing the same thing,
            // could be bad to write to the same location 4 at a time

            purgePlaylist(victim = item, purgelist = PurgeData.purgeuri.value,

                callback = {
                    displayLoadingScreen = false

                })

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
            .padding(all = 8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.SpaceEvenly
//            .align(Alignment.CenterVertically)
    ) {
//        center = Arrangement.spacedBy(8.dp),

        val purgeCallback: () -> Unit = {
            trackPicking = true // this redoes the whole screen
            PurgeData.choosingPurgelist = true
        }  // victim callback is the default

        Slot(-42, callback = purgeCallback)
        Text("from")
        Slot(0)
        Slot(1)
        Slot(2)
        Slot(3)
        Slot(4)

        GridlineButton(
            onClick = {
                shouldPurge = true
                displayLoadingScreen = true

            })
        {
            Text("purge!")
        }

        Row(modifier = Modifier.align(alignment = Alignment.End)) {
//            Text(PurgeData.testint.toString())
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
}

@Composable
private fun Slot(
    slot: Int,
    callback: () -> Unit = {
        trackPicking = true // default is victim
        PurgeData.currentSlot = slot;
    }

) {
    var thisIsPurgelist = false

    if (slot == -42)
        thisIsPurgelist = true

    var rowname: String = "defualt rowname"
// todo https://adamint.github.io/spotify-web-api-kotlin-docs/spotify-web-api-kotlin/com.adamratzman.spotify.endpoints.pub/-playlist-api/get-playlist-covers.html

// todo if purgelist is empty it just crashes


    if (thisIsPurgelist) { // if purgelist

        if (PurgeData.purgename.value == "default")
            rowname = "Choose purgelist"
        else
            rowname = PurgeData.purgename.value

    } else { // if victim

        if (PurgeData.namelist[slot] == "default")
            rowname = "Choose victim"
        else
            rowname = PurgeData.namelist[slot]
    }


    Column(
        modifier = Modifier
            .padding(4.dp)

    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)

                .clickable(onClick = {
                    callback()
                })
        ) {


//            Text(PurgeData.namelist[slot])
        }
    }
    Text(
        text = rowname,
        color = GridlineTheme.colors.textPrimary,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.body1,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )

//        Row(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(PurgeData.urilist[slot]) }

//}
    Divider(color = GridlineTheme.colors.uiBorder)
}

