package com.lightningtow.gridline

import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.lightningtow.gridline.GridlineApplication.Companion.ApplicationContext
import com.lightningtow.gridline.auth.Model
import com.lightningtow.gridline.auth.guardValidSpotifyApi
import com.lightningtow.gridline.data.API_State
import com.lightningtow.gridline.data.API_State.OFFLINE
import com.lightningtow.gridline.data.PlaylistsHolder
import com.lightningtow.gridline.data.API_State.currentPlayerContext
import com.lightningtow.gridline.data.API_State.currentPlayerState
import com.lightningtow.gridline.data.API_State.currentPos
import com.lightningtow.gridline.data.API_State.currentTrackCover
import com.lightningtow.gridline.data.API_State.kotlinApi
import com.lightningtow.gridline.data.API_State.spotifyAppRemote
import com.lightningtow.gridline.ui.components.BottomNavigationBar
import com.lightningtow.gridline.ui.components.NavHostContainer
import com.lightningtow.gridline.ui.components.downloadShortcutData
import com.lightningtow.gridline.ui.home.getQueue
//import com.lightningtow.gridline.ui.components.getStuff
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.utils.Constants
import com.lightningtow.gridline.utils.coroutineExceptionHandler
import com.lightningtow.gridline.utils.getAlbumArt
import com.lightningtow.gridline.utils.toasty
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private fun loadPlaylists() =
        scope.launch(coroutineExceptionHandler) { // Is invoked in UI context with Activity's scope as a parent

            guardValidSpotifyApi(classBackTo = MainActivity::class.java) { api ->
                PlaylistsHolder.lists = api.playlists.getClientPlaylists().getAllItemsNotNull()
            }
            PlaylistsHolder.loading = false
            Log.i("startup", "loaded playlists")

    }

    private val clientId: String = BuildConfig.SPOTIFY_CLIENT_ID
    private val redirectUri: String = BuildConfig.SPOTIFY_REDIRECT_URI_PKCE


    //    private var spotifyAppRemote: SpotifyAppRemote? = null
//    private fun connected() {
//        Log.e("startup", "connected")
//
//        connectToSDK()
//    }

    override fun onStop() {
        Log.e("MainActivity", "onStop")

        super.onStop() // todo is this wise?
//        spotifyAppRemote?.let {
//            Log.e("MainActivity", "Disconnecting from appRemote")
//            SpotifyAppRemote.disconnect(it)
//        }
        // todo if you dont disconnect and mean to actually shut the app, not good
    }
    // https://stackoverflow.com/questions/4414171/how-to-detect-when-an-android-app-goes-to-the-background-and-come-back-to-the-fo/44461605#44461605
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)

    override fun onStart() {
        // start of main activity, means this is called every time app resumed
        Log.e("startup", "onStart")


        super.onStart()
//        fun tart() {
        Log.w("MainActivity.onStart", "spotifyAppRemote:  ${spotifyAppRemote.toString()}")
        Log.w("MainActivity.onStart", "spotifyAppRemote connected:  ${spotifyAppRemote?.isConnected}")

        if (spotifyAppRemote == null || !(spotifyAppRemote!!.isConnected)) {

            tryConnectToAppRemote()
        } else Log.e("startup", "-----skipped connecting to appremote")
//        Log.e("startup", "downloading shortcut data")
        downloadShortcutData()
        getAlbumArt()
        getQueue()
        if (!API_State.OFFLINE) {
//            Log.e("startup", "loading playlists")
            loadPlaylists()
        }


        setContent {
            val context = LocalContext.current
//            toasty(context, "downloaded shortcut data")
            GridlineTheme {
                // remember navController so it does not
                // get recreated on recomposition
                val navController = rememberNavController()
                // Scaffold Component
                Scaffold( // Bottom navigation // Navhost: where screens are placed
                    bottomBar = { BottomNavigationBar(navController = navController)
                    }, content = { padding -> NavHostContainer(navController = navController, padding = padding) }) }
        }
    }

    fun tryConnectToAppRemote() {
        Log.e("startup", "tryConnectToAppRemote")
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        } // disconnect so you don't accidentally get two instances

        val connectionParams = ConnectionParams.Builder(clientId).setRedirectUri(redirectUri).showAuthView(true).build()


//    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
//    SpotifyAppRemote.connect(scope, connectionParams, object : Connector.ConnectionListener {

            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.e("startup", "onConnected")
                // Now you can start interacting with App Remote
//                connected()
                // todo this is only for app_remote

                try {
                    guardValidSpotifyApi(classBackTo = MainActivity::class.java) { api ->
                        kotlinApi = Model.credentialStore.getSpotifyClientPkceApi()!! // todo uncomment

                    }
                    Log.i("startup", "kotlinAPI initialized")
                }
                catch(ex: Exception) {
                    OFFLINE = true
                    Log.e("startup", "error initializing kotlinAPI: $ex")
                }

                Log.i("startup", "calling connectToSDK")
                guardValidSpotifyApi(classBackTo = MainActivity::class.java) { api ->
                    subscribeToAppRemote()

                    getAlbumArt("tryConnectToAppRemote") // tryConnectToAppRemote
                }
            }
            override fun onFailure(throwable: Throwable) {
                // todo this is only for app_remote
                Log.e("startup", "onFailure")

                Log.e("startup", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })
    }
    fun subscribeToAppRemote() {
        Log.i("startup", "connecting to SDK")
        // todo todooooooo


        Log.i("startup", "getting playerContext")
        spotifyAppRemote?.playerApi?.subscribeToPlayerContext()?.setEventCallback { playerContext ->
            currentPlayerContext.value = playerContext
            Log.i("startup", "subscribed to playerContext")
//            scope.launch(coroutineExceptionHandler) {
//                Log.e("startup", "playerContext")
//
//                try {
//                    val playlist = api.playlists.getPlaylist(playerContext.uri)
//                    contextLen.value = playlist!!.tracks.size
//
//                } catch (ex: Exception) { Log.e("MainActivity.connected", "error getting context length: $ex") }

//            }

        }?.setErrorCallback { throwable ->
            Log.e("startup", "playerContext error: $throwable")
        }

        Log.i("startup", "getting playerState")
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback { playerState ->
            Log.w("subscribeToAppRemote", "playerstate changed")

            currentPlayerState.value = playerState

            currentPos.value = playerState.playbackPosition

            guardValidSpotifyApi(classBackTo = MainActivity::class.java) { api ->
                getQueue()

                getAlbumArt("event callback for playerstate") // event callback for playerstate
            }
//                spotifyAppRemote?.imagesApi?.getImage(coverUri.value)
//                    ?.setResultCallback { result ->  bitmap.value = result }
        }?.setErrorCallback { throwable ->
//            toasty(context, "playerState failed: $throwable")
            Log.e("startup", "playerState error: $throwable")
        }

    }
}
