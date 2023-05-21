package com.lightningtow.gridline.ui.home

import LoadingScreen
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
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
import androidx.core.content.ContextCompat.startActivity
import com.adamratzman.spotify.models.Playable
import com.lightningtow.gridline.data.TrackHolder
import com.lightningtow.gridline.grid.PlaylistGetter
import com.lightningtow.gridline.grid.ShuffleInPlace
import com.lightningtow.gridline.ui.components.GridlineButton
import com.skydoves.landscapist.glide.GlideImage
import com.lightningtow.gridline.ui.theme.*
import com.lightningtow.gridline.utils.toast

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
                LoadingScreen(message = "Loading " + TrackHolder.playlistName)

//            }
        else
            PlayableViewPage(tracks = TrackHolder.templist, name = TrackHolder.playlistName)
}


// main page for viewing playables
@Composable
//fun PlayableViewPage(activity: BaseActivity? = null, tracks: List<Playable>, name: String,  onNameClick: (SimplePlaylist) -> Unit) {
private fun PlayableViewPage(
//    activity: BaseActivity? = null,
    tracks: List<Playable>,
    name: String
) { // todo howsabout make tracks just directly accessed
    val context = LocalContext.current
//    val imageModifier = Modifier
//        .height(80.dp)
//        .width(80.dp)

    GridlineTheme() {


        Column( // padding here messes with the dividers too
        ) {

            Header(name)//, context)

//        Divider(color = gridline_pink)
            PlayableList(tracks)
        }
//        startActivity(context, PlaylistViewActivity, )
//                    toast(context, "You clicked ${track.name} - opening in spotify")
//    }
    }
}

@Composable
private fun Header(
    name: String,
//    context: Context
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .background(color = GridlineTheme.colors.uiFloated)
            .fillMaxWidth()
            .padding(4.dp)
            .padding(start = 8.dp)
    ) { // header row

        GlideImage(
//            imageModel = (TrackHolder.imageuri
            imageModel = (TrackHolder.actualist.images.firstOrNull()?.url
                ?: "https://picsum.photos/300/300"),
            modifier = Modifier
                .height(80.dp)
                .width(80.dp)
                .clickable {
                    //                itemContent = { track ->
//                onNameClick = {
                    val browserIntent =
                        Intent(
                            Intent.ACTION_VIEW,
//                            Uri.parse(TrackHolder.uri.toString())
                            Uri.parse(TrackHolder.actualist.uri.uri)
//                          Uri.parse(track.externalUrls.first { it.name == "spotify" }.url)
                        )
                    startActivity(context, browserIntent, null)
                },
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
                    ShuffleInPlace.shuffle()
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
                toast(
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
//private fun TrackList(tracks: MutableList<Track>) {
    LazyColumn(
//        modifier = Modifier
//        .padding(start = 8.dp)

    ) {

        items(
            items = playables, itemContent = { playable ->
//                TrackRow(track = track, onTrackClick = {
                PlayableRow(playable = playable)

                Divider(color = GridlineTheme.colors.uiBorder)
            })
    }

}

// one playable item
@Composable
//private fun TrackRow(track: Track, onTrackClick: (Track) -> Unit) {
private fun PlayableRow(playable: Playable) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
//            .clickable(onClick = { onTrackClick(track) })
            .fillMaxWidth()
            .padding(4.dp)
            .padding(start = 8.dp) // padding between picture and edge
    ) {
//        val imageModifier = Modifier
//            .height(40.dp)
//            .width(40.dp)

// todo find better default image
        GlideImage(
            modifier = Modifier // image modifiers
                .height(40.dp)
                .width(40.dp)
                .clickable {
                    val browserIntent =
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(playable.asTrack!!.externalUrls.first { it.name == "spotify" }.url)
                        )
                    startActivity(context, browserIntent, null)
                },
            imageModel = (
                    if (playable.asTrack != null) playable.asTrack!!.album.images.firstOrNull()?.url
                        ?: "https://picsum.photos/300/300"
                    else if (playable.asLocalTrack != null) "https://picsum.photos/300/300"
//                    else if (playable.asLocalTrack != null) playable.asTrack!!.album.images.firstOrNull()?.url ?: "https://picsum.photos/300/300"
                    else if (playable.asPodcastEpisodeTrack != null) playable.asPodcastEpisodeTrack!!.album.images.firstOrNull()?.url
                        ?: "https://picsum.photos/300/300"
                    else {
                        Log.e("ctrlfme", "???")
                        "???" // text =
                    }),
//            imageModel = playable.asTrack?.album?.images?.firstOrNull()?.url ?: "https://picsum.photos/300/300",
            contentDescription = null,

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
                            Log.e("ctrlfme", "???")
                            "???" // text =
                        }),
                color = GridlineTheme.colors.textPrimary,
