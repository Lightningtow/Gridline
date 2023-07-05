package com.lightningtow.gridline.player;

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsActions.OnClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.lightningtow.gridline.MainActivity
import com.lightningtow.gridline.R
import com.lightningtow.gridline.auth.Model
import com.lightningtow.gridline.auth.guardValidSpotifyApi
import com.lightningtow.gridline.data.API_State
import com.lightningtow.gridline.data.API_State.contextLen
import com.lightningtow.gridline.data.API_State.currentPlayerContext
import com.lightningtow.gridline.data.API_State.currentPlayerState
import com.lightningtow.gridline.data.API_State.currentPos
import com.lightningtow.gridline.data.API_State.currentTrackCover
import com.lightningtow.gridline.data.API_State.spotifyAppRemote
import com.lightningtow.gridline.data.PlaylistsHolder
import com.lightningtow.gridline.data.TrackHolder1.TrackHolder1Uri
import com.lightningtow.gridline.ui.components.FavoriteStar
import com.lightningtow.gridline.ui.components.SHORTCUT_TYPE
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
import kotlinx.serialization.json.JsonNull.content
import kotlin.properties.Delegates


object Player {

//    var tempPlayerStateFILLME: PlayerState? = null



    /**     Observable values. Runs the func when the variable gets changed  */
    private var togglePlayback: Boolean by Delegates.observable(false) { _, _, _ ->
        // every time this var changes, it toggles. True/false never matters
        try { val playing: Boolean = !(currentPlayerState.value?.isPaused!!)
            Log.e("Player.togglePlayback", "toggling playback to " + (!playing).toString())
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
                Repeat.OFF -> spotifyAppRemote!!.playerApi.setRepeat(Repeat.ALL)
                Repeat.ALL -> spotifyAppRemote!!.playerApi.setRepeat(Repeat.ONE)
                Repeat.ONE -> spotifyAppRemote!!.playerApi.setRepeat(Repeat.OFF)
                else -> Log.e("Player.toggleRepeat", "invalid repeatMode value: $repeat") }
        } catch (ex: Exception) { Log.e("Player.toggleRepeat", "error: $ex") }
    }

    private var randSkip: Boolean by Delegates.observable(false) { _, _, _ ->
        try {
// todo this will ignore stuff in the queue
            val location = currentPlayerContext.value!!.uri
//            val loc2 =
            val skipIndex = (0 until contextLen.value!!).random()
            Log.e("Player.randSkip", "skipping to index $skipIndex")
            spotifyAppRemote!!.playerApi.skipToIndex(location, skipIndex)
//            spotifyAppRemote!!.playerApi.skipNext()
        } catch (ex: Exception) { Log.e("Player.randSkip", "error: $ex") }

    }

    private enum class SkipVals { FORWARD, BACK }
    private var skipTrack: SkipVals? by Delegates.observable(null) { _, _, _ ->
//        Log.e("Player.skipTrack", "skipping " + skipTrack.toString())
        if (randSkipEnabled.value && skipTrack == SkipVals.FORWARD) {
            randSkip = true
        } else { try {
                when (skipTrack) {
                    SkipVals.FORWARD -> spotifyAppRemote!!.playerApi.skipNext()
                    SkipVals.BACK -> spotifyAppRemote!!.playerApi.skipPrevious()
                    else -> Log.e("Player.skipTrack", "ERROR: in updateSkip, skipTo == null")
                } } catch (ex: Exception) { Log.e("Player.skipTrack", "error: $ex") }
        }
    }

    var queueMe: String? by Delegates.observable(null) { _, _, _ ->
        Log.e("Player.queueMe", queueMe.toString())
        try {
            spotifyAppRemote!!.playerApi.queue(queueMe)
//            toasty("test")
//            else -> Log.e("Player.skipTrack", "ERROR: updateSkip called when skipTo == null") }
        } catch (ex: Exception) { Log.e("Player.queueMe", "error: $ex") }
    }

