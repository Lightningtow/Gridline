package com.lightningtow.gridline.notifications

import android.util.Log
import com.adamratzman.spotify.notifications.AbstractSpotifyBroadcastReceiver
import com.adamratzman.spotify.notifications.SpotifyMetadataChangedData
import com.adamratzman.spotify.notifications.SpotifyPlaybackStateChangedData
import com.adamratzman.spotify.notifications.SpotifyQueueChangedData
import com.lightningtow.gridline.getAlbumArt
import com.lightningtow.gridline.ui.home.Broadcasts

class SpotifyBroadcastReceiver(val activity: Broadcasts) : AbstractSpotifyBroadcastReceiver() {
    override fun onMetadataChanged(data: SpotifyMetadataChangedData) {
        activity.broadcasts += data
        Log.e("broadcast", "metadata changed: $data")

    }

    override fun onPlaybackStateChanged(data: SpotifyPlaybackStateChangedData) {
        getAlbumArt()
        activity.broadcasts += data
        Log.e("broadcast", "playback state changed: $data")

    }

    override fun onQueueChanged(data: SpotifyQueueChangedData) {
        activity.broadcasts += data
        Log.e("broadcast", "queue changed: $data")
    }
}
