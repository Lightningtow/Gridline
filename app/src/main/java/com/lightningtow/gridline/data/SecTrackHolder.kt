package com.lightningtow.gridline.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.adamratzman.spotify.models.Playable

object SecTrackHolder {//} : ViewModel() {
    var loading = false


    var playlistName = "DEFAULT_NAME";
    var uri = "URI"
    var contents: List<Playable> = listOf();
//    var templist by mutableStateOf(listOf<Playable>())




}
