package com.lightningtow.gridline.player;

//import android.support.v7.app.AppCompatActivity;

//import com.adamratzman.spotify.models.Track

//import com.lightningtow.gridline.data.PlayerState.pauseNoView
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsActions.OnClick
import androidx.compose.ui.semantics.SemanticsProperties.ContentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.adamratzman.spotify.utils.Language
import com.lightningtow.gridline.BuildConfig
import com.lightningtow.gridline.R
import com.lightningtow.gridline.player.PlayerActivity.playPauseIcon
import com.lightningtow.gridline.player.PlayerActivity.playing
import com.lightningtow.gridline.ui.components.GridlineButton
import com.lightningtow.gridline.ui.theme.GridlineColors
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.skydoves.landscapist.glide.GlideImage
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import kotlinx.coroutines.NonDisposableHandle.parent

//class PlayerActivity : AppCompatActivity() {

//object PlayerActivity : AppCompatActivity() {
//    companion object {
//    fun PlayerActivity() {}
public object PlayerActivity {
    //        public fun PlayerActivity() {}
//        private val clientId: String = BuildConfig.SPOTIFY_CLIENT_ID
//        private val redirectUri: String = BuildConfig.SPOTIFY_REDIRECT_URI_PKCE
    var spotifyAppRemote: SpotifyAppRemote? = null

    enum class Skip {
        FORWARD, BACK
    }

    var change: Boolean = false
    var playing: Boolean = false
    var setPlayingTo: Boolean? = null
    var skipTo: Skip? = null

    // todo better way of doing this
    // todo    https://stackoverflow.com/questions/54186231/detecting-when-a-value-changed-in-a-specific-variable-in-android
    // todo    https://stackoverflow.com/questions/7157123/in-android-how-do-i-take-an-action-whenever-a-variable-changes
    fun updatePlaying() {
        when (setPlayingTo) {
            false -> pause()
            true -> resume()
            else -> Log.e("ctrlfme", "ERROR: updatePlaying called when setPlayingTo == null")
        }
        setPlayingTo = null
    }

    fun updateSkip() {
        when (skipTo) {
            Skip.FORWARD -> skipForward()
            Skip.BACK -> skipBack()
            else -> Log.e("ctrlfme", "ERROR: updateSkip called when skipTo == null")
        }
        skipTo = null
    }


    fun pause() {
        Log.e("ctrlfme", "pausing")
        spotifyAppRemote?.playerApi?.pause()
    }

    fun resume() {
        Log.e("ctrlfme", "resuming")
        spotifyAppRemote?.playerApi?.resume()
    }

    fun skipForward() {
        Log.e("ctrlfme", "skipping forward")
        spotifyAppRemote?.playerApi?.skipNext()
    }

    fun skipBack() {
        Log.e("ctrlfme", "skipping back")
        spotifyAppRemote?.playerApi?.skipPrevious()
    }

    //    var idk = spotifyAppRemote?.playerApi?.playerState?.
    var playPauseIcon =
        { mutableStateOf(if (playing) R.drawable.baseline_pause_circle_24 else R.drawable.baseline_play_circle_24) }
    // todo actually sync it to spotify

}

@Composable
fun PlayerPage(activity: Activity? = null) {
    GridlineTheme() {


        val context = LocalContext.current
//    MaterialTheme {
//        val typography = MaterialTheme.typography
//        val LocalContentColor = MaterialTheme.colors.contentColorFor(Color.White)

//        Surface(
////            color = MaterialTheme.colors.background
//
//
//        ) {

        Column(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            BigButton()
//                GridlineButton(onClick = {
//
//                }) {
//                    Text("start, hit me first")
//                };
//                GridlineButton(onClick = {
//                    context.startActivity(Intent(context, MainActivity::class.java))
//                }) {
//                    Text("start activity")
//                };

            Spacer(Modifier.padding(16.dp))

            ButtonRow()

        }

    }
}

private val TransparentColors = darkColors(
    primary = Color.Transparent
)

@Composable
private fun BigButton(

) {


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
                    PlayerActivity.skipTo = PlayerActivity.Skip.BACK;
                    PlayerActivity.updateSkip()

                },
            ) {}

            Button(
                modifier = Modifier
                    .alpha(0f) // todo change color here
                    .fillMaxHeight(fraction = .5f)
                    .fillMaxWidth(fraction = 1f),
                onClick = {
                    PlayerActivity.skipTo = PlayerActivity.Skip.FORWARD;
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


        val FILLME = stringResource(R.string.FILLME)

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
                PlayerActivity.skipTo = PlayerActivity.Skip.BACK
                PlayerActivity.updateSkip()
            },
        )

        PlayerButton(
            icon = R.drawable.baseline_pause_circle_24,
            OnClick = {
                PlayerActivity.setPlayingTo = false
                PlayerActivity.updatePlaying()
            },
        )

        PlayerButton(
            icon = R.drawable.baseline_play_circle_24,
            OnClick = {
                PlayerActivity.setPlayingTo = true
                PlayerActivity.updatePlaying()
            },
        )

        PlayerButton(
            icon = R.drawable.baseline_skip_next_24,
            OnClick = {

                PlayerActivity.skipTo = PlayerActivity.Skip.FORWARD
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
