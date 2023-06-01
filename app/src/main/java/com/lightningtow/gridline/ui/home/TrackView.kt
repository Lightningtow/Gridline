package com.lightningtow.gridline.ui.home

import LoadingScreen
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.adamratzman.spotify.models.Playable
import com.lightningtow.gridline.data.TrackHolder1
import com.lightningtow.gridline.grid.PlaylistGetter
import com.lightningtow.gridline.grid.shuffle
//import com.lightningtow.gridline.grid.ShuffleInPlace
import com.lightningtow.gridline.ui.components.GridlineButton
import com.lightningtow.gridline.ui.components.GridlineCoverImage
import com.lightningtow.gridline.ui.components.GridlineDivider
import com.lightningtow.gridline.ui.theme.*
import com.lightningtow.gridline.utils.toasty
import com.lightningtow.gridline.ui.components.GridlineHeader

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
            image_url = TrackHolder1.actualist.images.firstOrNull()?.url ?: "https://picsum.photos/300/300",
            deeplink_url = TrackHolder1.actualist.uri.uri,
        )


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
                    shuffle()
                }, modifier = Modifier.padding(end = 4.dp)
            ) { Text("Shuffle") }


//            GridlineButton(onClick = {
//                if (activity == null) return@GridlineButton
//                ShuffleInPlace.remove10()
//            }, modifier = Modifier.padding(end = 4.dp))
//            { Text("Remove 1") }


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
            items = playables, itemContent = { playable ->
//                TrackRow(track = track, onTrackClick = {
                PlayableRow(playable = playable)

                GridlineDivider()
            })
    }

}

// one playable item
@Composable
private fun PlayableRow(playable: Playable) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
//            .clickable(onClick = { onTrackClick(track) })
            .fillMaxWidth()
            .padding(4.dp)
            .padding(start = 8.dp) // padding between picture and edge
    ) {


// todo find better default image
        GridlineCoverImage(
            deeplink_url = playable.asTrack!!.externalUrls.first { it.name == "spotify" }.url,
            track = playable
        )




        Column(
            modifier = Modifier
                .padding(start = 8.dp)  // padding between picture and name
        ) {
            Text(
                text = (
                        if (playable.asTrack != null) playable.asTrack!!.name
                        else if (playable.asLocalTrack != null) playable.asLocalTrack!!.name
                        else if (playable.asPodcastEpisodeTrack != null) playable.asPodcastEpisodeTrack!!.name
//                    else if (playable.)
                        else {
                            Log.e("ctrlfme", "missing trackname in PlayableRow")
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
                            Log.e("ctrlfme", "missing artistname in PlayableRow")
                            "missing artistname"
                        }),
                color = GridlineTheme.colors.textSecondary,
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
