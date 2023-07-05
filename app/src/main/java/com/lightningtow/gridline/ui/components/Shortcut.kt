package com.lightningtow.gridline.ui.components

//import com.lightningtow.gridline.ui.home.dataStore
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lightningtow.gridline.GridlineApplication.Companion.ApplicationContext
//import com.lightningtow.gridline.ShortcutList
//import com.lightningtow.gridline.ShortcutStruct
import com.lightningtow.gridline.utils.toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue

import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.lightningtow.gridline.PlaylistShortcut
import com.lightningtow.gridline.R
import com.lightningtow.gridline.TrackShortcut
import com.lightningtow.gridline.TrackShortcutStore
import com.lightningtow.gridline.data.PlaylistShortcutStoreDataStore
import com.lightningtow.gridline.data.TrackShortcutStoreDataStore
//import com.lightningtow.gridline.p_TrackList
import com.lightningtow.gridline.ui.theme.GridlineTheme

public enum class SHORTCUT_TYPE { PLAYLIST, TRACK, ARTIST, ALBUM }



//var masterListOfShortcuts: MutableList<KotlinShortcut> = mutableStateListOf()

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
//        masterListOfTracks = tempList

        masterListOfTracks = ApplicationContext.TrackShortcutStoreDataStore.data.first().entriesList
        masterListOfPlaylists = ApplicationContext.PlaylistShortcutStoreDataStore.data.first().entriesList

        toasty(ApplicationContext, "downloaded shortcut data")
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

        ApplicationContext.TrackShortcutStoreDataStore.updateData { data ->
            data.toBuilder()
                .clearEntries()
                .addAllEntries(masterListOfTracks)
                .build()
        }
        ApplicationContext.PlaylistShortcutStoreDataStore.updateData { data ->
            data.toBuilder()
                .clearEntries()
                .addAllEntries(masterListOfPlaylists)
                .build()
        }
        toasty(ApplicationContext, "uploaded shortcut data") // todo i dont think this fake context works
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
                    val not_favorited = !favorited

                    if (not_favorited) {
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



