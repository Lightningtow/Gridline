package com.lightningtow.gridline.player;

//import android.support.v7.app.AppCompatActivity;

//import com.adamratzman.spotify.models.Track

//import com.lightningtow.gridline.data.PlayerState.pauseNoView
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ImageBitmap
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
import com.adamratzman.spotify.models.PlayableUri
import com.lightningtow.gridline.R
import com.lightningtow.gridline.auth.Model
import com.lightningtow.gridline.player.Player.bigButtonOpacity
import com.lightningtow.gridline.player.Player.currentTrack
import com.lightningtow.gridline.player.Player.isPlaying
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.utils.Constants
import com.lightningtow.gridline.utils.StringToPlayableURI
import com.lightningtow.gridline.utils.toasty
import com.skydoves.landscapist.glide.GlideImage
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.ImageUri
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


object Player {

    var spotifyAppRemote: SpotifyAppRemote? = null

    var tempPlayerStateFILLME: PlayerState? = null


    private enum class SkipVals {
        FORWARD, BACK
    }

    /** this var is only used to prompt updating the cover with coroutine scope */
//    var coverUri: MutableState<ImageUri?> = mutableStateOf(null) // nullable
    var coverUri: ImageUri? by Delegates.observable(null) { property, oldvalue, newvalue ->
        val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        scope.launch {
            try {
                val api =
                    Model.credentialStore.getSpotifyClientPkceApi()!! // todo this is nullpointer place
//                    Log.e("within coroutine", tempPlayerStateFILLME?.track!!.album.uri.toString())
                val albumUri = tempPlayerStateFILLME!!.track.album.uri.toString()
                cover.value = (api.albums.getAlbum(album = albumUri)!!.images.firstOrNull()?.url)
            } catch (e: NullPointerException) {
                Log.e("Player.coverUri", "failed to get album art")
//                toasty( "failed to get album art")
            }
        }
    }
    /**
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    scope.launch {
    val api = Model.credentialStore.getSpotifyClientPkceApi()!!
    Log.e("within coroutine", playerState.track.album.uri.toString())
    Player.cover.value = (api.albums.getAlbum(album = playerState.track.album.uri.toString())!!.images.firstOrNull()?.url)
    }
     */

    /**     Observable values. Runs the func when the variable gets changed  */
    private var togglePlayback: Boolean by Delegates.observable(false) { property, oldvalue, newvalue ->
        Log.e("toggling to", (!isPlaying.value).toString())
        if (isPlaying.value) spotifyAppRemote?.playerApi?.pause()
        else if (!isPlaying.value) spotifyAppRemote?.playerApi?.resume()
        else Log.e("uhhh", "readonlyplaying has some deep seated issues")
//        togglePlayback = false // DON'T TOGGLE THIS BACK MANUALLY
        // why does it automatically toggle back
        // oh it doesn't, it just triggers when true->false. More efficient that way anyways
    }
    private var skipTo: SkipVals? by Delegates.observable(null) { property, oldvalue, newvalue ->
        Log.e("Player.skipTo", skipTo.toString())
        if (newvalue != null) {
            when (skipTo) {
                SkipVals.FORWARD -> spotifyAppRemote?.playerApi?.skipNext()
                SkipVals.BACK -> spotifyAppRemote?.playerApi?.skipPrevious()
                else -> Log.e("Player.skipTo", "ERROR: updateSkip called when skipTo == null")
            }
        }
//        skipTo = null // this also breaks things
    }


    /**     NEVER update isPlaying manually  */
    var isPlaying: MutableState<Boolean> = mutableStateOf(false)

    var cover: MutableState<Any?> = mutableStateOf(null)

    var bigButtonOpacity: MutableState<Float> = mutableStateOf(0f)

//    private val defaultCoverUri: ImageUri = "ImageId{spotify:image:ab67616d0000b27380b68290d8a8cca20b341d20'}"

    //    var trackname:  MutableState<String> = mutableStateOf("default")
    var currentTrack: MutableState<Track?> = mutableStateOf(null)
    // todo combine variables?

//    var trackname: MutableState<String> = mutableStateOf("default")
//    var albumname: MutableState<String> = mutableStateOf("default")
//    var artistname: MutableState<String> = mutableStateOf("default")


//    var coverAny: MutableState<Any?> = mutableStateOf(null)
//    lateinit var sdkTrack: MutableState<com.spotify.protocol.types.Track>
//    lateinit var kotlinTrack: MutableState<com.adamratzman.spotify.models.Track>

    //    var image = Player.spotifyAppRemote?.imagesApi?.getImage(Player.coverAny.value as ImageUri?)
    var imagebitmap: ImageBitmap? = null
    var bitmap: MutableState<Bitmap?> = mutableStateOf(null)
//    var pendingresult = spotifyAppRemote?.imagesApi?.getImage(coverUri.value)
//        ?.setResultCallback { result ->  bitmap = result }
//        ?.setErrorCallback { throwable -> Log.e("player.kt", "error fetching image")}


    private fun quickAdd(
        context: Context,
        trackUriString: String,
        playlistUri: String,
        displayname: String
    ) {
        try {
            val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
            scope.launch {
                val api = Model.credentialStore.getSpotifyClientPkceApi()!!
                val trackUri = StringToPlayableURI(trackUriString)
                api.playlists.addPlayableToClientPlaylist(
                    playlist = playlistUri,
                    playable = trackUri
                )
                toasty(context, "track added to $displayname")
            }
        } catch (ex: Exception) {
            toasty(context, "track could not be added")
            ex.message?.let { Log.e("error in quickAdd", it) } // log errors
        }
    }

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
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
//            val maxHeight = this.maxHeight
//                if(image != null) {

