package com.lightningtow.gridline.ui.home

import LoadingScreen
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.adamratzman.spotify.models.SimplePlaylist
import com.lightningtow.gridline.data.PlaylistsHolder
import com.lightningtow.gridline.data.PlaylistsHolder.lists
import com.lightningtow.gridline.ui.components.GridlineCoverImage
import com.lightningtow.gridline.ui.components.GridlineHeader
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.ui.components.GridlineDivider

@Composable
fun PlaylistViewMaster(headername: String = "DEFAULT", onPlaylistClick: (SimplePlaylist) -> Unit) {
    GridlineTheme {

        if (PlaylistsHolder.loading) // since `loading` is a mutableStateOf, PlaylistViewMaster automatically recomposes when loading changes
            LoadingScreen(message = "Loading playlists...")
        else
            PlaylistViewPage(headername = headername, onPlaylistClick = onPlaylistClick)
    }
}

@Composable
private fun PlaylistViewPage(headername: String, onPlaylistClick: (SimplePlaylist) -> Unit) {

    Column() { // don't put padding here or you'll move the dividers
        GridlineHeader() {
            Text(
//                    "All Playlists",
                headername,
                color = GridlineTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold,
                style = typography.h5,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 12.dp)
            )
//            }
        }
//        Divider(color = gridline_pink)

//        val sortedlist: List<SimplePlaylist> = PlaylistsHolder.lists.sortedBy { it.name } // todo sort lowercase letters correctly
//        PlaylistList(sortedlist, onPlaylistClick)
        PlaylistList(lists.sortedBy { it.name }, onPlaylistClick)

    }
}


@Composable
private fun PlaylistList(lists: List<SimplePlaylist>, onPlaylistClick: (SimplePlaylist) -> Unit) {
//    val context = LocalContext.current
    LazyColumn {
        items(
            items = lists, itemContent = { item ->

                PlaylistRow(playlistItem = item, onPlaylistClick)
                GridlineDivider()
            })
    }
}


@Composable
private fun PlaylistRow(playlistItem: SimplePlaylist, onPlaylistClick: (SimplePlaylist) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp) // padding between rows
            .padding(start = 8.dp) // padding between playlist name and left edge -- 2
            .clickable(onClick = {
                onPlaylistClick(playlistItem)
            })

    ) {
        GridlineCoverImage(
//            imageModelArg = playlistItem.images.firstOrNull()?.url ?: "https://picsum.photos/300/300",
            image_url = playlistItem.images.first().url,
            deeplink_url = playlistItem.uri.uri
        )

        Box(
            modifier = Modifier
                .padding(start = 8.dp)  // padding between picture and name
                .align(Alignment.CenterVertically) // todo put this below in Text()?
        ) {

            Text(
                // playlist name
                text = playlistItem.name,
                color = GridlineTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
//                modifier = Modifier
//                    .padding(start = 8.dp)
//                    .align(Alignment.CenterVertically)
            )
        }
    }
}