//    var contextLen: Int? by Delegates.observable(null) { _, _, _ ->
//        Log.e("Player.contextLen", contextLen.toString())
//        try {
//            spotifyAppRemote!!.playerApi.queue(queueMe)
////            toasty("test")
////            else -> Log.e("Player.skipTrack", "ERROR: updateSkip called when skipTo == null") }
//        } catch (ex: Exception) { Log.e("Player.queueMe", "error: $ex") }
//    }


    private var debugging: MutableState<Boolean> = mutableStateOf(false)
    private var randSkipEnabled: MutableState<Boolean> = mutableStateOf(false)

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
//                val api = Model.credentialStore.getSpotifyClientPkceApi()!!
//                val api = API_State.kotlinApi

                val playable = StringToPlayableURI(trackUri)

                // remove it first, to ensure no duplicates
                API_State.kotlinApi.playlists.removePlayableFromClientPlaylist(playlist = playlistUri, playable = playable)

                API_State.kotlinApi.playlists.addPlayableToClientPlaylist(playlist = playlistUri, playable = playable)
                toasty(context, "$trackDisplayName added to $playlistDisplayname")
            }
        } catch (ex: Exception) {
            toasty(context, "track could not be added")
            ex.message?.let { Log.e("error in quickAdd", it) } // log errors
        }
    }


