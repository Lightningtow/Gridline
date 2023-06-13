package com.lightningtow.gridline.player;

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.lightningtow.gridline.R
import com.lightningtow.gridline.auth.Model
import com.lightningtow.gridline.data.TrackHolder1.TrackHolder1Uri
import com.lightningtow.gridline.ui.theme.GridlineSliderColors
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.utils.Constants
import com.lightningtow.gridline.utils.StringToPlayableURI
import com.lightningtow.gridline.utils.toasty
import com.skydoves.landscapist.glide.GlideImage
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerContext
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Repeat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


object Player {

    var spotifyAppRemote: SpotifyAppRemote? = null
//    var tempPlayerStateFILLME: PlayerState? = null



    /**     Observable values. Runs the func when the variable gets changed  */
    private var togglePlayback: Boolean by Delegates.observable(false) { _, _, _ ->
        // every time this var changes, it toggles. True/false never matters
        try { val playing: Boolean = !(currentPlayerState.value?.isPaused!!)
            Log.e("toggling playback to", (!playing).toString())
            if (!playing) spotifyAppRemote?.playerApi?.resume()
            else if (playing) spotifyAppRemote?.playerApi?.pause()
            else Log.e("Player.togglePlayback", "invalid playing value: $playing")
        } catch(ex: Exception) { Log.e("Player.togglePlayback", "error: $ex") }
    }

    private var toggleShuffle: Boolean by Delegates.observable(false) { _, _, _ ->
        try { val shuffle: Boolean = currentPlayerState.value!!.playbackOptions.isShuffling
            Log.e("shuffle:", (!shuffle).toString())
            if (shuffle) spotifyAppRemote?.playerApi?.setShuffle(false)
            else if (!shuffle) spotifyAppRemote?.playerApi?.setShuffle(true) // ignore the warning that it's always true
            else Log.e("Player.toggleShuffle", "invalid isShuffling value: $shuffle")
        } catch (ex: Exception) { Log.e("Player.shuffle", "error: $ex") }
    }

    private var toggleRepeat: Boolean by Delegates.observable(false) { _, _, _ ->
        try { val repeat: Int = currentPlayerState.value!!.playbackOptions.repeatMode
            Log.e("repeat is currently", (repeat).toString())
            when (repeat) {
                Repeat.OFF -> spotifyAppRemote?.playerApi?.setRepeat(Repeat.ALL)
                Repeat.ALL -> spotifyAppRemote?.playerApi?.setRepeat(Repeat.ONE)
                Repeat.ONE -> spotifyAppRemote?.playerApi?.setRepeat(Repeat.OFF)
                else -> Log.e("Player.toggleRepeat", "invalid repeatMode value: $repeat") }
        } catch (ex: Exception) { Log.e("Player.toggleRepeat", "error: $ex") }
    }

    private enum class SkipVals { FORWARD, BACK }
    private var skipTrack: SkipVals? by Delegates.observable(null) { _, _, _ ->
//        Log.e("Player.skipTrack", "skipping " + skipTrack.toString())
        try { when (skipTrack) {
                SkipVals.FORWARD -> spotifyAppRemote?.playerApi?.skipNext()
                SkipVals.BACK -> spotifyAppRemote?.playerApi?.skipPrevious()
                else -> Log.e("Player.skipTrack", "ERROR: updateSkip called when skipTo == null") }
        } catch (ex: Exception) { Log.e("Player.skipTrack", "error: $ex") }
    }

    var queueMe: String? by Delegates.observable(null) { _, _, _ ->
        Log.e("Player.queueMe", queueMe.toString())
        try {
            spotifyAppRemote!!.playerApi.queue(queueMe)
//            toasty("test")
//            else -> Log.e("Player.skipTrack", "ERROR: updateSkip called when skipTo == null") }
        } catch (ex: Exception) { Log.e("Player.queueMe", "error: $ex") }
    }

    /**     never update these manually  */
    val currentPlayerState: MutableState<PlayerState?> = mutableStateOf(null)
    val currentPlayerContext: MutableState<PlayerContext?> = mutableStateOf(null)

    val currentTrackCover: MutableState<Any?> = mutableStateOf(null)
    val currentPos: MutableState<Long> = mutableStateOf(0)
    /**     never update these manually  */



