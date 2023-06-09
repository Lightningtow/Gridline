package com.lightningtow.gridline.auth

import android.content.Intent
import android.util.Log
import com.adamratzman.spotify.SpotifyImplicitGrantApi
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.auth.implicit.AbstractSpotifyAppImplicitLoginActivity
import com.lightningtow.gridline.BuildConfig
import com.lightningtow.gridline.MainActivity
import com.lightningtow.gridline.utils.toasty

class SpotifyImplicitLoginActivityImpl : AbstractSpotifyAppImplicitLoginActivity() {
    override val state: Int = 1337
    override val clientId: String = BuildConfig.SPOTIFY_CLIENT_ID
    override val redirectUri: String = BuildConfig.SPOTIFY_REDIRECT_URI_AUTH
    override val useDefaultRedirectHandler: Boolean = false
//    override fun getRequestingScopes(): List<SpotifyScope> = SpotifyScope.values().toList()

    val appremote = SpotifyScope.APP_REMOTE_CONTROL
    val streaming = SpotifyScope.STREAMING
    override fun getRequestingScopes(): List<SpotifyScope> = SpotifyScope.values().toList() + appremote + streaming

//    override fun getRequestingScopes(): List<SpotifyScope> =

    override fun onSuccess(spotifyApi: SpotifyImplicitGrantApi) {
        Log.e("ctrlfme", "implicit login success")
        Log.e("implicit scopes", getRequestingScopes().toString())

        val model = (application as com.lightningtow.gridline.GridlineApplication).model
        model.credentialStore.setSpotifyApi(spotifyApi)
        toasty("Authentication via spotify-auth has completed. Launching TrackViewActivity..")
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onFailure(errorMessage: String) {
        toasty("Auth failed: $errorMessage")
        Log.e("Auth failed:", errorMessage)
    }
}