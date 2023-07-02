package com.lightningtow.gridline.ui.home

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.snap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.lightningtow.gridline.PlaylistShortcut
import com.lightningtow.gridline.TrackShortcut
import com.lightningtow.gridline.data.PlaylistsHolder.lists
import com.lightningtow.gridline.player.Player
import com.lightningtow.gridline.ui.components.GridlineButton
import com.lightningtow.gridline.ui.components.GridlineDivider
import com.lightningtow.gridline.ui.components.masterListOfPlaylists
import com.lightningtow.gridline.ui.components.masterListOfTracks
import com.lightningtow.gridline.ui.components.realList
import com.lightningtow.gridline.ui.components.uploadShortcutData
import com.lightningtow.gridline.ui.home.HomePage.swipeableLeftRight
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.utils.Constants
import com.lightningtow.gridline.utils.toasty
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content
import kotlin.math.roundToInt

object HomePage {

    enum class SHORTCUT_TYPE { PLAYLIST, TRACK, ALBUM, ARTIST }
//var HomepageShortcutList: MutableList<KotlinShortcut>

    fun up() {

    }

    //    @Composable
//    val dividerItem = item
    enum class SwipeDirection(val raw: Int) {
        Left(0),
        Initial(1),
        Right(2),
    }

    @OptIn(ExperimentalMaterialApi::class)
    fun Modifier.swipeableLeftRight(onLeft: () -> Unit, onRight: () -> Unit): Modifier = composed {
        var width by rememberSaveable { mutableStateOf(0f) }
//        var width =  mutableStateOf(0f)

        val swipeableState = rememberSwipeableState(
            SwipeDirection.Initial,
            animationSpec = snap()
        )
        val anchorWidth = remember(width) {
            if (width == 0f) {
                1f
            } else {
                width
            }
        }
        val scope = rememberCoroutineScope()
        if (swipeableState.isAnimationRunning) {
            DisposableEffect(Unit) {
                onDispose {
                    when (swipeableState.currentValue) {
                        SwipeDirection.Left -> {
                            onLeft()
                        }

                        SwipeDirection.Right -> {
                            onRight()
                        }

                        else -> {
                            return@onDispose
                        }
                    }
                    scope.launch {
                        swipeableState.snapTo(SwipeDirection.Initial)
                    }
                }
            }
        }
        return@composed Modifier
            .onSizeChanged { width = it.width.toFloat() }
            .swipeable(
                state = swipeableState,
                anchors = mapOf(
                    0f to SwipeDirection.Left,
                    anchorWidth / 2 to SwipeDirection.Initial,
                    anchorWidth to SwipeDirection.Right,
                ),
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
    }

    var currrentPage: MutableState<SHORTCUT_TYPE> = mutableStateOf(SHORTCUT_TYPE.TRACK)

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
                .swipeableLeftRight(
                    onLeft = {
                        currrentPage.value = SHORTCUT_TYPE.TRACK
                    },
                    onRight = {
                        currrentPage.value = SHORTCUT_TYPE.PLAYLIST
                    }
                )
//                .swipeable(
//                    state = swipeableState,
//                    orientation = Orientation.Horizontal,
////                    anchors = mapOf(0f to 0, sizePx to 1, -sizePx to 2)
////                    anchors = mapOf(
////                        0f to SHORTCUT_TYPE.PLAYLIST,
////                        50f to SHORTCUT_TYPE.TRACK,
////                        100f to SHORTCUT_TYPE.ARTIST,
////                        150f to SHORTCUT_TYPE.ALBUM,
////                        )
//                    anchors = mapOf(
//                        0f to SwipeDirection.Left,
//                        width / 2 to SwipeDirection.Initial,
//                        width to SwipeDirection.Right,
//                    ),
//                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
//
//                    )
//                .padding(start = 8.dp)
        ) {
            LazyVerticalGrid(
//                modifier = Modifier
//                    .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) },
                columns = GridCells.Adaptive(minSize = 128.dp),
                content = {

                    when (currrentPage.value) {
                        SHORTCUT_TYPE.PLAYLIST -> {
                            items(masterListOfPlaylists.size) { index ->
                                HomePageIcon(list = masterListOfPlaylists[index])
                            }
                        }

                        SHORTCUT_TYPE.TRACK -> {
                            items(masterListOfTracks.size) { index ->
                                HomePageIcon(track = masterListOfTracks[index])
                            }
                        }

                        SHORTCUT_TYPE.ALBUM -> {
                            item {
                                Text("album goes here")
                            }

                        }

                        SHORTCUT_TYPE.ARTIST -> {
                            item {
                                Text("artist goes here")
                            }
                        }

                    }
//                    items(masterListOfPlaylists.size) { index ->
//                        HomePageIcon(list = masterListOfPlaylists[index])
//                    }
//
//                    item( span = { GridItemSpan(maxLineSpan) }) {
//                        Spacer(modifier = Modifier.size(42.dp))
//                    }
//
//                    items(masterListOfTracks.size) { index ->
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
