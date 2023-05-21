package com.lightningtow.gridline.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.adamratzman.spotify.models.SimplePlaylist

object PlaylistsHolder : ViewModel() {
//    var loading = true;
//    var loadinglist: MutableList<Boolean> = mutableListOf<Boolean>(true)


    var loading by mutableStateOf(true)
    var lists by mutableStateOf(listOf<SimplePlaylist>())

}

//class SetupUiState(
//    initialHasPermission: Boolean = false
//) {
//    var hasNotificationPermission: Boolean by mutableStateOf(initialHasPermission)
//
//
//}