package com.lightningtow.gridline.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.adamratzman.spotify.SpotifyClientApi
//import com.adamratzman.spotify.models.CurrentUserQueue
import com.lightningtow.gridline.auth.Model
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerContext
import com.spotify.protocol.types.PlayerState

object API_State {
    /**     never update these manually  */
    val currentPlayerState: MutableState<PlayerState?> = mutableStateOf(null)
    val currentPlayerContext: MutableState<PlayerContext?> = mutableStateOf(null)

    val contextLen: MutableState<Int?> = mutableStateOf(null) // todo what tf is this
    val currentTrackCover: MutableState<Any?> = mutableStateOf(null) // todo what format is this
    val currentPos: MutableState<Long> = mutableStateOf(0) // current position of track todo in MS? seconds?
    var spotifyAppRemote: SpotifyAppRemote? = null

//    val kotlinApi = Model.credentialStore.getSpotifyClientPkceApi()!! // todo yo wtf this gets initialized on its own by api_state

    lateinit var kotlinApi: SpotifyClientApi
//    lateinit var currentUserQueue: CurrentUserQueue

    var OFFLINE = false

    /**     never update these manually  */

}