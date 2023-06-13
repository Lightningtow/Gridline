package com.lightningtow.gridline.ui.home

import LoadingScreen
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.adamratzman.spotify.models.Playable
import com.lightningtow.gridline.R
import com.lightningtow.gridline.data.TrackHolder1
import com.lightningtow.gridline.grid.PlaylistGetter
import com.lightningtow.gridline.player.Player
//import com.lightningtow.gridline.grid.ShuffleInPlace
import com.lightningtow.gridline.ui.components.GridlineButton
import com.lightningtow.gridline.ui.components.GridlineCoverImage
import com.lightningtow.gridline.ui.components.GridlineDivider
import com.lightningtow.gridline.ui.theme.*
import com.lightningtow.gridline.utils.toasty
import com.lightningtow.gridline.ui.components.GridlineHeader
import com.lightningtow.gridline.utils.Constants
import kotlin.math.roundToInt

private var loadingTracks by mutableStateOf(false)
var shouldLoadTracks by mutableStateOf(false)




@Composable
fun TrackViewMaster(uri: String = "default") {
    if (shouldLoadTracks) {
        loadingTracks = true
        shouldLoadTracks = false

        PlaylistGetter.getPlaylistByURI(URI=uri, holder=1, callback = {
            loadingTracks = false
        });

    }

        if (loadingTracks)
//            setContent {
                LoadingScreen(message = "Loading " + TrackHolder1.playlistName)

//            }
        else
            PlayableViewPage(tracks = TrackHolder1.templist, name = TrackHolder1.playlistName)
}


// main page for viewing playables
@Composable
private fun PlayableViewPage(
    tracks: List<Playable>,
    name: String
) {
// todo howsabout make tracks just directly accessed    ??? what does this mean

    GridlineTheme() {

        Column( // padding here messes with the dividers too
        ) {

            Header(name)//, context)

//        Divider(color = gridline_pink)
            PlayableList(tracks)
        }

    }
}

@Composable
private fun Header(
    name: String,
) {
    val context = LocalContext.current

    GridlineHeader(
        modifier = Modifier
//            .background(color = GridlineTheme.colors.uiFloated)
//            .fillMaxWidth()
            .padding(4.dp)
            .padding(start = 8.dp)

    ) { // header row
        GridlineCoverImage(
            size = 80.dp,
            image_url = TrackHolder1.actualist.images.firstOrNull()?.url ?: Constants.DEFAULT_MISSING,
            deeplink_url = TrackHolder1.actualist.uri.uri,
        )
        Log.e("header image", TrackHolder1.actualist.images.firstOrNull()?.url ?: Constants.DEFAULT_MISSING)

        Column() {
            Row( // text row
                modifier = Modifier
                    .padding(start = 12.dp) // padding to left of playlist title
            ) {
                Text(
                    text = name, // playlist title
                    color = GridlineTheme.colors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    style = typography.h5,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,

                    )
            }
            ButtonRow()

        }
    }
}

// creates a row of buttons
@Composable
private fun ButtonRow() { // here
//private fun ButtonRow(activity: BaseActivity? = null) {
    val context = LocalContext.current

    Column() {
        Row(
//        Modifier
            modifier = Modifier
//            .clickable(onClick = { onTrackClick(track) })
                .fillMaxWidth()
                .padding(4.dp), // padding above and left

            horizontalArrangement = Arrangement.Start,
//            horizontal
        ) {
            GridlineButton(
                onClick = {  // shuffle button
//                    if (activity == null) return@GridlineButton // todo why does this exist - here
                    TrackHolder1.shuffle()
                }, modifier = Modifier.padding(end = 4.dp)
            ) { Text("Shuffle") }



            GridlineButton(onClick = {  // upload button
//                if (activity == null) return@GridlineButton //here
                PlaylistGetter.upload()
                toasty(
                    context,
                    "Successfully uploaded playlist"
                ) // todo what happens if unsuccessful? wrap this with trycatch?

            }, modifier = Modifier.padding(end = 8.dp))
            { Text("Upload") }
        }
    }
}

@Composable
private fun PlayableList(playables: List<Playable>) {
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

}

