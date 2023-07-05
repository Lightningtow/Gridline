package com.lightningtow.gridline.auth

import com.lightningtow.gridline.BuildConfig
import com.adamratzman.spotify.auth.SpotifyDefaultCredentialStore
//import com.lightningtow.gridline.GridlineApplication

object Model {
    val credentialStore by lazy {
        SpotifyDefaultCredentialStore(
            clientId = BuildConfig.SPOTIFY_CLIENT_ID,
            redirectUri = BuildConfig.SPOTIFY_REDIRECT_URI_PKCE,
            applicationContext = com.lightningtow.gridline.GridlineApplication.ApplicationContext
        )
    }
}