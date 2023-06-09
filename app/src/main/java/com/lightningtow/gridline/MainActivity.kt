package com.lightningtow.gridline

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.navigation.compose.rememberNavController
import com.lightningtow.gridline.auth.guardValidSpotifyApi
import com.lightningtow.gridline.data.PlaylistsHolder
import com.lightningtow.gridline.player.PlayerActivity
import com.lightningtow.gridline.player.PlayerActivity.spotifyAppRemote
import com.lightningtow.gridline.ui.components.BottomNavigationBar
import com.lightningtow.gridline.ui.components.NavHostContainer
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    fun loadPlaylists() =
        scope.launch { // Is invoked in UI context with Activity's scope as a parent

            guardValidSpotifyApi(classBackTo = MainActivity::class.java) { api ->
                PlaylistsHolder.lists = api.playlists.getClientPlaylists().getAllItemsNotNull()
            }
            PlaylistsHolder.loading = false;

        }

    private val clientId: String = BuildConfig.SPOTIFY_CLIENT_ID
    private val redirectUri: String = BuildConfig.SPOTIFY_REDIRECT_URI_PKCE

    //    private var spotifyAppRemote: SpotifyAppRemote? = null
    private fun connected() {
        PlayerActivity.spotifyAppRemote?.let { it ->
            // Subscribe to PlayerState
            /**
             * automatically update isPlaying
             */
            it.playerApi.subscribeToPlayerState().setEventCallback {
//                val track: Track = it.track
//                Log.d("MainActivity", track.name + " by " + track.artist.name)
                PlayerActivity.isPlaying.value = !it.isPaused
            }
        }
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()
            ?.setEventCallback { playerState -> PlayerActivity.isPlaying.value = !playerState.isPaused }
            ?.setErrorCallback { throwable -> }
    }
    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadPlaylists()
//        fun tart() {
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()


        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                PlayerActivity.spotifyAppRemote = appRemote
                Log.d("MainActivity", "Connected! Yay!")
                // Now you can start interacting with App Remote
                connected()

            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })
//            setContent {
//                PlayerPage()
//            }
//        }
        setContent {
            GridlineTheme {

                // remember navController so it does not
                // get recreated on recomposition
                val navController = rememberNavController()

                // Scaffold Component
                Scaffold(
                    // Bottom navigation
                    bottomBar = {

                        BottomNavigationBar(navController = navController)
                    }, content = { padding ->

                        // Navhost: where screens are placed
                        NavHostContainer(navController = navController, padding = padding)
                    }
                )
            }
        }
    }

}