// one playable item
@Composable
private fun PlayableRow(playable: Playable) {
    val context = LocalContext.current
//    var offsetX by remember { mutableStateOf(0f) }
//    var offsetY by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(0f) }
    val dragLen = 250

    var swipeQueueReady by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
//            .clickable(onClick = { onTrackClick(track) })
            .offset {
                if (offset.roundToInt() < 0) IntOffset(0, 0)
                else if (offset.roundToInt() > dragLen) IntOffset(dragLen, 0)
                else IntOffset(offset.roundToInt(), 0)
            }// offsetY.roundToInt()) }
            .fillMaxWidth()
            .padding(4.dp)
            .padding(start = 8.dp) // padding between picture and edge
            .background(color = (if (swipeQueueReady) GridlineTheme.colors.brand else GridlineTheme.colors.uiBackground))
//            .background(color = GridlineTheme.colors.brand)


            .pointerInput(Unit) {
//                detectDragGestures
                detectHorizontalDragGestures(
//                    onDragCancel = { offsetX = 0f; offsetY = 0f; },
//                    onDragEnd = { offsetX = 0f; offsetY = 0f; },
                    onDragCancel = { offset = 0f; swipeQueueReady = false },
                    onDragEnd = {
                        if (swipeQueueReady) {
                            Log.e("PlayerRow", "attempting to swipe queue")
                            Player.queueMe = playable.uri.uri
                            toasty(context, "queued " + playable.asTrack!!.name)
                        }
                        offset = 0f;
                        swipeQueueReady = false

                        // todo this toast will crash podcasts and locals
                    },

                ) { change, dragAmount ->
                    change.consume()

//                    val (x,y) = dragAmount
                    swipeQueueReady = (offset > dragLen)
                    when {
                        dragAmount > 0 -> { // right
//                            Log.e("PlayableRow", "dragging right")

//                            swipeQueueReady = (offset > dragLen)

//                            Log.e("PlayableRow", "swipeQueueReady is $swipeQueueReady")
                        }

                        dragAmount < 0 -> { // left
//                            swipeQueueReady = (offset > dragLen)
//                            Log.e("PlayableRow", "dragging left")
                        }
                    }

                    offset += dragAmount
//                    offsetX += dragAmount.x
//                    offsetY += dragAmount.y
//                    ,
//                    onDragEnd
                }
            }
    ) {
//        if (swipeQueueReady) {
//            Icon(
//                ImageVector.vectorResource(id = R.drawable.round_queue_music_24),
//                contentDescription = "add to queue",
//                tint = GridlineTheme.colors.brand,
//                modifier = Modifier.size(30.dp),
//
//                )
//        }
//        else {
            GridlineCoverImage(
            // todo this deeplink_url can cause crashes
            deeplink_url = "",//,playable.asTrack!!.externalUrls.first { it.name == "spotify" }.url,
            track = playable
        )
//        }




        Column(
            modifier = Modifier
                .padding(start = 8.dp)  // padding between picture and name
        ) {
            Text(
                text = ( // todo getPlayableName
                        if (playable.asTrack != null) playable.asTrack!!.name
                        else if (playable.asLocalTrack != null) playable.asLocalTrack!!.name
                        else if (playable.asPodcastEpisodeTrack != null) playable.asPodcastEpisodeTrack!!.name
//                    else if (playable.)
                        else {
                            Log.e("PlayableRow", "missing trackname in PlayableRow")
                            "missing trackname"
                        }),
                color = GridlineTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
//                text = playable.asTrack!!.artists.joinToString(", ") { it.name },
                text = (
                        if (playable.asTrack != null) playable.asTrack!!.artists.joinToString(", ") { it.name }
                        else if (playable.asLocalTrack != null) playable.asLocalTrack!!.artists.joinToString(", ") { it.name }
                        else if (playable.asPodcastEpisodeTrack != null) playable.asPodcastEpisodeTrack!!.artists.joinToString(", ") { it.name }
                        //                    else if (playable.)
                        else {
                            Log.e("PlayableRow", "missing artistname in PlayableRow")
                            "missing artistname"
                        }),
                color = GridlineTheme.colors.textSecondary,
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
    GridlineDivider()

} // end of playableRow
