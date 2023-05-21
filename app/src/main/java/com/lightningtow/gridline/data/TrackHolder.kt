package com.lightningtow.gridline.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.adamratzman.spotify.models.Playable
import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.SimplePlaylist

object TrackHolder : ViewModel() {
    var loading = false
//    val tracks = guardValidSpotifyApi(classBackTo = ActionHomeActivity::class.java) { api ->
////                api.search.searchTrack("Avicii").items
//        val mutlist: MutableList<Track> = mutableListOf()
//
////                for (item in playlistTrackToTrack(api.playlists.getPlaylistTracks(CURRENT).getAllItemsNotNull())) {
////                    mutlist.add(item)
////                }
//        //            Log.e("does it make it here", "?")
//        playlistTrackToTrack(api.playlists.getPlaylistTracks(CURRENT).getAllItemsNotNull())
//    }


    // todo below were actually used
    var playlistName = "DEFAULT_NAME";
    var uri = "URI"

//    var imageuri = "DEFAULT_IMAGE";

    var contents: List<Playable> = listOf();
    var templist by mutableStateOf(listOf<Playable>())
    lateinit var actualist: Playlist;



//    var templist: MutableStateList<Track> = mutableListOf();

//    fun setContents(arg: List<Track>) {
//        contents = arg;
//    }
//    fun setTemplist(arg: MutableList<Track>) {
//        templist = arg;
//    }
//
//    fun getTemplist(): List<Track> {
//        return templist;
//    }
//    fun getTemplistIndex(arg: Int): Track {
//        return templist[arg];
//    }



}
