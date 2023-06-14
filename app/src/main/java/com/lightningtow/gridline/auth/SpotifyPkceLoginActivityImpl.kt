package com.lightningtow.gridline.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.lightningtow.gridline.BuildConfig
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.auth.pkce.AbstractSpotifyPkceLoginActivity
import com.lightningtow.gridline.MainActivity
import com.lightningtow.gridline.utils.toasty

internal var pkceClassBackTo: Class<out Activity>? = null

class SpotifyPkceLoginActivityImpl : AbstractSpotifyPkceLoginActivity() {
    override val clientId: String = BuildConfig.SPOTIFY_CLIENT_ID
    override val redirectUri: String = BuildConfig.SPOTIFY_REDIRECT_URI_PKCE

    //    override val clientId: String = Secrets.CLIENT_ID
    //    override val redirectUri: String = Secrets.REDIRECT_URI_PKCE
    val appremote = SpotifyScope.APP_REMOTE_CONTROL
    val streaming = SpotifyScope.STREAMING
    override val scopes = SpotifyScope.values().toList() + streaming + appremote
//    override val scopes = SpotifyScope.values().toList() // original

    override fun onCreate(savedInstanceState: Bundle?) { // this didnt exist
        super.onCreate(savedInstanceState)
        Log.e("auth", "LOGGING IN VIA PKCE")
    }

    override fun onSuccess(api: SpotifyClientApi) {
        Log.e("auth", "pkce login success")
        Log.e("pkce scopes", scopes.toString())
        val model = (application as com.lightningtow.gridline.GridlineApplication).model
        model.credentialStore.setSpotifyApi(api)
        val classBackTo = pkceClassBackTo ?: MainActivity::class.java
        pkceClassBackTo = null
        toasty("PKCE auth completed. Launching ${classBackTo.simpleName}..")
//        toasty("Authentication via PKCE has completed. Launching ${classBackTo.simpleName}..")
        startActivity(Intent(this, classBackTo))
    }

    override fun onFailure(exception: Exception) {
        exception.printStackTrace()
        pkceClassBackTo = null
        toasty("PKCE auth failed: ${exception.message}")
        Log.e("PKCE auth failed:", "${exception.message}")

    }
}