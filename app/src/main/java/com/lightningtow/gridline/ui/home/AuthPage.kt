package com.lightningtow.gridline.ui.home

import android.app.Activity
import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getDrawable
import com.adamratzman.spotify.auth.implicit.startSpotifyImplicitLoginActivity
import com.adamratzman.spotify.auth.pkce.startSpotifyClientPkceLoginActivity
import com.lightningtow.gridline.R
//import com.lightningtow.gridline.activities.ActionHomeActivity
//import com.lightningtow.gridline.activities.PlaylistViewPage
import com.lightningtow.gridline.auth.SpotifyImplicitLoginActivityImpl
import com.lightningtow.gridline.auth.SpotifyPkceLoginActivityImpl
import com.lightningtow.gridline.ui.components.GridlineButton
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.utils.toast
//import androidx.compose.material.icons.filled .materialIconsExtended
import com.lightningtow.gridline.R.drawable.point_scan

@Composable
private fun AuthPageInner(activity: Activity? = null) {

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
            .padding(16.dp)
            .fillMaxSize()
    ) {

        GridlineButton(onClick = {
            activity?.startSpotifyImplicitLoginActivity<SpotifyImplicitLoginActivityImpl>()
        }) {
            Text("Connect to Spotify (spotify-auth integration, Implicit Grant)")
        };
        Text(
            "The button above starts authentication via the spotify-auth library",
            style = MaterialTheme.typography.body2
        )


        GridlineButton(onClick = {
            activity?.startSpotifyClientPkceLoginActivity(SpotifyPkceLoginActivityImpl::class.java);
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

        Text(
            "spotify-web-api-kotlin by Adam Ratzman",
            color = Color.White,
            style = MaterialTheme.typography.body2
        )
        Button(onClick = {
//            activity?.model?.credentialStore?.spotifyAccessToken = "invalid"

            activity?.let {
                toast(
                    it,
                    message = "Invalidated spotify token... next call should refresh api"
                )
            }
        }) {
            Text("Invalidate token")
        }
        Spacer(modifier = Modifier.height(16.dp))
//        Icons()
    }
//        }
//    }

}

@Preview
@Composable
private fun AuthPagePreview() {
    AuthPage()
}

@Composable
fun AuthPage(activity: Activity? = null) {
    val context = LocalContext.current

    GridlineTheme() {
//        GridlineSurface() {
        AuthPageInner(activity)
//        }

        // idk what the below was supposed to do but it duplicates the nav bar


//        val navController = rememberNavController()
//
////                Surface(color = Color.White) {
//        // Scaffold Component
//        GridlineScaffold(bottomBar = {
//            BottomNavigationBar(navController = navController)
//        }, content = { innerPadding ->
//            // Apply the padding globally to the whole BottomNavScreensController
////                            Box(modifier = Modifier.padding(innerPadding)) {
//            NavHostContainer(navController = navController, padding = innerPadding)
////            AuthPageInner()
//
//
////         remember navController Language.so it does not
////         get recreated on recomposition
//            val navController = rememberNavController()
//
//            Surface(color = Color.White) {
////         Scaffold Component
//                Scaffold(
////         Bottom navigation
//                    bottomBar = {
//                        BottomNavigationBar(navController = navController)
//                    }, content = { innerPadding ->
//                        // Apply the padding globally to the whole BottomNavScreensController
////                            Box(modifier = Modifier.padding(innerPadding)) {
//                        NavHostContainer(navController = navController, padding = innerPadding)
//
//
//                    }
//
//                )
//
//            }
//        })
    }
}
