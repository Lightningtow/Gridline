package com.lightningtow.gridline.ui.home

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adamratzman.spotify.auth.implicit.startSpotifyImplicitLoginActivity
import com.adamratzman.spotify.auth.pkce.startSpotifyClientPkceLoginActivity
import com.lightningtow.gridline.auth.Model
import com.lightningtow.gridline.auth.Model.credentialStore
//import com.lightningtow.gridline.activities.ActionHomeActivity
//import com.lightningtow.gridline.activities.PlaylistViewPage
import com.lightningtow.gridline.auth.SpotifyImplicitLoginActivityImpl
import com.lightningtow.gridline.auth.SpotifyPkceLoginActivityImpl
import com.lightningtow.gridline.ui.components.GridlineButton
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.utils.toasty

//import androidx.compose.material.icons.filled .materialIconsExtended

@Composable
private fun AuthPageInner(activity: Activity? = null) {
    val context = LocalContext.current

//    MaterialTheme {
//        val typography = MaterialTheme.typography
//        val LocalContentColor = MaterialTheme.colors.contentColorFor(Color.White)

//        Surface(
////            color = MaterialTheme.colors.background
//        ) {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        GridlineButton(onClick = {
            Log.e("AuthPageInner", "implicit login")
//            activity?.startSpotifyImplicitLoginActivity<SpotifyImplicitLoginActivityImpl>()
            context.startActivity(Intent(context, SpotifyImplicitLoginActivityImpl::class.java))

        }) {
            Text("Connect to Spotify (spotify-auth integration, Implicit Grant)")
        };
        Text(
            "The button above starts authentication via the spotify-auth library",
            style = MaterialTheme.typography.body2
        )


        GridlineButton(onClick = {
            Log.e("AuthPageInner", "pkce login")
//            activity?.startSpotifyClientPkceLoginActivity(SpotifyPkceLoginActivityImpl::class.java);
            context.startActivity(Intent(context, SpotifyPkceLoginActivityImpl::class.java))

        }) {
            Text("Connect to Spotify (spotify-web-api-kotlin integration, PKCE auth)")
        }
        Text(
            "The button above starts authentication via our PKCE auth implementation",
            style = MaterialTheme.typography.body2
        )


//        GridlineButton(onClick = {
//            activity?.startActivity(Intent(activity, ActionHomeActivity::class.java))
//        }) {
//            Text("Go to the app →")
//        }
//        Text(
//            "If you are logged out when clicking this button, you will be prompted to authenticate via spotify-auth via implicit auth, if you haven't already authenticated via PKCE",
////                color = Color.White,
//            style = MaterialTheme.typography.body2
//        )

        Spacer(modifier = Modifier.height(16.dp))


        GridlineButton(onClick = {
//            activity?.model?.credentialStore?.spotifyAccessToken = "invalid"
            Log.e("AuthPageInner", "invalidating token")
            credentialStore.spotifyAccessToken = "this is the text of an invalid token"
//            Model?.credentialStore?.spotifyAccessToken = "invalid"

//            activity?.let {
            toasty(context, message = "Invalidated spotify token... next call should refresh api")
            Log.e("AuthPageInner", credentialStore.spotifyAccessToken!!)
//            }

        }) {
            Text("Invalidate token")
        }
        Spacer(modifier = Modifier.height(16.dp))
//        Icons()
    }
//        }
//    }

}

//@Preview
//@Composable
//private fun AuthPagePreview() {
//    AuthPage()
//}

@Composable
fun AuthPage(activity: Activity? = null) {
    val context = LocalContext.current

    GridlineTheme() {
//        GridlineSurface() {
        AuthPageInner(activity)
//        }

    }
}
