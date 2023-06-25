package com.lightningtow.gridline.ui.components

//import com.lightningtow.gridline.ui.home.dataStore
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.adamratzman.spotify.models.Playable
import com.google.protobuf.InvalidProtocolBufferException
import com.lightningtow.gridline.GridlineApplication.Companion.context
import com.lightningtow.gridline.ShortcutList
import com.lightningtow.gridline.ShortcutStruct
import com.lightningtow.gridline.utils.Constants
import com.lightningtow.gridline.utils.toasty
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import androidx.compose.runtime.getValue

import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.adamratzman.spotify.models.PlaylistTrack
import com.lightningtow.gridline.PlaylistShortcut
import com.lightningtow.gridline.R
import com.lightningtow.gridline.TrackShortcut
import com.lightningtow.gridline.TrackShortcutStore
import com.lightningtow.gridline.data.PlaylistShortcutStoreDataStore
import com.lightningtow.gridline.data.TrackHolder1.templist
import com.lightningtow.gridline.data.TrackShortcutStoreDataStore
import com.lightningtow.gridline.p_TrackList
import com.lightningtow.gridline.ui.theme.GridlineTheme

public enum class SHORTCUT_TYPE { PLAYLIST, TRACK, ARTIST, ALBUM }



//var masterListOfShortcuts: MutableList<KotlinShortcut> = mutableStateListOf()
var realList: List<TrackShortcut> = listOf()

var masterListOfTracks by mutableStateOf(listOf<TrackShortcut>())
var masterListOfPlaylists by mutableStateOf(listOf<PlaylistShortcut>())

fun downloadShortcutData() {
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val templist: MutableList<TrackShortcut> = mutableListOf()
    var listObj: TrackShortcutStore

    scope.launch {// TODO ITS A COROUTINE DUH, RACE CONDITIONS
//        listObj = context.TrackShortcutStoreDataStore.data.first()

//        Log.e("scoped", "scoped $listObj")
//        list = runBlocking { context.shortcutListDataStore.data.first()

//        for (item in listObj.entriesList) {
//
////            tempList += protoToKotlin(item)
//            templist += item.
//        }
//        realList = listThing
//        masterListOfTracks = tempList

        masterListOfTracks = context.TrackShortcutStoreDataStore.data.first().entriesList
        masterListOfPlaylists = context.PlaylistShortcutStoreDataStore.data.first().entriesList

        toasty(context, "downloaded shortcut data")
    }

}

fun uploadShortcutData() {
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    scope.launch {
//        val tempList = mutableListOf<ShortcutStruct>()
//
//        for (item in masterListOfShortcuts) {
////      for (item in datas) {
//
//                tempList += kotlinToProto(item)
//        }

        context.TrackShortcutStoreDataStore.updateData { data ->
            data.toBuilder()
                .clearEntries()
                .addAllEntries(masterListOfTracks)
                .build()
        }
        context.PlaylistShortcutStoreDataStore.updateData { data ->
            data.toBuilder()
                .clearEntries()
                .addAllEntries(masterListOfPlaylists)
                .build()
        }
        toasty(context, "uploaded shortcut data") // todo i dont think this fake context works
    }
}

@Composable
fun FavoriteStar(
    accessUri: String,
    coverUri: String,
    type: SHORTCUT_TYPE,
    displayname: String
) {
//    var thing: Any
    val thing = PlaylistShortcut.newBuilder()
    thing.accessUri = accessUri
    thing.coverUri = coverUri
    thing.displayname = displayname
    val newList: PlaylistShortcut = thing.build()


    val thing2 = TrackShortcut.newBuilder()
    thing2.accessUri = accessUri
    thing2.coverUri = coverUri
    thing2.displayname = displayname
    val newTrack: TrackShortcut = thing2.build()


//    val listItem =

    val favorited: Boolean = if (type == SHORTCUT_TYPE.PLAYLIST) newList in masterListOfPlaylists
    else newTrack in masterListOfTracks

    Icon(
        ImageVector.vectorResource(if (favorited) R.drawable.baseline_star_24 else R.drawable.outline_star_border_24),
        contentDescription = "who cares",
        tint = if (favorited) GridlineTheme.colors.brand else GridlineTheme.colors.iconPrimary,
        modifier = Modifier
            .size(size = 32.dp)
            .clickable(
                onClick = {
                    val newvalue = !favorited

                    if (newvalue) {
                        if (type == SHORTCUT_TYPE.PLAYLIST)  masterListOfPlaylists += newList
                        else masterListOfTracks += newTrack
//                        masterListOfShortcuts += (item)
                    } else {
                        if (type == SHORTCUT_TYPE.PLAYLIST)  masterListOfPlaylists -= newList
                        else masterListOfTracks -= newTrack
//                        masterListOfShortcuts -= (item)
                    }
                    uploadShortcutData()

//                    Log.e("FavoriteStar", masterListOfShortcuts.toString())
                    // todo combine remove with HomePageIcon
//                    favorited = newvalue
                }

            )
    )
}


//data class KotlinShortcut(
//    val accessUri: String,
//    val coverUri: String,
////    val type: SHORTCUT_TYPE,
//    val displayname: String
//)



