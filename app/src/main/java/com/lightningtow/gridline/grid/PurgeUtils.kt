package com.lightningtow.gridline.grid

import LoadingScreen
import android.os.Bundle
import android.util.Log
import com.lightningtow.gridline.data.Model
import kotlinx.coroutines.launch
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import com.lightningtow.gridline.data.SecTrackHolder
import com.lightningtow.gridline.grid.PlaylistGetter
import com.lightningtow.gridline.ui.home.loadingMessage
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.utils.Constants
import com.lightningtow.gridline.utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
//var purging: Boolean = false
//var message: String = "default"


// just removes purgelist from the playlist passed as an arg
//class PurgeUtils(){ // todo this only hosts backend shit
//    companion object {


//        fun

        private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)


        fun purgePlaylist(victim: String, purgelist: String = Constants.PURGELIST, regen_purgelist: Boolean = true, callback: () -> Unit) {

            scope.launch {

//                idk()
                val api = Model.credentialStore.getSpotifyClientPkceApi()!!

//                uri = uriARG
//                var uri = Constants.GIANT_TEST.toString()
//
//                val idk = "spotify:track:1xN1kZz11rolePhsZeLPJo"
//                val idk2: PlayableUri = PlayableUri.invoke("spotify:track:1xN1kZz11rolePhsZeLPJo")
//                var to_remove: MutableList<Playable> = mutableListOf() // idk2


                Log.e("hah", "cliiiiiiiiiicked")

                Log.e("coroutine?", Dispatchers.toString())

//            var id: String =


//                if (regen_purgelist) {
                    loadingMessage = "Getting Purgelist"
                    Log.e("purge", "getting purgelist")
                    PlaylistGetter.getPlaylistByURI(purgelist, 2)
//                }

                // lmaooo theres no need at all to load the victims, just remove

//                loadingMessage = "Getting Victim"
//                Log.e("purge", "getting victim")
//                PlaylistGetter.getPlaylistByURI(victim, 1)

                val uh: String? = null

                Log.e("purge", "removing")

                api.playlists.removePlayablesFromClientPlaylist(
                    playlist = victim,
                    snapshotId = uh,
                    playables = SecTrackHolder.contents.map { it.uri }.toTypedArray() )
                // todo recommended to have snapshot id



                Log.e("purge", "returing")
                callback()
//                return@launch

            }

        }

//    }
//}
//@Composable
//private fun PurgeMaster() {
//    GridlineTheme {
//
//        if (purging) // since `loading` is a mutableStateOf, PlaylistViewMaster automatically recomposes when loading changes
//            LoadingScreen(message = "Loading playlists...")
//        else
////            PlaylistViewPage(holder = holder, onPlaylistClick)
//
//
//    }
//
//}