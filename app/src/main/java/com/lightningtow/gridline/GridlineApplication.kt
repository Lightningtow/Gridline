package com.lightningtow.gridline

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.lightningtow.gridline.auth.Model
import com.lightningtow.gridline.player.Player
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

class GridlineApplication : Application() {
    lateinit var model: Model

    override fun onCreate() {

        super.onCreate()
        instance = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        model = Model


    }

    companion object {
        val context: Context
            get() = instance!!.applicationContext

        @get:Synchronized
        var instance: GridlineApplication? = null
            private set
    }
}

