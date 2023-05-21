package com.lightningtow.gridline.ui.home

import LoadingScreen
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
//import com.lightningtow.gridline.R
import androidx.core.content.ContextCompat.startActivity
import com.adamratzman.spotify.models.SimplePlaylist
import com.lightningtow.gridline.data.PlaylistsHolder
import com.lightningtow.gridline.data.PlaylistsHolder.lists
import com.lightningtow.gridline.data.SecTrackHolder
import com.lightningtow.gridline.data.TrackHolder
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.ui.theme.gridline_pink
import com.skydoves.landscapist.glide.GlideImage

//class PlaylistViewActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            PlaylistViewMaster(onPlaylistClick = )
//        }
//    }
//}

@Composable
fun PlaylistViewMaster(onPlaylistClick: (SimplePlaylist) -> Unit) {
//fun PlaylistViewMaster(holder: Int, onPlaylistClick: () -> Unit) {
    GridlineTheme {

        if (PlaylistsHolder.loading) // since `loading` is a mutableStateOf, PlaylistViewMaster automatically recomposes when loading changes
            LoadingScreen(message = "Loading playlists...")
        else
//            PlaylistViewPage(holder = holder, onPlaylistClick)
            PlaylistViewPage(onPlaylistClick)

    }

}

@Composable
//fun PlaylistViewPage(activity: BaseActivity? = null, lists: List<SimplePlaylist>?) {
//fun PlaylistViewPage(lists: List<SimplePlaylist>?) {
private fun PlaylistViewPage(onPlaylistClick: (SimplePlaylist) -> Unit) {
//    private fun PlaylistViewPage(holder: Int, onPlaylistClick: () -> Unit) {

//    LoadingScreen(message = "Loading playlists...")

//        while (PlaylistsHolder.loading) {
//            LoadingScreen(message = "Loading playlists...")
//        }
//        while(!PlaylistsHolder.loading) {

    Column(
        modifier = Modifier
            .background(color = GridlineTheme.colors.uiBackground)
// don't put padding here or you'll move the dividers
    ) {
        Row(
            modifier = Modifier
                .background(color = GridlineTheme.colors.uiFloated)
                .fillMaxWidth()
        ) // padding to put space to the left of the 'all playlists'
        {
            Row(modifier = Modifier.padding(start = 12.dp)) {
                Text(

                    "All Playlists",
                    color = GridlineTheme.colors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    style = typography.h5,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis

                )
            }
        }
//        Divider(color = gridline_pink)

        if (lists != null) {
            val sortedlist: List<SimplePlaylist> =
                PlaylistsHolder.lists.sortedBy { it.name } // todo deal with capitals
//            PlaylistList(holder = holder, sortedlist, onPlaylistClick)
            PlaylistList(sortedlist, onPlaylistClick)

        }
    }
//        }
}


// todo figure out how to auto refresh auth

@Composable
private fun PlaylistList(lists: List<SimplePlaylist>, onPlaylistClick: (SimplePlaylist) -> Unit) {
//private fun PlaylistList(holder: Int, lists: List<SimplePlaylist>, onPlaylistClick: () -> Unit) {

    val context = LocalContext.current
    LazyColumn {
        items(

            items = lists, itemContent = { item ->

//                PlaylistRow(holder = holder, playlistItem = item, onPlaylistClick)
                PlaylistRow(playlistItem = item, onPlaylistClick)
//                = {
//
//                    val intent = Intent(context, TrackViewActivity::class.java)
////                        val intent = Intent(context, LoadingScreenActivity::class.java)
//
////                    val id = item.uri.uri
////                        toast(context, id)
////                        Log.e("URIIIII", id)
//
////                        intent.putExtra("uri", id)
//                    TrackHolder.uri = item.uri.uri;
//                    TrackHolder.playlistName = item.name
//                    startActivity(context, intent, null)
//                })

                Divider(color = GridlineTheme.colors.uiBorder)


            })

    }

}

@Composable
private fun PlaylistRow(playlistItem: SimplePlaylist, onPlaylistClick: (SimplePlaylist) -> Unit) {
//private fun PlaylistRow(holder: Int, playlistItem: SimplePlaylist, onPlaylistClick: () -> Unit) {

//private fun PlaylistRow(list: SimplePlaylist) {
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
//                TrackHolder.uri = id;
//                TrackHolder.playlistName = playlistItem.name
//                startActivity(context, intent, null)
//            })

            .fillMaxWidth()
            .padding(4.dp) // padding between rows
            .padding(start = 8.dp) // padding between playlist name and left edge -- 2

    ) {
        val imageModifier = Modifier
            .height(40.dp)
            .width(40.dp)
//            .height(46.dp)
//            .width(46.dp)
// todo find better default image
        GlideImage(
            imageModel = (playlistItem.images.firstOrNull()?.url
                ?: "https://picsum.photos/300/300"),
//                    else {
//                        Log.e("ctrlfme", "???")
//                        "???" // text =
//                    }),
//            contentDescription = null,
            modifier = imageModifier
        )
        Box(
            modifier = Modifier
                .padding(start = 8.dp)  // padding between picture and name
                .align(Alignment.CenterVertically)
//            .verticalAr
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