                GlideImage(
                    cover.value,
                    modifier = Modifier // 'modifier' uses argument, 'Modifier' creates new modifier
                        .size(372.dp),

                    contentDescription = null,
                )
            }


            Row(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {

                Button( // back

                    modifier = Modifier
                        .alpha(bigButtonOpacity.value)
                        .fillMaxHeight(fraction = .6f)
                        .fillMaxWidth(fraction = .4f)
                        .background(color = GridlineTheme.colors.uiBackground),
                    onClick = {
                        Player.skipTo = Player.SkipVals.BACK;

                    },
                ) {}

                Button( // forwards
                    modifier = Modifier
                        .alpha(bigButtonOpacity.value) // todo change color here
                        .fillMaxHeight(fraction = .6f)
                        .fillMaxWidth(fraction = 1f),
                    onClick = {
                        Player.skipTo = Player.SkipVals.FORWARD;
                    }
                ) {}

            }

        }

    }

    @Composable
    fun PlayerPage() {
        GridlineTheme() {

            Column() {


//                Column() { // this starts at the same height as the

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

    @OptIn(ExperimentalFoundationApi::class) // for basicMarquee
    @Composable
    private fun BottomPart() {
        val context = LocalContext.current

        Column(
            modifier = Modifier
        ) {
            Column(

            ) {

                currentTrack.value?.name?.let {
                    Text(it,
                        //                    trackname.value,
                        modifier = Modifier
                            .padding(start = 20.dp)
//                            .basicMarquee(velocity=64.dp)
                            .basicMarquee()

                            .clickable {
                               toasty(context, "get rekt")
                            },
                        style = MaterialTheme.typography.h5
                    )
                }
                Spacer(Modifier.padding(12.dp))

                currentTrack.value?.artist?.name?.let {
                    Text(it,
            //                    artistname.value,
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .basicMarquee()
                            .clickable {
                                val goto = Uri.parse(currentTrack.value?.artist?.uri)
                                Log.e("deeplink", "deeplinking to $goto")
                                //                            toasty(context, "Opening " + track.value?.artist?.name)
                                val browserIntent = Intent(Intent.ACTION_VIEW, goto)
                                ContextCompat.startActivity(context, browserIntent, null)
                            },
                        style = MaterialTheme.typography.h5  // todo figure out better typography

                    )
                }

                Spacer(Modifier.padding(16.dp))

                currentTrack.value?.album?.name?.let {
                    Text(it,
            //                    albumname.value,
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .basicMarquee()
                            .clickable {
                                val goto = Uri.parse(currentTrack.value?.album?.uri)
                                Log.e("deeplink", "deeplinking to $goto")
                                //                            toasty(context, "Opening " + track.value?.album?.name)

                                val browserIntent = Intent(Intent.ACTION_VIEW, goto)
                                ContextCompat.startActivity(context, browserIntent, null)
                            },
                        style = MaterialTheme.typography.body2
                    )
                }
                Spacer(Modifier.padding(8.dp))
                PlaybackButtonRow()
                Spacer(Modifier.padding(8.dp))

            }
        }
    }

    @Composable
    private fun PlaybackButtonRow(
        modifier: Modifier = Modifier,
        playerButtonSize: Dp = 72.dp, sideButtonSize: Dp = 48.dp
    ) {

        Row( // todo create a giant database of playback funcs
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {


            PlayerButton(
                icon = R.drawable.shuffle,
                OnClick = {
//                Player.skipTo = Player.Skip.BACK
//                Player.updateSkip()
                },
                modifier = Modifier.size(36.dp) // icon is bigger than the rest
            )


            PlayerButton(
                icon = R.drawable.baseline_skip_previous_24,
                OnClick = {
                    skipTo = SkipVals.BACK
                })


            PlayerButton(
//            icon = playPauseIcon.value,
                icon = if (isPlaying.value) R.drawable.baseline_pause_circle_24 else R.drawable.baseline_play_circle_24,
                OnClick = {
                    togglePlayback = true
                },
                modifier = Modifier.size(playerButtonSize) // icon is bigger than the rest
            )


            PlayerButton(
                icon = R.drawable.baseline_skip_next_24,
                OnClick = { skipTo = SkipVals.FORWARD })

            PlayerButton(
                icon = R.drawable.round_repeat_24,
                OnClick = {
//                Player.skipTo = Player.Skip.BACK
//                Player.updateSkip()
                },
            )

        }
    }

    @Composable
    private fun UtilRow(
        modifier: Modifier = Modifier,
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
                OnClick = {
                    currentTrack.value?.let {
                        quickAdd(context,
                            it.uri,
                            Constants.PURGELIST,
                            "Purgelist" // todo hardcoding
                        ) }
                })
            Spacer(spacing)


            PlayerButton(
                icon = R.drawable.point_scan,
                OnClick = {
                    if (bigButtonOpacity.value == 0.3f) bigButtonOpacity.value = 0f
                    else bigButtonOpacity.value = 0.3f
                })

            Spacer(spacing)



            PlayerButton(
                icon = R.drawable.round_water_drop_24,
                OnClick = {
                    currentTrack.value?.let {
                        quickAdd(context,
                            it.uri,
                            Constants.ROADPURGE,
                            "Road Cleansing" // todo hardcoding
                        )
                    }
                })



        }
    }
    @Composable
    private fun PlayerButton(
        icon: Int,
        contentDescription: String = stringResource(R.string.FILLME),
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
                )
            }
        )
    }
}
