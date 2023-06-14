package com.lightningtow.gridline.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lightningtow.gridline.ui.components.KotlinShortcut
import com.lightningtow.gridline.ui.components.SHORTCUT_TYPE
import com.lightningtow.gridline.ui.components.ShortcutIcon
import com.lightningtow.gridline.ui.components.realList
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.utils.Constants

object HomePage {

//var HomepageShortcutList: MutableList<KotlinShortcut>

    @Composable
    private fun HomePage() {
//        Text("hello world")
//        LazyVerticalGrid( //
        Log.e("homepage", realList.toString())
        LazyColumn(
//        modifier = Modifier
//        .padding(start = 8.dp)

        ) {

            items(
                items = realList,
                itemContent = { item ->
//                TrackRow(track = track, onTrackClick = {
                    ShortcutIcon(item = item)
                    Divider()

                }
            )
        }
//        Column( // todo make lazyGrid
//
//
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//            ShortcutIcon(
//                type = SHORTCUT_TYPE.PLAYLIST,
//                uri = Constants.ROADKILL,
////                cover = "https://i.scdn.co/image/ab67706c0000bebbce70dd919d2c0c6bf8e2bb8c",
//                displayname = "Roadkill"
//            )
////            ShortcutIcon(
////                type = SHORTCUT_TYPE.PLAYLIST,
////                uri = Constants.OMNI,
//////                cover = "https://i.scdn.co/image/ab67706c0000bebbce70dd919d2c0c6bf8e2bb8c",
////                displayname = "Omniscience"
////            )
//        }
    }




    @Composable
    public fun HomePageEntry() {
        GridlineTheme() {
            HomePage()

        }
    }
}