    private fun quickAdd(
        context: Context,
        trackUri: String?,
        playlistUri: String,
        trackDisplayName: String?,
        playlistDisplayname: String?
    ) {
        if (trackUri == null) {
            toasty(context, "track is null")
            Log.e("error in quickAdd", "track is null")  // log errors
            return
        }
        var trackDisplayName = trackDisplayName
        if (trackDisplayName == null) trackDisplayName = "trackname"
        var playlistDisplayname = playlistDisplayname
        if (playlistDisplayname == null) playlistDisplayname = "playlistname"
        try {
            val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
            scope.launch {
                val api = Model.credentialStore.getSpotifyClientPkceApi()!!
                val playable = StringToPlayableURI(trackUri)

                // remove it first, to ensure no duplicates
                api.playlists.removePlayableFromClientPlaylist(playlist = playlistUri, playable = playable)

                api.playlists.addPlayableToClientPlaylist(playlist = playlistUri, playable = playable)
                toasty(context, "$trackDisplayName added to $playlistDisplayname")
            }
        } catch (ex: Exception) {
            toasty(context, "track could not be added")
            ex.message?.let { Log.e("error in quickAdd", it) } // log errors
        }
    }


    private var bigButtonOpacity: MutableState<Float> = mutableStateOf(0f)
    @Composable
    private fun BigButton() {
        /**
         * cover image with skip forward/back buttons on it
         */
        Box(
            modifier = Modifier
//            .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Box( // cover image
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
//            val maxHeight = this.maxHeight
//                if(image != null) {

                GlideImage(
                    currentTrackCover.value,
                    modifier = Modifier // 'modifier' uses argument, 'Modifier' creates new modifier
                        .size(372.dp),
                    contentDescription = null,
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {

                Button( // skip back
                    modifier = Modifier
                        .alpha(bigButtonOpacity.value)
                        .fillMaxHeight(fraction = .6f)
                        .fillMaxWidth(fraction = .4f),
                    onClick = { skipTrack = SkipVals.BACK },
                ){}

                Button( // skip forward
                    modifier = Modifier
                        .alpha(bigButtonOpacity.value)
                        .fillMaxHeight(fraction = .6f)
                        .fillMaxWidth(fraction = 1f),
                    onClick = { skipTrack = SkipVals.FORWARD }
                ){}
            }
        }
    }

    @Composable
    fun PlayerPage() {
        val context = LocalContext.current
        GridlineTheme() {
            Column() {
//                Column() { // this starts at the same height as the
                val uri: String? = currentPlayerContext.value?.uri
                Column(
                    modifier = Modifier.clickable {
                        if(currentPlayerContext.value?.type == "playlist" && uri != null ) {
                            Log.e("deeplink", "deeplinking to $uri")
                            toasty(context, "Opening " + currentPlayerContext.value?.title)

// todo need to overhaul restructure trackview backend
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                            browserIntent.putExtra(Intent.EXTRA_REFERRER,
                                Uri.parse("android-app://" + context.packageName))
                            ContextCompat.startActivity(context, browserIntent, null)

                        }
                    }
                ) {
                    Text("type: " + currentPlayerContext.value?.type)
                    Text(currentPlayerContext.value?.title ?: "default")
                    Text(currentPlayerContext.value?.subtitle ?: "default")
                }


                Spacer(Modifier.padding(46.dp))
                UtilRow()
                    BigButton()
//                }
//                Column( // this starts at the same height as the button
//                    modifier = Modifier
////                .padding(4.dp)
//                        .fillMaxSize()
//                                    .weight(0.5f, false) // anchor to bottom
//                    ,
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.SpaceBetween // so bottom bar anchored to bottom
//                    // https://stackoverflow.com/questions/70904979/how-align-to-bottom-a-row-in-jetpack-compose
//                ) {
//                    Column(
////                        modifier = Modifier
////                            .weight(0.1f, true) // anchor to bottom
//                    ) {
//                        Spacer(Modifier.padding(20.dp))
//                Spacer(Modifier.padding(170.dp))
//                    Spacer(Modifier.padding(270.dp))
                        BottomPart()
//                    }
//                }
            }
        }
    }

    @Composable
    private fun PlayerSlider(value: Float = 42f) {
        // todo https://stackoverflow.com/questions/66386039/jetpack-compose-react-to-slider-changed-value
//        if (episodeDuration != null) {
//        Log.e("PlayerSlider", "generating player slider")
//            var heldPos: Long by remember { mutableStateOf(42) }
//            var heldPos: MutableState<Long> = mutableStateOf(42)
        var heldPos: Float = 42f
        val viewModel by remember { mutableStateOf(10f) }

        Column(Modifier.fillMaxWidth()) {
            var sliderValueRaw by remember { mutableStateOf(32f) }

            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val isDragged by interactionSource.collectIsDraggedAsState()
            val isInteracting = isPressed || isDragged
            val sliderValue by remember {
                derivedStateOf {
                    if (isInteracting) {
                        sliderValueRaw
//                        heldPos
                    } else {
                        currentPos.value.toFloat()
//                    currentPlayerState.value?.playbackPosition?.toFloat() ?: 0f
                    }
                }
            }
            Slider(
                value = sliderValue,
                valueRange = 0f.rangeTo(currentPlayerState.value?.track?.duration?.toFloat() ?: 0f),
                onValueChange = {
//                    heldPos = it.toLong()
                    sliderValueRaw = it

                    heldPos = it         },
                onValueChangeFinished = {
//                        seekSlider()
                    spotifyAppRemote!!.playerApi.seekTo(heldPos.toLong())
                },
                colors = GridlineSliderColors(),
                interactionSource = interactionSource
            )
//            Text("isPressed: $isPressed | isDragged: $isDragged | sliderValue: $sliderValue | sliderValueRaw: $sliderValueRaw")

            Row(Modifier.fillMaxWidth()) {
//                    Text(text = "0s")
                Spacer(modifier = Modifier.weight(1f)) // todo this might be what I'm looking for
//                    Text("${episodeDuration.seconds}s")
//                    Text("42")

            }
//            }
        }

    }
    /*
//    @Composable
//    fun SliderDemo() {
//        // In this demo, ViewModel updates its progress periodically from 0f..1f
////        val viewModel by remember { mutableStateOf(SliderDemoViewModel()) }
//
//        val currentPos = currentPlayerState.value?.playbackPosition?.toFloat()
//
//        Column(
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
////            val currentPos by remember { mutableStateOf(currentPlayerState.value!!.playbackPosition.toFloat()) }
//
//            // local slider value state
//            var sliderValueRaw by remember { mutableStateOf(currentPos) }
//
//            // getting current interaction with slider - are we pressing or dragging?
//            val interactionSource = remember { MutableInteractionSource() }
//            val isPressed by interactionSource.collectIsPressedAsState()
//            val isDragged by interactionSource.collectIsDraggedAsState()
//            val isInteracting = isPressed || isDragged
//
//            // calculating actual slider value to display
//            // depending on wether we are interacting or not
//            // using either the local value, or the ViewModels / server one
//            val sliderValue by derivedStateOf {
//                if (isInteracting) {
//                    sliderValueRaw
//                } else {
//                    currentPos
////                    currentPlayerState.value!!.playbackPosition.toFloat()
//                }
//            }
//
//
//            sliderValue?.let {
//                Slider(
//                    value = it, // using calculated sliderValue here from above
//                    onValueChange = {
//                        sliderValueRaw = it
//                    },
//                    onValueChangeFinished = {
//                        sliderValueRaw?.let { it1 -> spotifyAppRemote!!.playerApi.seekTo(it1.toLong()) }
//        //                    viewModel.updateProgress(sliderValue)
//                    },
//                    interactionSource = interactionSource
//                )
//            }
//
//            // Debug interaction info
//            Text("isPressed: $isPressed | isDragged: $isDragged | currentPos $currentPos" )
//        }
//    } */
    @OptIn(ExperimentalFoundationApi::class) // for basicMarquee
    @Composable
    private fun BottomPart() {
        val context = LocalContext.current

        Column(
//            modifier = Modifier
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
            ) {
//                var duration = currentTrack.value.
//                currentPlayerState.value?.playbackPosition?.toFloat()?.let {
//                PlayerSlider()
//                PlayerSlider(currentPlayerState.value?.playbackPosition?.toFloat() ?: 0f)

                // current one \/
                PlayerSlider(currentPos.value.toFloat())


                Text( // trackname
                    currentPlayerState.value?.track?.name ?: "Loading...",
                    modifier = Modifier
                        .basicMarquee()
                        .clickable {
                            toasty(context, "get rekt")
                        },
                    style = MaterialTheme.typography.h5
                )

                Spacer(Modifier.padding(4.dp))


                Text( // artistname
                    currentPlayerState.value?.track?.artist?.name ?: "Loading...",
                    modifier = Modifier
                        .basicMarquee()
                        .clickable {
                            val goto = Uri.parse(currentPlayerState.value?.track!!.artist?.uri)
                            Log.e("deeplink", "deeplinking to $goto")
                            //                            toasty(context, "Opening " + track.value?.artist?.name)
                            val browserIntent = Intent(Intent.ACTION_VIEW, goto)
                            ContextCompat.startActivity(context, browserIntent, null)
                        },
                    style = MaterialTheme.typography.h6  // todo figure out better typography
                )


                Spacer(Modifier.padding(4.dp))

                Text( // albumname
                    currentPlayerState.value?.track?.album?.name ?: "Loading...",
                    modifier = Modifier
                        .basicMarquee()
                        .clickable {
                            val goto = Uri.parse(currentPlayerState.value?.track?.album?.uri)
                            Log.e("deeplink", "deeplinking to $goto")

                            val browserIntent = Intent(Intent.ACTION_VIEW, goto)
                            ContextCompat.startActivity(context, browserIntent, null)
                        },
                    style = MaterialTheme.typography.body2
                )

                Spacer(Modifier.padding(8.dp))
                PlaybackButtonRow()
                Spacer(Modifier.padding(8.dp))

            }
        }
    }

