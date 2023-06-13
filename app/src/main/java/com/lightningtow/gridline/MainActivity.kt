package com.lightningtow.gridline

import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.navigation.compose.rememberNavController
import com.lightningtow.gridline.auth.Model
import com.lightningtow.gridline.auth.guardValidSpotifyApi
import com.lightningtow.gridline.data.PlaylistsHolder
import com.lightningtow.gridline.player.Player
import com.lightningtow.gridline.player.Player.spotifyAppRemote
import com.lightningtow.gridline.ui.components.BottomNavigationBar
import com.lightningtow.gridline.ui.components.NavHostContainer
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
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
        spotifyAppRemote?.playerApi?.subscribeToPlayerContext()?.setEventCallback { playerContext ->
            Player.currentPlayerContext.value = playerContext
        }
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback { playerState ->


                Player.currentPlayerState.value = playerState
                Player.currentPos.value = playerState.playbackPosition

                // todo //////////////////// THIS CAUSES INFINITE LOOP ON STARTUP WHEN 1H REFRESH HAPPENS
//            Player.coverUri = playerState.track.imageUri

            scope.launch {
                try {
                    val api = Model.credentialStore.getSpotifyClientPkceApi()!!
//                    Log.e("within coroutine", tempPlayerStateFILLME?.track!!.album.uri.toString())
                    val albumUri = Player.currentPlayerState.value!!.track.album.uri.toString()
                    Player.currentTrackCover.value = (api.albums.getAlbum(album = albumUri)!!.images.firstOrNull()?.url)
                } catch (e: NullPointerException) {
                    Log.e("Player.coverUri", "failed to get album art")
//                toasty( "failed to get album art")
                }
            }
                // todo //////////////////// THIS CAUSES INFINITE LOOP ON STARTUP WHEN 1H REFRESH HAPPENS


//                spotifyAppRemote?.imagesApi?.getImage(Player.coverUri.value)
//                    ?.setResultCallback { result ->  Player.bitmap.value = result }
            }?.setErrorCallback { throwable ->
                Log.e(
                    "error connecting to appremote", "message: $throwable"
                )
            }
    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }

    }

    // https://stackoverflow.com/questions/4414171/how-to-detect-when-an-android-app-goes-to-the-background-and-come-back-to-the-fo/44461605#44461605
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
    override fun onStart() {
        super.onStart()
        loadPlaylists()
//        fun tart() {
        val connectionParams = ConnectionParams.Builder(clientId).setRedirectUri(redirectUri).showAuthView(true).build()


        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                Player.spotifyAppRemote = appRemote
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
                    })
            }
        }
    }

}
