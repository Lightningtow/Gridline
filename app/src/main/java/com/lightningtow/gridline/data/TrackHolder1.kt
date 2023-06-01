package com.lightningtow.gridline.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.adamratzman.spotify.models.Playable
import com.adamratzman.spotify.models.Playlist


// the only holder to be used when shuffling
fun WipeHolders(
    wipeOne: Boolean = false,
    wipeTwo: Boolean = false,
    wipeThree: Boolean = false,
) {
    if (wipeOne) {
        TrackHolder1.playlistName = "default"
        TrackHolder1.uri = "default"
        TrackHolder1.contents = listOf();
//        TrackHolder1.templist
        // todo actually wipe templist
    }


    if (wipeTwo) {
        TrackHolder2.contents = listOf();
    }


    if (wipeThree) {
        TrackHolder3.contents = listOf();
    }
}

object TrackHolder1 {//} : ViewModel() {
    var loading = false



    // should maybe just leave these be
    // if it ain't broke, don't fix it
    var playlistName = "DEFAULT_NAME"; // used in TrackView for header
    var uri = "URI" // used for important stuff in shuffle, upload() and such


    // todo just use the one list
    // just delete it if its shuffled and user backs out
    var contents: List<Playable> = listOf();
    var templist by mutableStateOf(listOf<Playable>())
    lateinit var actualist: Playlist;




}
