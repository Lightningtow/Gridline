package com.lightningtow.gridline.grid

//import com.lightningtow.gridline.utils.toast
import android.util.Log
import android.widget.Toast
import com.adamratzman.spotify.models.PlayableUri
import com.lightningtow.gridline.GridlineApplication.Companion.context
import com.lightningtow.gridline.data.Model
import com.lightningtow.gridline.data.TrackHolder
import com.lightningtow.gridline.utils.toast
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class ShuffleInPlace() {//} : BaseActivity() { //
    companion object { // basically static class



//        fun makeToast() {
//            toast(this,"You clicked ${track.name} - opening in spotify")
//
//        }
        fun shuffle() {
            TrackHolder.templist = TrackHolder.templist.toMutableList().also { it.shuffle() }

        }

        fun remove10() {

            Log.e("ctrlfme", TrackHolder.templist[1].asTrack!!.name)

            TrackHolder.templist =
                TrackHolder.templist.toMutableList().also { it.removeAt(1) }
//            PlaylistHolder.setTemplist(PlaylistHolder.getTemplist().toMutableList().also {it.removeAt(1)})
//            PlaylistHolder.templist = favourites.toMutableList().also { it.remove(item) }

//            myList.swapList(PlaylistHolder.getTemplist())

        }

    }
}