//                color = Color(0xFFF0F0F0),

//                playable.asTrack!!.name,
                fontWeight = FontWeight.Bold,
//                style = MaterialTheme.typography.body1,
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
//                text = playable.asTrack!!.artists.joinToString(", ") { it.name },
                text = (
                        if (playable.asTrack != null) playable.asTrack!!.artists.joinToString(", ") { it.name }
                        else if (playable.asLocalTrack != null) playable.asLocalTrack!!.artists.joinToString(
                            ", "
                        ) { it.name }
                        else if (playable.asPodcastEpisodeTrack != null) playable.asPodcastEpisodeTrack!!.artists.joinToString(
                            ", "
                        ) { it.name }
                        //                    else if (playable.)
                        else {
                            Log.e("ctrlfme", "???")
                            "???" // text =
                        }),
                color = GridlineTheme.colors.textSecondary,
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

////class TrackViewActivity : BaseActivity() {
//class TrackViewActivity : AppCompatActivity() {
////
//////    val context = LocalContext.current doesnt work here!!
////
////    //    private val scope = MainScope()
////    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
////
////    fun asyncGetData() =
////        scope.launch { // Is invoked in UI context with Activity's scope as a parent
////
//////            PlaylistHolder.loading = true
////            PlaylistGetter.getPlaylistByURI(uri, 1);
//////            PlaylistGetter.getPlaylist();
////
//////            DisplayTracks()
////            TrackHolder.templist = TrackHolder.contents
////            setContent {
////                PlayableViewPage(TrackHolder.templist, TrackHolder.playlistName) // here
////            }
////        }
////    // todo if crashes cause something 'activity' try uncommenting all the lines that say 'here'
//
//
////    fun DisplayTracks() {
////        setContent {
////            GridlineTheme() {
////                PlayableViewPage(TrackHolder.templist, TrackHolder.playlistName) // here
//////                PlayableViewPage(this, TrackHolder.templist, TrackHolder.playlistName)
////            }
////        }
////    }
//
//    // https://stackoverflow.com/questions/47499891/how-i-can-use-callback-in-kotlin
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        asyncGetPlaylistTracks()
//        setContent {
////            TrackViewMaster
//
//
//////            GridlineTheme() {
//////            val msg = "Loading " + TrackHolder.playlistName + "..."
////            val msg = "Loading " + TrackHolder.playlistName + "..."
////            LoadingScreen(msg)
//////            PlayableViewPage(this, PlaylistHolder.templist, PlaylistHolder.playlistName)
//////            }
//
//        }
//
//
//    }
//}

//private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
//fun asyncGetPlaylistTracks() {
//
//    scope.launch { // Is invoked in UI context with Activity's scope as a parent
//
////            PlaylistHolder.loading = true
//        PlaylistGetter.getPlaylistByURI(uri, 1, );
////            PlaylistGetter.getPlaylist();
//
////            DisplayTracks()
//        TrackHolder.templist = TrackHolder.contents
//        loadingTracks = false
//    }
//}