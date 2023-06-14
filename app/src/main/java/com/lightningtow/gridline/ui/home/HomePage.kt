package com.lightningtow.gridline.ui.home

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.lightningtow.gridline.ui.components.KotlinShortcut
import com.lightningtow.gridline.ui.components.SHORTCUT_TYPE
import com.lightningtow.gridline.ui.components.masterListOfShortcuts
import com.lightningtow.gridline.ui.components.realList
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.utils.Constants
import com.skydoves.landscapist.glide.GlideImage

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
//                items = realList,
                items = masterListOfShortcuts,
                itemContent = { item ->
//                TrackRow(track = track, onTrackClick = {
                    ShortcutIcon(item = item)
                    Divider()

                }
            )
        }

    }




    @Composable
    public fun HomePageEntry() {
        GridlineTheme() {
            HomePage()

        }
    }
}

@Composable
fun ShortcutIcon(item: KotlinShortcut) {
    ShortcutIcon(
        accessUri = item.accessUri,
        coverUri = item.coverUri,
        type = item.type,
        displayname = item.displayname,
        )
}

@Composable
fun ShortcutIcon(
    accessUri: String,
    coverUri: String,
    type: SHORTCUT_TYPE,
    displayname: String,

//    cover: String? = null,
) {

//    ) {
    val context = LocalContext.current
//    val coveraaa by remember { mutableStateOf(realCover) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable() {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(accessUri))
            browserIntent.putExtra(
                Intent.EXTRA_REFERRER,
                Uri.parse("android-app://" + context.packageName)
            )

            //  Uri.parse(track.externalUrls.first { it.name == "spotify" }.url) // what does this do
            Log.e("deeplink", "deeplinking to $accessUri")
            ContextCompat.startActivity(context, browserIntent, null)
        }
    ) {


//        val cover: MutableState<Any?> = mutableStateOf(null)
//        val test = getStuff()
//        Log.e("ctrlfme", "displaying $coveraaa")
        GlideImage(
//            imageModel = getCover(uri, type) ?: Constants.DEFAULT_MISSING,
            imageModel = coverUri ?: Constants.DEFAULT_MISSING,
            modifier = Modifier
                .size(150.dp)
        )
        Text(displayname)
    }
}
