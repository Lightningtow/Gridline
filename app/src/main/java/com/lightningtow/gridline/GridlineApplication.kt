package com.lightningtow.gridline

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
//import com.adamratzman.spotify.SpotifyClientApi
import com.lightningtow.gridline.auth.Model
import com.lightningtow.gridline.utils.getAlbumArt

//import android.app.Application


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
        val ApplicationContext: Context
            get() = instance!!.applicationContext

        @get:Synchronized
        var instance: GridlineApplication? = null
            private set
    }
}

class AppLifecycleListener : DefaultLifecycleObserver {




    override fun onStart(owner: LifecycleOwner) { // app moved to foreground
        Log.e("GridlineApplication", "app moved to foreground")
        getAlbumArt("app moved to foreground") // app moved to foreground
    }

    override fun onStop(owner: LifecycleOwner) { // app moved to background
        Log.e("GridlineApplication", "app moved to background")

    }
}