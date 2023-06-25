package com.lightningtow.gridline.ui.home

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.lightningtow.gridline.PlaylistShortcut
import com.lightningtow.gridline.TrackShortcut
import com.lightningtow.gridline.data.PlaylistsHolder.lists
import com.lightningtow.gridline.player.Player
import com.lightningtow.gridline.ui.components.GridlineDivider
import com.lightningtow.gridline.ui.components.SHORTCUT_TYPE
import com.lightningtow.gridline.ui.components.masterListOfPlaylists
import com.lightningtow.gridline.ui.components.masterListOfTracks
import com.lightningtow.gridline.ui.components.realList
import com.lightningtow.gridline.ui.components.uploadShortcutData
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.utils.Constants
import com.lightningtow.gridline.utils.toasty
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.serialization.json.JsonNull.content

object HomePage {

//var HomepageShortcutList: MutableList<KotlinShortcut>

    @Composable
    private fun HomePage() {
//        Text("hello world")
//        LazyVerticalGrid( //
        Log.e("homepage", realList.toString())

        Column(
            //            columns = GridCells.Adaptive(minSize = 128.dp),
            verticalArrangement = Arrangement.Center,
//            horizontalA
            modifier = Modifier
                .fillMaxSize()
//                .padding(start = 8.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                content = {
                    items(masterListOfPlaylists.size) { index ->
                        HomePageIcon(list = masterListOfPlaylists[index])

                    }
                    items(masterListOfTracks.size) { index ->
                        HomePageIcon(track = masterListOfTracks[index])

                    }
//                    items(masterListOfTracks.size) { index ->
////                        GridlineDivider()
//
//                        HomePageIcon(track = masterListOfTracks[index])
//                    }
                }
            )
        }
    }
//        Column(
////            columns = GridCells.Adaptive(minSize = 128.dp),
////            verticalArrangement = Arrangement.Center,
////            horizontalA
//            modifier = Modifier
//                .fillMaxSize()
////                .padding(start = 8.dp)
//
//        ) {
//            Column() {
////            Row() {
//                LazyHorizontalGrid(
////                    rows = GridCells.Adaptive(minSize = 128.dp),
//                    rows = GridCells.Fixed(2) ,
//
////                    verticalArrangement = Arrangement.Center,
////            horizontalA
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(all = 4.dp)
//
//                ) {
//
//                    items(masterListOfPlaylists.size) { index ->
//                        HomePageIcon(list = masterListOfPlaylists[index])
//
//                    }
//
////                    items(masterListOfTracks.size) { index ->
//////                        GridlineDivider()
////
////                        HomePageIcon(track = masterListOfTracks[index])
////                    }
//
//                }
//            }
//            GridlineDivider()
//
//            Column(
//
//                verticalArrangement = Arrangement.Center,
//                modifier = Modifier
//                    .fillMaxSize()
//                ) {//                LazyVerticalGrid(
//
//                LazyHorizontalGrid(
////                    rows = GridCells.Adaptive(minSize = 128.dp),
//                    rows = GridCells.Fixed(2),
////                    verticalArrangement = Arrangement.Center,
////            horizontalA
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(all = 4.dp)
//
//                ) {
//                    items(masterListOfTracks.size) { index ->
//                        HomePageIcon(track = masterListOfTracks[index])
//                    }
//                }
//            }
//        }
//    }


        @Composable
        public fun HomePageEntry() {
            GridlineTheme() {
                HomePage()

            }
        }


        @OptIn(ExperimentalFoundationApi::class)
        @Composable
        fun HomePageIcon(
            list: PlaylistShortcut? = null,
            track: TrackShortcut? = null,
        ) {

            val context = LocalContext.current
//    val coveraaa by remember { mutableStateOf(realCover) }

            // todo use enum maybe?
//    val item = list ?: track // lol uses whichever one is null
            // todo gets fucked if multiple are not null
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .combinedClickable(

                        onClick = {
                            if (list != null) {
                                Log.e("deeplink", "deeplinking to ${list.accessUri}")

                                val browserIntent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(list.accessUri))
                                browserIntent.putExtra(
                                    Intent.EXTRA_REFERRER,
                                    Uri.parse("android-app://" + context.packageName)
                                )
                                ContextCompat.startActivity(context, browserIntent, null)

                            } else if (track != null) {
                                Player.queueMe = track.accessUri
                                toasty(context, "Queued ${track.displayname}")
                            }
                        },
                        onLongClick = { // todo context menu
                            if (list != null) masterListOfPlaylists -= list
                            else masterListOfTracks -= track!!

                            uploadShortcutData()

// todo combine with FavoriteStar

                        }

                        //  Uri.parse(track.externalUrls.first { it.name == "spotify" }.url) // what does this do
                    )
            ) {


//        val cover: MutableState<Any?> = mutableStateOf(null)
//        val test = getStuff()
//        Log.e("ctrlfme", "displaying $coveraaa")
                GlideImage(
//            imageModel = getCover(uri, type) ?: Constants.DEFAULT_MISSING,
                    imageModel = if (list != null) list.coverUri else track?.coverUri
                        ?: Constants.DEFAULT_MISSING,
                    modifier = Modifier
                        .size(125.dp)
                )
                Text(
                    if (list != null) list.displayname else track?.displayname ?: "displayname",
//            item
                    style = MaterialTheme.typography.body2,
//            textAlign = TextAlign.Center, // useless if parent column overwrites
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
//            color = GridlineTheme.colors.brand,

                )
            }
        }
    }

//@Composable
//fun HomePageIcon(
//    accessUri: String,
//    coverUri: String,
//    type: SHORTCUT_TYPE,
//    displayname: String,
//
////    cover: String? = null,
//) {
//    val item = KotlinShortcut(
//        accessUri = accessUri,
//        coverUri = coverUri,
//        type = type,
//        displayname = displayname,
//    )
//    HomePageIcon(item)
//
//}
