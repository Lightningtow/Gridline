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
import com.lightningtow.gridline.utils.dumpPlayerState
import com.lightningtow.gridline.utils.getAlbumArt
import com.lightningtow.gridline.utils.toasty
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
const val s = "startup"
fun tryConnectToKotlinAPI() {
    Log.e(s, "tryConnectToKotlinAPI")

    try {
//        guardValidSpotifyApi(classBackTo = MainActivity::class.java) { api ->
//        Log.
        kotlinApi = Model.credentialStore.getSpotifyClientPkceApi()!! // todo uncomment
//        kotlinApi = Model.credentialStore.getSpotifyCli
//        }
        Log.e(s, "kotlinAPI initialized")
        val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        scope.launch(coroutineExceptionHandler) {
            val drones = kotlinApi.tracks.getTrack("spotify:track:5JvspoRjPwqMmPV5anGYZj")
            Log.e(s, "testing KotlinAPI: should be Drones: ${drones?.asTrack?.name}")
            if (drones?.asTrack?.name == "Drones") Log.e(s,"KOTLIN API INITIALIZED SUCCESSFULLY")
        }
    } catch (ex: Exception) {
        OFFLINE = true
        Log.e(s, "error initializing kotlinAPI: $ex")
    }
}
fun subscribeToPlayerContext() {
    /** set the callback that runs every time the playercontext changes */
    val tag = "subscribeToPlayerContext"

    Log.e(s, tag)
    try {
        spotifyAppRemote!!.playerApi.subscribeToPlayerContext().setEventCallback { playerContext ->
            Log.w(tag, "playercontext changed callback")
            currentPlayerContext.value = playerContext

        }?.setErrorCallback { throwable ->
            Log.e(tag, "playerContext error: $throwable")
        }
        Log.i(tag, "subscribed to playerContext")

    } catch (ex: Exception) {
        Log.e(tag, "error subscribing to playerContext: $ex")
    }
}


class MainActivity : AppCompatActivity() {
    fun subscribeToPlayerState() {
        /** set the callback that runs every time the playerstate changes */
        val tag = "subscribeToPlayerState"


        Log.e(s, tag)
        try {
            spotifyAppRemote!!.playerApi.subscribeToPlayerState().setEventCallback { playerState ->
                Log.w(tag, "playerstate changed callback")

//                dumpPlayerState()
                currentPlayerState.value = playerState

//            currentPos.value = playerState.playbackPosition // this doesn't work

                guardValidSpotifyApi(classBackTo = MainActivity::class.java) { api ->
                    getQueue()
                    getAlbumArt("event callback for playerstate") // event callback for playerstate
                }

            }?.setErrorCallback { throwable ->
                Log.e(tag, "playerState error: $throwable")
            }
        } catch (ex: Exception) {
            Log.e(tag, "error subscribing to playerState: $ex")

        }
    }
    fun tryConnectToAppRemote() {
        Log.e(s, "tryConnectToAppRemote")
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        } // disconnect so you don't accidentally get two instances

        val connectionParams =
            ConnectionParams.Builder(clientId).setRedirectUri(redirectUri).showAuthView(true)
                .build()


//    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
//    SpotifyAppRemote.connect(scope, connectionParams, object : Connector.ConnectionListener {

            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.e(s, "onConnected")
                // Now you can start interacting with App Remote
//                connected()
                // todo this is only for app_remote
                Log.e(s, "CONNECTED TO APP REMOTE SUCCESSFULLY")


                guardValidSpotifyApi(classBackTo = MainActivity::class.java) { api ->
                    subscribeToPlayerContext() // this func only works if connected to app remote
                    subscribeToPlayerState()
                    getAlbumArt("tryConnectToAppRemote") // tryConnectToAppRemote
                }
            }

            override fun onFailure(throwable: Throwable) {
                // todo this is only for app_remote
                Log.e(s, "onFailure")

                Log.e(s, throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })
    }

    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private fun loadPlaylists() =
        scope.launch(coroutineExceptionHandler) { // Is invoked in UI context with Activity's scope as a parent

            guardValidSpotifyApi(classBackTo = MainActivity::class.java) { api ->
                PlaylistsHolder.lists = api.playlists.getClientPlaylists().getAllItemsNotNull()
            }
            PlaylistsHolder.loading = false
            Log.i(s, "loaded playlists")

        }

    private val clientId: String = BuildConfig.SPOTIFY_CLIENT_ID
    private val redirectUri: String = BuildConfig.SPOTIFY_REDIRECT_URI_PKCE


    //    private var spotifyAppRemote: SpotifyAppRemote? = null
//    private fun connected() {
//        Log.e(s, "connected")
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
        /** start of main activity, means this is called every time app brought to foreground from background */
        Log.e(s, "onStart")


        super.onStart()
//        fun tart() {
//        Log.e(s, "spotifyAppRemote:  ${spotifyAppRemote.toString()}")
        Log.e(s, "spotifyAppRemote connected:  ${spotifyAppRemote?.isConnected}")

        if (spotifyAppRemote == null || !(spotifyAppRemote!!.isConnected)) {
            Log.e(s, "appRemote is null or disconnected, trying to reconnect")
            tryConnectToAppRemote()

        } else Log.e(s, "-----skipped connecting to appremote")
//        Log.e(s, "downloading shortcut data")
        tryConnectToKotlinAPI()
//        if (kotlinApi == null)

        downloadShortcutData()
        getAlbumArt("MainActivity")
        getQueue()
        if (!API_State.OFFLINE) {
//            Log.e(s, "loading playlists")
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
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
                    },
                    content = { padding ->
                        NavHostContainer(
                            navController = navController,
                            padding = padding
                        )
                    })
            }
        }
    }




}



