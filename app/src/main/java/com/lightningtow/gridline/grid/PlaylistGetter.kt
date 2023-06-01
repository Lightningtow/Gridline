package com.lightningtow.gridline.grid

import android.util.Log
import com.adamratzman.spotify.models.Playable
import com.adamratzman.spotify.models.PlayableUri
import com.adamratzman.spotify.utils.Market
import com.lightningtow.gridline.auth.Model
import com.lightningtow.gridline.data.TrackHolder2
import com.lightningtow.gridline.data.TrackHolder1
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PlaylistGetter() {
    //} : ViewModel() {
    companion object { // basically static class
        // upload new playlist to spotify
        fun upload() = runBlocking {
            launch {

                val api = Model.credentialStore.getSpotifyClientPkceApi()!!

                api.playlists.removeAllClientPlaylistPlayables(TrackHolder1.uri)

                val newlist: MutableList<PlayableUri> =
                    mutableListOf(); //PlaylistHolder.templist.toTypedArray()

                for (item in TrackHolder1.templist) {
                    if (item.asLocalTrack != null) continue;  // todo let it keep local tracks
                    newlist.add(item.uri)
                }
                api.playlists.addPlayablesToClientPlaylist(
                    playlist = TrackHolder1.uri,
                    *newlist.toTypedArray()
                ) // todo prevent trying to uploading to unowned playlist, causes crash
//                val toast = Toast.makeText(context, "sometext", Toast.LENGTH_SHORT)
//                toast.show()
            }
        }


        private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

        fun getPlaylistByURI(URI: String, holder: Int, callback: () -> Unit = {}) {
            scope.launch {
//        = runBlocking { launch {// this gets local tracks too

                Log.e("ctrlfme", "getting playlist $URI")
                val api = Model.credentialStore.getSpotifyClientPkceApi()!!

//                val contents = playlistTrackToPlayable(
////                    val contents = playlistTrackToTrack(
//
//                        api.playlists.getPlaylistTracks(URI).getAllItemsNotNull()
//                );
//                val contents =  (api.playlists.getPlaylistTracks(URI).getAllItemsNotNull().map { it.track!! })

//                val deets = api.playlists.getPlaylist(URI)
//                PlaylistHolder.playlistName = deets!!.name

//                PlaylistHolder.contents = contents;
//                PlaylistHolder.templist = contents as MutableList<Track>;
                val mutlist = (api.playlists.getPlaylistTracks(URI).getAllItemsNotNull()
                    .map { it.track!! }) as MutableList<Playable>;

                val realPlaylist = api.playlists.getPlaylist(URI, Market.US)!!
                if (holder == 1) {
                    TrackHolder1.templist = mutlist
                    TrackHolder1.contents = mutlist // todo wildly inefficient
                    TrackHolder1.actualist = realPlaylist
                    TrackHolder1.playlistName = realPlaylist.name

                } else if (holder == 2) {
                    TrackHolder2.contents = mutlist
//                    TrackHolder2.actualist = api.playlists.getPlaylist(URI, Market.US)!!

                } else {
                    while (true) {
                        Log.e(
                            "invalid int passed for 'holder' in getPlaylistByURI",
                            "haha wheeeee"
                        )
                    }
                }
                Log.e("ctrlfme", "finished getting playlist")
                callback();

            }
        }


    }
}

