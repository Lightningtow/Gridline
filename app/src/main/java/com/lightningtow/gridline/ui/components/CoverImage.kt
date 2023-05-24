package com.lightningtow.gridline.ui.components

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.adamratzman.spotify.models.Playable
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.utils.Market
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.lightningtow.gridline.data.Model
import com.lightningtow.gridline.data.TrackHolder
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.palette.BitmapPalette
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.system.exitProcess


@Composable
fun GridlineCoverImage(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    imageModelArg: Any? = null,
    track: Playable? = null,
    deeplink_url: String,

    // todo can imageModelArg be a string?
) {
    val missing = "https://picsum.photos/300/300"

    var newImageModel: Any = missing

    if ((imageModelArg == null && track == null) || (imageModelArg != null && track != null)) {
        Log.e("ctrlfme", "invalid args passed for GridlineCoverImage, either neither or both passed")

    }

    // todo use the above validation to shortcut the below chunk


    newImageModel =
        if (imageModelArg != null) { imageModelArg }

        else if (track != null) {
            if (track.asTrack != null) track.asTrack!!.album.images.firstOrNull()?.url ?: missing
            else if (track.asLocalTrack != null) missing // local tracks no bueno for fancy cover art
            else if (track.asPodcastEpisodeTrack != null) track.asPodcastEpisodeTrack!!.album.images.firstOrNull()?.url ?: missing
            else {
                Log.e("ctrlfme", "playable somehow managed to be neither a track nor a localtrack nor an episode")
                missing
            }
        }
        else {
            Log.e("ctrlfme", "invalid args passed for GridlineCoverImage, both args are null")
            missing
        }


    val context = LocalContext.current
    GlideImage(
        modifier = modifier // 'modifier' uses argument, 'Modifier' creates new modifier
            .size(size)
            .clickable {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(deeplink_url))
                    //  Uri.parse(track.externalUrls.first { it.name == "spotify" }.url) // what does this do

                Log.e("deeplink", "deeplinking to $deeplink_url")
                ContextCompat.startActivity(context, browserIntent, null)
            },

        imageModel = newImageModel,

        contentDescription = null,
        // I can't imagine blind people particularly appreciate cover art, seems best to not clog up their screenreaders with useless stuff


        )
}

/*
// backups
//// playlistView playlist image
//        GlideImage(
//            imageModel = (playlistItem.images.firstOrNull()?.url
//                ?: "https://picsum.photos/300/300"),
//            modifier = Modifier
//            .height(40.dp)
//            .width(40.dp)
//        )
//
//// trackView header
//        GlideImage(
////            imageModel = (TrackHolder.imageuri
//            imageModel = (TrackHolder.actualist.images.firstOrNull()?.url
//                ?: "https://picsum.photos/300/300"),
//            modifier = Modifier
//                .height(80.dp)
//                .width(80.dp)
//                .clickable {
//                    //                itemContent = { track ->
////                onNameClick = {
//                    val browserIntent =
//                        Intent(
//                            Intent.ACTION_VIEW,
////                            Uri.parse(TrackHolder.uri.toString())
//                            Uri.parse(TrackHolder.actualist.uri.uri)
////                          Uri.parse(track.externalUrls.first { it.name == "spotify" }.url)
//                        )
//                    startActivity(context, browserIntent, null)
//                },
//        )
//
//// trackView track art
//        GlideImage(
//            modifier = Modifier // image modifiers
//                .height(40.dp)
//                .width(40.dp)
//                .clickable {
//                    val browserIntent =
//                        Intent(
//                            Intent.ACTION_VIEW,
//                            Uri.parse(playable.asTrack!!.externalUrls.first { it.name == "spotify" }.url)
//                        )
//                    startActivity(context, browserIntent, null)
//                },
//            imageModel = (
//                    if (playable.asTrack != null) playable.asTrack!!.album.images.firstOrNull()?.url
//                        ?: "https://picsum.photos/300/300"
//                    else if (playable.asLocalTrack != null) "https://picsum.photos/300/300"
//                    else if (playable.asPodcastEpisodeTrack != null) playable.asPodcastEpisodeTrack!!.album.images.firstOrNull()?.url
//                        ?: "https://picsum.photos/300/300"
//                    else {
//                        Log.e("ctrlfme", "???")
//                        "???" // text =
//                    }),
//            contentDescription = null,
//
//            )
*/