package com.lightningtow.gridline.grid

import android.util.Log
import com.adamratzman.spotify.models.Playable
import com.lightningtow.gridline.data.TrackHolder1
import com.lightningtow.gridline.data.TrackHolder2
import com.lightningtow.gridline.data.TrackHolder3
import com.lightningtow.gridline.utils.Constants


const val logging: Boolean = true

var getcount = 0


// ALL OF THESE DO THE SAME THING, TREAT ARGS IN THE SAME ORDER
// todo actually test GetDiffByPlayableList

// take everything in H1, remove it if its also in H2, then put the rest in H3
// for example: to add H1 to H2 without duplicating:
// baselist = H1, removeTheseTracks = H2, then add H3 to H2

// or generally, baselist is bigger, removeTheseTracks is smaller


fun GetDiffByURI(
    baselist: String,
    removeTheseTracks: String,
    wipeHoldersAfter: Boolean = true
) {

    fun callback() {
        if (getcount == 1) {
            Log.e("GetDiff callback", "subtracting")

            // everything in H1 minus stuff that's also in H2
            TrackHolder3.contents = TrackHolder1.contents.filterNot { it in TrackHolder2.contents }
            if(logging)   for (i in TrackHolder3.contents) { i.asTrack?.name?.let { Log.e("getDiff", it) } }
            getcount = 0


        } else getcount += 1
    } // end callback


    PlaylistGetter.getPlaylistByURI(URI = baselist, holder = 1, callback = { callback() })
    PlaylistGetter.getPlaylistByURI(URI= removeTheseTracks, holder = 2, callback = { callback() })
}



fun GetDiffByPlayableList(
    baselist: List<Playable>,
    removeTheseTracks: List<Playable>
) {
    TrackHolder3.contents = baselist.filterNot { it in removeTheseTracks }
    if(logging)   for (i in TrackHolder3.contents) { i.asTrack?.name?.let { Log.e("getDiff", it) } }
}