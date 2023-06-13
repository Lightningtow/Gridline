package com.lightningtow.gridline.notifications

import com.adamratzman.spotify.notifications.AbstractSpotifyBroadcastReceiver
import com.adamratzman.spotify.notifications.SpotifyMetadataChangedData
import com.adamratzman.spotify.notifications.SpotifyPlaybackStateChangedData
import com.adamratzman.spotify.notifications.SpotifyQueueChangedData
import com.lightningtow.gridline.ui.home.Broadcasts

class SpotifyBroadcastReceiver(val activity: Broadcasts) : AbstractSpotifyBroadcastReceiver() {
    override fun onMetadataChanged(data: SpotifyMetadataChangedData) {
        activity.broadcasts += data
    }

    override fun onPlaybackStateChanged(data: SpotifyPlaybackStateChangedData) {
        activity.broadcasts += data
    }

    override fun onQueueChanged(data: SpotifyQueueChangedData) {
        activity.broadcasts += data
    }
}
