package com.lightningtow.gridline

import android.app.Application
import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.adamratzman.spotify.SpotifyClientApi
import com.lightningtow.gridline.auth.Model
import com.lightningtow.gridline.data.PlaylistsHolder
import com.lightningtow.gridline.auth.guardValidSpotifyApi

import com.lightningtow.gridline.player.Player
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File
//import android.app.Application
import java.io.IOException


class GridlineApplication : Application() {
    lateinit var model: Model

    override fun onCreate() {

        super.onCreate()
        instance = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        model = Model

        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleListener()) // for new app lifecycle listener class

        // https://stackoverflow.com/questions/19565685/saving-logcat-to-a-text-file-in-android-device
//        getExternalFilesDir(null)?.let { publicAppDirectory -> // getExternalFilesDir don't need storage permission
//            val logDirectory = File("${publicAppDirectory.absolutePath}/logs")
//            if (!logDirectory.exists()) {
//                logDirectory.mkdir()
//            }
//
//            val logFile = File(logDirectory, "logcat_" + System.currentTimeMillis() + ".txt")
//            // clear the previous logcat and then write the new one to the file
//            try {
//                Runtime.getRuntime().exec("logcat -c")
//                Runtime.getRuntime().exec("logcat -f $logFile")
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }


    }

    companion object {
        val context: Context
            get() = instance!!.applicationContext

        @get:Synchronized
        var instance: GridlineApplication? = null
            private set
    }
}

class AppLifecycleListener : DefaultLifecycleObserver {




    override fun onStart(owner: LifecycleOwner) { // app moved to foreground
        getAlbumArt()
    }

    override fun onStop(owner: LifecycleOwner) { // app moved to background
    }
}