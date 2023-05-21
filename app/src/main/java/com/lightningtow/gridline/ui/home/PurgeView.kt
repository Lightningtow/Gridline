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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lightningtow.gridline.data.PurgeData
import com.lightningtow.gridline.grid.purgePlaylist
import com.lightningtow.gridline.ui.components.GridlineButton
import com.lightningtow.gridline.ui.theme.GridlineTheme

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
//                    addToPurgeData(PurgeData.currentSlot, it)
                PurgeData.purgename = it.name
                PurgeData.purgeuri = it.uri.uri
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

        var regen = true;

        for (item in uris_to_purge) { // item is a uri from purgelist
            Log.e("purging", item)

            // regen_purgelist doesnt work cause each list is launched in its own coroutine
            // so either get the purgelist ahead of time (and hang purging till its done)
            // or just launch all concurrently, and get testlist multiple times

            purgePlaylist(victim = item, purgelist = PurgeData.purgeuri, regen_purgelist = regen,

                callback = {
                    displayLoadingScreen = false
                    regen = false

                })
            regen = false

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
        }
//        val victimCallback: () -> Unit = {
//            trackPicking = true // this redoes the whole screen
//            PurgeData.currentSlot = slot;
//        }


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
    }
}

@Composable
private fun Slot(
    slot: Int,
    callback: () -> Unit = {
        trackPicking = true // default is victim
        PurgeData.currentSlot = slot; // just like americans
    }
) {
    var purgelistSlot = false
    if (slot == -42)
        purgelistSlot = true

    var rowname: String = "defualt rowname"


// todo if purgelist is empty it just crashes


// todo https://developer.android.com/topic/libraries/architecture/datastore
    if (purgelistSlot) { // if purgelist

        if (PurgeData.purgename == "default")
            rowname = "Choose purgelist"
        else
            rowname = PurgeData.purgename

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
                .padding(4.dp)

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


//@Composable
//private fun PurgeSlot() {
//    Column(
//        modifier = Modifier
//            .padding(4.dp)
//
//    ) {
////        GridlineButton(onClick = {
////            trackPicking = true // this redoes the whole screen
////            PurgeData.choosingPurgelist = true
////        }) {
////            Text("Pick purgelist")
////        }
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable(onClick = {
//                    trackPicking = true // this redoes the whole screen
//                    PurgeData.choosingPurgelist = true
//                })
//        ) {
//            Text(PurgeData.purgename)
//        }
//
////        Row(modifier = Modifier.fillMaxWidth()
////        ) {
////            Text(PurgeData.purgeuri) }
//
//        Divider(color = GridlineTheme.colors.uiBorder)
//    }
//}
//
//@Composable
//private fun VictimSlot(slot: Int) {
//    Column(
//        modifier = Modifier
//            .padding(4.dp)
//
//    ) {
//
////        GridlineButton(onClick = {
////            trackPicking = true // this redoes the whole screen
////            PurgeData.currentSlot = slot;
////        }) {
////            Text("Pick victim") }
//        var rowname: String = "defualt rowname"
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(4.dp)
//
//                .clickable(onClick = {
//                    trackPicking = true // this redoes the whole screen
//                    PurgeData.currentSlot = slot;
//                })
//        ) {
//
//            if (PurgeData.namelist[slot] == "default")
//                rowname = "Choose victim"
//            else
//                rowname = PurgeData.namelist[slot]
////            Text(PurgeData.namelist[slot])
//        }
//        Text(
//            text = rowname,
//            color = GridlineTheme.colors.textPrimary,
//            textAlign = TextAlign.Center,
//            style = MaterialTheme.typography.body1,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis
//        )
//
////        Row(
////            modifier = Modifier.fillMaxWidth()
////        ) {
////            Text(PurgeData.urilist[slot]) }
//
//    }
//    Divider(color = GridlineTheme.colors.uiBorder)
//}
