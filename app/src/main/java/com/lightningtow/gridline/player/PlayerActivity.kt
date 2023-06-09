package com.lightningtow.gridline.player;

//import android.support.v7.app.AppCompatActivity;

//import com.adamratzman.spotify.models.Track

//import com.lightningtow.gridline.data.PlayerState.pauseNoView
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsActions.OnClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.lightningtow.gridline.R
import com.lightningtow.gridline.player.PlayerActivity.skipTo
import com.lightningtow.gridline.player.PlayerActivity.spotifyAppRemote
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.skydoves.landscapist.glide.GlideImage
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlin.properties.Delegates


public object PlayerActivity {

    var spotifyAppRemote: SpotifyAppRemote? = null


    enum class SkipVals {
        FORWARD, BACK
    }

//    mCurrentIndex.setValue(12345); // Replace 12345 with your int value.

/**     NEVER update isPlaying manually  */
    var isPlaying: MutableState<Boolean> = mutableStateOf(false)
//    var readOnlyPlaying = readOnlyisPlaying.value


/**     Observable values. Runs the func when the function gets changed  */
    var togglePlayback: Boolean by Delegates.observable(false) { property, oldvalue, newvalue ->
        Log.e("toggling to", (!isPlaying.value).toString())
        if (isPlaying.value) spotifyAppRemote?.playerApi?.pause()
        else if (!isPlaying.value) spotifyAppRemote?.playerApi?.resume()
        else Log.e("uhhh", "readonlyplaying has some deep seated issues")
//        togglePlayback = false // DON'T TOGGLE THIS BACK MANUALLY
        // why does it automatically toggle back
        // oh it doesn't, it just triggers when true->false. More efficient that way anyways
    }
    var skipTo: SkipVals? by Delegates.observable(null) { property, oldvalue, newvalue ->
        Log.e("skipping", skipTo.toString())
        when (skipTo) {
            SkipVals.FORWARD -> skipForward()
            SkipVals.BACK -> skipBack()
            else -> Log.e("ctrlfme", "ERROR: updateSkip called when skipTo == null")
        }
        skipTo = null
    }

//    fun onstart() {
//        spotifyAppRemote?.playerApi?.subscribeToPlayerState()
//            ?.setEventCallback { playerState -> readOnlyPlaying = !playerState.isPaused }
//            ?.setErrorCallback { throwable -> }
//    }



//    fun updatePlaying() {
//
//        when (setPlayingTo) {
//            false -> pause()
//            true -> resume()
//            else -> Log.e("ctrlfme", "ERROR: updatePlaying called when setPlayingTo == null")
//        }
//        setPlayingTo = null
//
//        // Subscribe to PlayerState
//        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback {
////            val track: Track = it.track
////            Log.d("MainActivity", track.name + " by " + track.artist.name)
//            Log.e("ctrlfme", it.isPaused.toString())
//        }
//
//
//    }

    fun updateSkip() {
        when (skipTo) {
            PlayerActivity.SkipVals.FORWARD -> skipForward()
            PlayerActivity.SkipVals.BACK -> skipBack()
            else -> Log.e("ctrlfme", "ERROR: updateSkip called when skipTo == null")
        }
        skipTo = null
    }

//    fun pause() {
//        Log.e("ctrlfme", "pausing")
//        spotifyAppRemote?.playerApi?.pause()
//    }
//    fun resume() {
//        Log.e("ctrlfme", "resuming")
//        spotifyAppRemote?.playerApi?.resume()
//    }

    fun skipForward() {
        Log.e("ctrlfme", "skipping forward")
        spotifyAppRemote?.playerApi?.skipNext()
    }

    fun skipBack() {
        Log.e("ctrlfme", "skipping back")
        spotifyAppRemote?.playerApi?.skipPrevious()
    }

    //    var idk = spotifyAppRemote?.playerApi?.playerState?.
//    var playPauseIcon: MutableState<Int> = mutableStateOf(if (isPlaying.value) R.drawable.baseline_pause_circle_24 else R.drawable.baseline_play_circle_24)

}

@Composable
fun PlayerPage(activity: Activity? = null) {
    GridlineTheme() {

        val context = LocalContext.current


        Column(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            BigButton()


            Spacer(Modifier.padding(16.dp))

            ButtonRow()

        }

    }
}

private val TransparentColors = darkColors(
    primary = Color.Transparent
)

@Composable
private fun BigButton() {
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

            GlideImage(
                modifier = Modifier // 'modifier' uses argument, 'Modifier' creates new modifier
                    .size(350.dp),
//                    .height(maxHeight),
                imageModel = "https://picsum.photos/300/300",
                contentDescription = null,
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.Center)
        ) {

            Button(

                modifier = Modifier
                    .alpha(0f)
                    .fillMaxHeight(fraction = .5f)
                    .fillMaxWidth(fraction = .4f)
                    .background(color = GridlineTheme.colors.uiBackground),
                onClick = {
                    PlayerActivity.skipTo = PlayerActivity.SkipVals.BACK;
                    PlayerActivity.updateSkip()

                },
            ) {}

            Button(
                modifier = Modifier
                    .alpha(0f) // todo change color here
                    .fillMaxHeight(fraction = .5f)
                    .fillMaxWidth(fraction = 1f),
                onClick = {
                    PlayerActivity.skipTo = PlayerActivity.SkipVals.FORWARD;
                    PlayerActivity.updateSkip()

                }
            ) {}

        }

    }

}


@Composable
private fun PlayerButton(
    icon: Int, // todo add missing icon
    contentDescription: String = stringResource(R.string.FILLME),
    modifier: Modifier = Modifier
        .size(48.dp) // todo make this less hardcoded
        .semantics { role = Role.Button },

    OnClick: () -> Unit
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

@Composable
private fun ButtonRow(
    modifier: Modifier = Modifier, playerButtonSize: Dp = 72.dp, sideButtonSize: Dp = 48.dp
) {

    Row( // todo create a giant database of playback funcs
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {


        PlayerButton(
            icon = R.drawable.shuffle,
            OnClick = {
//                PlayerActivity.skipTo = PlayerActivity.Skip.BACK
//                PlayerActivity.updateSkip()
            },
            modifier = Modifier.size(36.dp) // icon is bigger than the rest
        )


        PlayerButton(
            icon = R.drawable.baseline_skip_previous_24,
            OnClick = {
                PlayerActivity.skipTo = PlayerActivity.SkipVals.BACK
                PlayerActivity.updateSkip()
            },
        )

        PlayerButton(
//            icon = playPauseIcon.value,
            icon = if (PlayerActivity.isPlaying.value) R.drawable.baseline_pause_circle_24 else R.drawable.baseline_play_circle_24,
            OnClick = {
                PlayerActivity.togglePlayback = true
            },
        )

//        PlayerButton(
//            icon = R.drawable.baseline_play_circle_24,
//            OnClick = {
//                PlayerActivity.setPlayingTo = true
//                PlayerActivity.updatePlaying()
//            },
//        )

        PlayerButton(
            icon = R.drawable.baseline_skip_next_24,
            OnClick = {

                PlayerActivity.skipTo = PlayerActivity.SkipVals.FORWARD
                PlayerActivity.updateSkip()
            }
        )

        PlayerButton(
            icon = R.drawable.round_repeat_24,
            OnClick = {
//                PlayerActivity.skipTo = PlayerActivity.Skip.BACK
//                PlayerActivity.updateSkip()
            },
        )

    }
}
