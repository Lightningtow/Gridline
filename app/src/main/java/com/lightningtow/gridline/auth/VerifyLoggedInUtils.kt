package com.lightningtow.gridline.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.net.Credentials
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.auth.SpotifyDefaultCredentialStore
import com.adamratzman.spotify.auth.implicit.startSpotifyImplicitLoginActivity
import com.adamratzman.spotify.auth.pkce.startSpotifyClientPkceLoginActivity
import com.lightningtow.gridline.GridlineApplication.Companion.ApplicationContext
import com.lightningtow.gridline.MainActivity
import com.lightningtow.gridline.data.API_State.spotifyAppRemote
import com.lightningtow.gridline.ui.home.Broadcasts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.UnknownHostException



//fun guardValidAppRemote() {
//    if (spotifyAppRemote != null && spotifyAppRemote!!.isConnected) {
//        return
//    } else {
////        val intent = Intent(ApplicationContext, MainActivity::class.java)
////        startActivity(intent)
////        finish()
////        val intent = Intent( MainActivity::class.java)
////        startActivity()
////        context.startActivity(Intent(context, Broadcasts::class.java))
////        val browserIntent = Intent(Intent.ACTION_VIEW, MainActivity::class.java)
////        startActivity(Intent(this, MainActivity::class.java))
////        val intent = Intent(ApplicationContext, MainActivity::class.java)
////        startActivity(intent)
////        finish()
//        startActivity(ApplicationContext, Intent(ApplicationContext, MainActivity::class.java), null)
//    }
//}

private fun guardValidSpotifyApi(
    classBackTo: Class<MainActivity>,
    block: suspend (SpotifyClientApi) -> Unit) {

}
fun <T> Activity.guardValidSpotifyApi(
    classBackTo: Class<out Activity>,
    alreadyTriedToReauthenticate: Boolean = false,
    block: suspend (api: SpotifyClientApi) -> T
): T? {
    return runBlocking {
        try {
            val token = Model.credentialStore.spotifyToken
                ?: throw SpotifyException.ReAuthenticationNeededException()
            val usesPkceAuth = token.refreshToken != null  // todo this is an attempt to force using pkce and not implicit
            // PKCE authorization lets you obtain a refreshable Spotify token.
            // This means that you do not need to keep prompting your users to re-authenticate (or force them to wait a second for automatic login).
//            val usesPkceAuth = true

            val api = (if (usesPkceAuth) Model.credentialStore.getSpotifyClientPkceApi()
            else Model.credentialStore.getSpotifyImplicitGrantApi())
                ?: throw SpotifyException.ReAuthenticationNeededException()

            block(api)
        } catch (e: SpotifyException) {
            e.printStackTrace()
            val usesPkceAuth = Model.credentialStore.spotifyToken?.refreshToken != null
            if (usesPkceAuth) {
                val api = Model.credentialStore.getSpotifyClientPkceApi()!!
                if (!alreadyTriedToReauthenticate) {
                    try {
                        api.refreshToken()
                        Model.credentialStore.spotifyToken = api.token
                        block(api)
                    } catch (e: SpotifyException.ReAuthenticationNeededException) {
                        e.printStackTrace()
                        return@runBlocking guardValidSpotifyApi(
                            classBackTo = classBackTo,
                            alreadyTriedToReauthenticate = true,
                            block = block
                        )
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace()
                        return@runBlocking guardValidSpotifyApi(
                            classBackTo = classBackTo,
                            alreadyTriedToReauthenticate = true,
                            block = block
                        )
                    }
                } else {
                    pkceClassBackTo = classBackTo
                    startSpotifyClientPkceLoginActivity(SpotifyPkceLoginActivityImpl::class.java)
                    null
                }
            }
            else {
//                exitProcess(-69)
                SpotifyDefaultCredentialStore.activityBackOnImplicitAuth = classBackTo
                startSpotifyImplicitLoginActivity(SpotifyImplicitLoginActivityImpl::class.java)
                null
            }
        }
    }
}
