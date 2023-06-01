package com.lightningtow.gridline.ui.components

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.adamratzman.spotify.models.Playable
import com.skydoves.landscapist.glide.GlideImage

/**
 * Required arguments:
 * deeplink_url, either to playlist or track.
 * ONE AND ONLY ONE of either imageModelArg or Track.
 * If both or neither are passed, simply uses the default picture argument
 *
 * Parameters:
 * imageModelArg: string to an image url
 * track: a track object that we can get the image from.
 *
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GridlineCoverImage(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,

    image_url: String? = null, // this is a string to an image url
    track: Playable? = null,

    deeplink_url: String,

// todo directly pass playlist uri
// but that would require async, fuuuck
) {
    val missing = "https://picsum.photos/300/300"

    var newImageModel: Any = missing

    if ((image_url == null && track == null) || (image_url != null && track != null)) {
        Log.e("ctrlfme", "invalid args passed for GridlineCoverImage, either neither or both passed")

    }

    // todo use the above validation to shortcut the below chunk


    newImageModel =
        image_url // if imageModelArg != null, return imageModelArg. Else, right hand expression
            ?: if (track != null) {
                if (track.asTrack != null) track.asTrack!!.album.images.firstOrNull()?.url ?: missing
                else if (track.asLocalTrack != null) missing // local tracks no bueno for fancy cover art
                else if (track.asPodcastEpisodeTrack != null) track.asPodcastEpisodeTrack!!.album.images.firstOrNull()?.url ?: missing
                else {
                    Log.e("ctrlfme", "playable somehow managed to be neither a track nor a localtrack nor an episode")
                    missing
                }
            }
            else {
                Log.e("ctrlfme", "invalid args passed for GridlineCoverImage, both are null")
                missing
            }



    val context = LocalContext.current
    GlideImage(
        modifier = modifier // 'modifier' uses argument, 'Modifier' creates new modifier
            .size(size)
            .combinedClickable(
                onClick = { },
                onLongClick = {
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(deeplink_url))
                    //  Uri.parse(track.externalUrls.first { it.name == "spotify" }.url) // what does this do

                    Log.e("deeplink", "deeplinking to $deeplink_url")
                    ContextCompat.startActivity(context, browserIntent, null)
                }
            ),
//            .clickable {
//                val browserIntent =
//                    Intent(Intent.ACTION_VIEW, Uri.parse(deeplink_url))
//                    //  Uri.parse(track.externalUrls.first { it.name == "spotify" }.url) // what does this do
//
//                Log.e("deeplink", "deeplinking to $deeplink_url")
//                ContextCompat.startActivity(context, browserIntent, null)
//            },

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
////            imageModel = (TrackHolder1.imageuri
//            imageModel = (TrackHolder1.actualist.images.firstOrNull()?.url
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
////                            Uri.parse(TrackHolder1.uri.toString())
//                            Uri.parse(TrackHolder1.actualist.uri.uri)
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