//    private var bigButtonOpacity: MutableState<Float> = mutableStateOf(0f)
    @Composable
    private fun BigButton() {
        /**
         * cover image with skip forward/back buttons on it
         */
        val shown = .3f
        val hidden = 0f
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
                        .alpha(if (debugging.value) shown else hidden)
                        .fillMaxHeight(fraction = .6f)
                        .fillMaxWidth(fraction = .4f),
                    onClick = { skipTrack = SkipVals.BACK },
                ){}

                Button( // skip forward
                    modifier = Modifier
                        .alpha(if (debugging.value) shown else hidden)
                        .fillMaxHeight(fraction = .6f)
                        .fillMaxWidth(fraction = 1f)
//                        .combinedClickable(enabled = true,
//                            onLongClick = { randSkip = true },
//                            {val test = "Hi"}
//                        ),
//                        .pointerInput(Unit) {
//                            detectTapGestures(
//                                onLongPress = { randSkip = true })
//                        },
                    , onClick = { skipTrack = SkipVals.FORWARD }

                ){}
            }
        }
    }

    @Composable
    private fun ContextData() {
        val context = LocalContext.current

        val uri: String? = currentPlayerContext.value?.uri
        Column(
            modifier = Modifier.clickable {
                if(currentPlayerContext.value?.type == "playlist" && uri != null ) {
                    Log.e("deeplink", "deeplinking to $uri")
                    toasty(context, "Opening " + currentPlayerContext.value?.title)

                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    browserIntent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://" + context.packageName))
                    ContextCompat.startActivity(context, browserIntent, null)

                }
            }
        ) {
            Text("type: " + currentPlayerContext.value?.type)
            Text(currentPlayerContext.value?.title ?: "default")
            Text(currentPlayerContext.value?.subtitle ?: "default")
        }
    }

    @Composable
    fun PlayerPage() {
        GridlineTheme() {
            Column( // right column
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                DebugInfo()
            }

            Column() { // left (main) column

                ContextData()

                Spacer(Modifier.padding(46.dp))

                UtilRow()

                BigButton()

                BottomPart()
            }
        }
    }

    @Composable
    private fun DebugInfo() {
        if (!debugging.value) return
        Column() {
            Text("rand skip enabled: " + randSkipEnabled.value.toString())
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
                            //   toasty(context, "Opening " + track.value?.artist?.name)
                            val browserIntent = Intent(Intent.ACTION_VIEW, goto)
                            browserIntent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://" + context.packageName))

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
                            browserIntent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://" + context.packageName))
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
        /** playback controls below the slider */
        val context = LocalContext.current
        Row( // todo create a giant database of playback funcs
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {


            PlayerButton( // shuffle toggle
                icon = R.drawable.shuffle,
                color = (
                        if(currentPlayerState.value?.playbackOptions?.isShuffling == true)
                    GridlineTheme.colors.brand
                else GridlineTheme.colors.iconPrimary),


                OnClick = { toggleShuffle = !toggleShuffle },
                modifier = Modifier
                    .size(36.dp) // icon is bigger than the rest, so needs to be scaled down
            )

            PlayerButton( // skip back
                icon = R.drawable.baseline_skip_previous_24,
                OnClick = {
                    skipTrack = SkipVals.BACK
                })

            PlayerButton( // playback toggle
//            icon = playPauseIcon.value,
                icon = if (currentPlayerState.value?.isPaused == true) R.drawable.baseline_play_circle_24
                else R.drawable.baseline_pause_circle_24,
                OnClick = { togglePlayback = !togglePlayback },
                modifier = Modifier.size(playerButtonSize) // make icon bigger than the rest
            )

            PlayerButton( // skip forward
                icon = R.drawable.baseline_skip_next_24,
                color = if (randSkipEnabled.value) GridlineTheme.colors.brand else GridlineTheme.colors.iconPrimary,
                OnClick = { skipTrack = SkipVals.FORWARD },
                OnLongClick = {
                    Log.e("PlayerButton", "skip button long clicked")
                    randSkipEnabled.value = !randSkipEnabled.value
                    toasty(context, "Toggled randSkip")
                },
            )

            PlayerButton( // repeat toggle
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
    private fun UtilRow( modifier: Modifier = Modifier, ) {
        val context = LocalContext.current
        val spacing = Modifier.padding(8.dp)

        Row( // todo create a giant database of playback funcs
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {




            FavoriteStar(
                accessUri = currentPlayerState.value?.track?.uri ?: "NULL",
                coverUri =  currentTrackCover.value?.toString() ?: "NULL", // todo disable this if null
                type = SHORTCUT_TYPE.TRACK,
                displayname = currentPlayerState.value?.track?.name ?: "NULL"
            )
            PlayerButton (
                icon = R.drawable.baseline_skip_next_24,
                OnClick = {
                    randSkipEnabled.value = !randSkipEnabled.value
//                    randSkip = true
                    Log.e("Player.UtilRow", "long skipping")
                }

            )
            Spacer(spacing)

            PlayerButton(
                icon = R.drawable.knife,
                OnClick = { // todo hardcoding
                            quickAdd(context,
                                trackUri = currentPlayerState.value!!.track.uri,
                                playlistUri = Constants.PURGELIST,
                                trackDisplayName = currentPlayerState.value!!.track.name,
                                playlistDisplayname = "Purgelist"
                            )
                    skipTrack = SkipVals.FORWARD // skip forward

                }
            )

            Spacer(spacing)

            PlayerButton(
                icon = R.drawable.round_bug_report_24,
                OnClick = {
                    debugging.value = !debugging.value
                }
            )

            Spacer(spacing)

            PlayerButton(
                icon = R.drawable.round_water_drop_24,
                OnClick = { // todo hardcoding
//                    Log.e("Player.UtilRow", "adding " + currentPlayerState.value?.track?.uri + " to roadCleansing")
                    quickAdd(
                        context,
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

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun PlayerButton(
        icon: Int,
        contentDescription: String = stringResource(R.string.FILLME),
        color: Color = GridlineTheme.colors.iconPrimary,
        modifier: Modifier = Modifier
            .size(48.dp) // todo make this less hardcoded
            .semantics { role = Role.Button },
        OnClick: () -> Unit,
        OnLongClick: () -> Unit = {}
    ) {
        /**   a single button in the playerRow  */

        Icon(
            ImageVector.vectorResource(id = icon),
            contentDescription = contentDescription,
            tint = color,
            modifier = modifier
                .combinedClickable(
                    onClick = {
//                        guardValidSpotifyApi(classBackTo = MainActivity::class.java) { api -> }
                        OnClick()
                              },
                    onLongClick = {
                        Log.e("ctrlfme", "long click")
                        OnLongClick()
                        Log.e("ctrlfme", "after long click")
                    }
                )
        )

    }
}
