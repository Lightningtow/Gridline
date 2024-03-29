package com.lightningtow.gridline.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
//import com.
//import com.bumptech.gli
import com.adamratzman.spotify.notifications.*
import com.lightningtow.gridline.GridlineApplication
import com.lightningtow.gridline.auth.Model
import com.lightningtow.gridline.notifications.SpotifyBroadcastReceiver
import com.lightningtow.gridline.ui.components.GridlineButton
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.utils.toasty
//import com.spotify.android.appremote.api
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

import java.util.*

//@Composable
//fun BroadcastButton() {
//    val context = LocalContext.current
//    GridlineTheme() {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier = Modifier
//                .fillMaxSize(),
//
//            ) {
//            GridlineButton(onClick = {
//                context.startActivity(Intent(context, Broadcasts::class.java))
//            }) {
//                Text("broadcasts")
//            }
//        }
//
//    }
//
//}

class Broadcasts : AppCompatActivity() {
    lateinit var spotifyBroadcastReceiver: SpotifyBroadcastReceiver
    val broadcasts: MutableList<SpotifyBroadcastEventData> = mutableStateListOf()
    lateinit var model: Model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = (application as GridlineApplication).model


        spotifyBroadcastReceiver = SpotifyBroadcastReceiver(this)

        setContent {
            ViewBroadcastsPage(broadcasts)
        }

        registerSpotifyBroadcastReceiver(spotifyBroadcastReceiver, *SpotifyBroadcastType.values())
    }
}

@Composable
private fun ViewBroadcastsPage(broadcasts: List<SpotifyBroadcastEventData>) {
    GridlineTheme() {
        val typography = MaterialTheme.typography

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Most recent 3 broadcasts",
                style = typography.h4,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (broadcasts.isNotEmpty()) {
                BroadcastList(broadcasts = broadcasts)
                Text(
                    "There have been ${broadcasts.size} total broadcasts received by this activity.",
                    style = typography.body1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            } else Text(
                "There are no broadcasts!",
                style = typography.body1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun BroadcastList(broadcasts: List<SpotifyBroadcastEventData>) {
    val context = LocalContext.current
    LazyColumn {
        items(items = broadcasts.takeLast(3).reversed(), itemContent = { broadcast ->
            BroadcastRow(broadcastEventData = broadcast, onClick = {
                toasty(context, "You clicked $broadcast")
            })
            Divider()
        })
    }
}

@Composable
private fun BroadcastRow(
    broadcastEventData: SpotifyBroadcastEventData, onClick: (SpotifyBroadcastEventData) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = { onClick(broadcastEventData) })
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = broadcastEventData.type.id,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h6
            )
            when (broadcastEventData) {
                is SpotifyQueueChangedData -> {
                    Text(
                        text = "Time sent: ${
                            DateFormat.getDateFormat(LocalContext.current)
                                .format(Date(broadcastEventData.timeSentInMs))
                        }", style = MaterialTheme.typography.body2
                    )
                }

                is SpotifyMetadataChangedData -> {
                    mapOf(
                        "Spotify URI" to broadcastEventData.playableUri,
                        "Track" to broadcastEventData.trackName,
                        "Artist" to broadcastEventData.artistName,
                        "Album" to broadcastEventData.albumName,
                        "Track length (seconds)" to broadcastEventData.trackLengthInSec,
                        "Time sent" to DateFormat.getDateFormat(LocalContext.current)
                            .format(Date(broadcastEventData.timeSentInMs))
                    ).forEach { (title, data) ->
                        Text(
                            text = "$title: $data", style = MaterialTheme.typography.caption
                        )
                    }
                }

                is SpotifyPlaybackStateChangedData -> {
                    mapOf(
                        "Is playing?" to broadcastEventData.playing,
                        "Position (seconds)" to broadcastEventData.positionInMs / 1000,
                        "Time sent" to DateFormat.getDateFormat(LocalContext.current)
                            .format(Date(broadcastEventData.timeSentInMs))
                    ).forEach { (title, data) ->
                        Text(
                            text = "$title: $data", style = MaterialTheme.typography.caption
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ViewBroadcastsPagePagePreview() {
    ViewBroadcastsPage(listOf())
}
