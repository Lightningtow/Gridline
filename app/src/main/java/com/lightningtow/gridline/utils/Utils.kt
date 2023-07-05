package com.lightningtow.gridline.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import com.adamratzman.spotify.models.PlayableUri
import com.lightningtow.gridline.MainActivity
import com.lightningtow.gridline.auth.Model
import com.lightningtow.gridline.auth.guardValidSpotifyApi
import com.lightningtow.gridline.data.API_State
import com.lightningtow.gridline.getAlbumArt
import com.lightningtow.gridline.ui.components.SHORTCUT_TYPE
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// safeLet retrieved from: https://stackoverflow.com/a/35522422/6422820
fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? =
    if (p1 != null && p2 != null) block(p1, p2) else null

// safeLet retrieved from: https://stackoverflow.com/a/35522422/6422820
fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3) -> R?): R? =
    if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null

//fun Intent?.isSpotifyAuthIntent() : Boolean = (this != null && this.dataString?.startsWith("${BuildConfig.SPOTIFY_REDIRECT_URI}/?code=") == true)

fun SharedPreferences.saveString(key: String, value: String) = this.edit().putString(key, value).apply()
fun SharedPreferences.saveLong(key: String, value: Long) = this.edit().putLong(key, value).apply()
fun SharedPreferences.saveBoolean(key: String, value: Boolean) = this.edit().putBoolean(key, value).apply()

fun SharedPreferences.getStringNotNull(key: String, defaultValue: String? = ""): String = this.getString(key, defaultValue) ?: defaultValue ?: ""

fun Activity.toasty(message: String, duration: Int = Toast.LENGTH_SHORT) =
    toasty(this, message, duration)

fun Activity.toasty(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) =
    toasty(this, getString(message), duration)

fun RefreshAPIs() {
    MainActivity.
}

fun safeLoadImage(action: () -> Unit) {
    try {
        action.invoke()
    } catch (e: IllegalArgumentException) {
        // Possible error: You cannot start a load for a destroyed activity
    }
}
val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
    Log.e("coroutine error", throwable.message.toString())
    throwable.printStackTrace()
}
// todo deal with coroutines
//fun getCover(uri: String, type: SHORTCUT_TYPE): Any {
//    return withContext(Dispatchers.IO) {
//
//
//        val api = Model.credentialStore.getSpotifyClientPkceApi()!!
//
//        val cover =
//            if (type == SHORTCUT_TYPE.PLAYLIST) api.playlists.getPlaylistCovers(uri)
//            else Constants.DEFAULT_MISSING
//        return@launch cover
////    }
//}
//}
suspend fun StringToPlayableURI(arg: String): PlayableUri {
//    val api = Model.credentialStore.getSpotifyClientPkceApi()!!
//    val api = API_State.kotlinApi

    val trackUri: PlayableUri = API_State.kotlinApi.tracks.getTrack(arg)!!.uri
    return trackUri;
}

// from KotlinUtils.kt  /\

// from UIUtils.kt      \/

fun toasty(context: Context, message: String?, duration: Int = Toast.LENGTH_SHORT) {
    safeLet(context, message, duration) { safeContext, safeMessage, safeDuration ->
        (safeContext as? Activity)?.runOnUiThread {
            Toast.makeText(safeContext, safeMessage, safeDuration).show()
        }
    }
}