    @Composable
    private fun PlaybackButtonRow( modifier: Modifier = Modifier, playerButtonSize: Dp = 72.dp, ) {

        Row( // todo create a giant database of playback funcs
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {


            PlayerButton(
                icon = R.drawable.shuffle,
                color = (
                        if(currentPlayerState.value?.playbackOptions?.isShuffling == true)
                    GridlineTheme.colors.brand
                else GridlineTheme.colors.iconPrimary),


                OnClick = { toggleShuffle = !toggleShuffle },
                modifier = Modifier
                    .size(36.dp) // icon is bigger than the rest, so needs to be scaled down
            )

            PlayerButton(
                icon = R.drawable.baseline_skip_previous_24,
                OnClick = {
                    skipTrack = SkipVals.BACK
                })

            PlayerButton(
//            icon = playPauseIcon.value,
                icon = if (currentPlayerState.value?.isPaused == true) R.drawable.baseline_play_circle_24
                else R.drawable.baseline_pause_circle_24,
                OnClick = { togglePlayback = !togglePlayback },
                modifier = Modifier.size(playerButtonSize) // icon is bigger than the rest
            )

            PlayerButton(
                icon = R.drawable.baseline_skip_next_24,
                OnClick = { skipTrack = SkipVals.FORWARD })

            PlayerButton(
                icon = (if(currentPlayerState.value?.playbackOptions?.repeatMode == Repeat.ONE) R.drawable.round_repeat_one_24
                else R.drawable.round_repeat_24)

                , color = (
                        if(currentPlayerState.value?.playbackOptions?.repeatMode == Repeat.OFF)
                            GridlineTheme.colors.iconPrimary
                        else GridlineTheme.colors.brand), // if its not off, then its pink
                OnClick = { toggleRepeat = !toggleRepeat },
            )

        }
    }

    @Composable
    private fun UtilRow( modifier: Modifier = Modifier,
    ) {
        val context = LocalContext.current
        val spacing = Modifier.padding(8.dp)

        Row( // todo create a giant database of playback funcs
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {


            PlayerButton(
                icon = R.drawable.knife,
                OnClick = { // todo hardcoding
                            quickAdd(context,
                                trackUri = currentPlayerState.value?.track?.uri,
                                playlistUri = Constants.PURGELIST,
                                trackDisplayName = currentPlayerState.value?.track?.name,
                                playlistDisplayname = "Purgelist"
                            )
                    skipTrack = SkipVals.FORWARD // skip forward

                }
            )

            Spacer(spacing)

            PlayerButton(
                icon = R.drawable.round_bug_report_24,
                OnClick = {
                    if (bigButtonOpacity.value == 0.3f) bigButtonOpacity.value = 0f
                    else bigButtonOpacity.value = 0.3f}
            )

            Spacer(spacing)

            PlayerButton(
                icon = R.drawable.round_water_drop_24,
                OnClick = { // todo hardcoding
                    Log.e("cleanse", "adding " + currentPlayerState.value?.track?.uri)
                            quickAdd(context,
                                trackUri = currentPlayerState.value?.track?.uri,
                                playlistUri = Constants.ROADPURGE,
                                trackDisplayName = currentPlayerState.value?.track?.name,
                                playlistDisplayname = "Road Cleansing",

                                )
                    skipTrack = SkipVals.FORWARD // skip forward
                }
            )

        }
    }

    @Composable
    private fun PlayerButton(
        /**   a single button in the playerRow  */
        icon: Int,
        contentDescription: String = stringResource(R.string.FILLME),
        color: Color = GridlineTheme.colors.iconPrimary,
        modifier: Modifier = Modifier
            .size(48.dp) // todo make this less hardcoded
            .semantics { role = Role.Button }, OnClick: () -> Unit
    ) {
        IconButton(
            onClick = OnClick,
            content = {
                Icon(
                    ImageVector.vectorResource(id = icon),
                    modifier = modifier,
                    contentDescription = contentDescription,
                    tint = color
                )
            }
        )
    }
}
