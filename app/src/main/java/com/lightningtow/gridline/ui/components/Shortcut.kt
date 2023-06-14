package com.lightningtow.gridline.ui.components

//import com.lightningtow.gridline.ui.home.dataStore
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.lightningtow.gridline.GridlineApplication.Companion.context
import com.lightningtow.gridline.ShortcutStruct
import com.lightningtow.gridline.utils.Constants
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.io.OutputStream

public enum class SHORTCUT_TYPE { PLAYLIST, TRACK, ARTIST, ALBUM }
object ShortcutStructSerializer : Serializer<ShortcutStruct> {
    override val defaultValue: ShortcutStruct = ShortcutStruct.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ShortcutStruct {
        try {
            return ShortcutStruct.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: ShortcutStruct,
        output: OutputStream
    ) = t.writeTo(output)

//    override suspend fun getCoverUri(): Flow<Boolean> {
//        return  protoDataStore.data.map { protoBuilder ->
//            protoBuilder.isLoggedIn
//        }
//    }
}

val Context.shortcutStructDataStore: DataStore<ShortcutStruct> by dataStore(
    fileName = "ShortcutStruct.pb",
    serializer = ShortcutStructSerializer
)
//var coverUsed: MutableState<String?> = mutableStateOf("default")

@Composable
public fun ShortcutIcon(
    type: SHORTCUT_TYPE,
    displayname: String,
    uri: String,
//    cover: String? = null,
) {
//    Box(modifier = Modifier
//        .size(128.dp),
//        Alignment(Alignment.Center)

//    ) {
    val context = LocalContext.current
    val coveraaa by remember { mutableStateOf(realCover) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable() {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            browserIntent.putExtra(
                Intent.EXTRA_REFERRER,
                Uri.parse("android-app://" + context.packageName)
            )

            //  Uri.parse(track.externalUrls.first { it.name == "spotify" }.url) // what does this do
            Log.e("deeplink", "deeplinking to $uri")
            ContextCompat.startActivity(context, browserIntent, null)
        }
    ) {


//        val cover: MutableState<Any?> = mutableStateOf(null)
//        val test = getStuff()
        Log.e("ctrlfme", "displaying $coveraaa")
        GlideImage(
//            imageModel = getCover(uri, type) ?: Constants.DEFAULT_MISSING,
            imageModel = coveraaa ?: Constants.DEFAULT_MISSING,
            modifier = Modifier
                .size(150.dp)
        )
        Text(displayname)
    }


}
var realCover: String = "default"


fun getStuff() {
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    var ffs: String? = null
    var exampleData: ShortcutStruct? = null
    var scoped: String? = null

    scope.launch  {// TODO ITS A FUCKIN COROUTINE DUH, RACE CONDITIONS
        scoped = context.shortcutStructDataStore.data.first().coverUri
        Log.e("scoped", "scoped $scoped")
        exampleData = runBlocking { context.shortcutStructDataStore.data.first() }
        realCover = scoped ?: "null"
    }
}

//suspend fun uploadStuff() {
fun uploadStuff() {
    val cover: String = "default2"
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    scope.launch {
        context.shortcutStructDataStore.updateData { shortcut ->
            shortcut.toBuilder()
                .setCoverUri("https://i.scdn.co/image/ab67706c0000bebbfee4892fcb2d95910502c238")
                .build()
        }

    }
}

data class Shortcut(
    val uri: String
) {


}


//object ShortcutData {
//
//    var currentSlot: Int = 1;
//
//
//    var displaynamelist =
//        mutableStateListOf<String>("default", "default", "default", "default", "default", "default")
//    var urilist =
//        mutableStateListOf<String>("default", "default", "default", "default", "default", "default")
//    var coverlist =
//        mutableStateListOf<String>("default", "default", "default", "default", "default", "default")
//    var isEmptylist = mutableStateListOf<Boolean>(true, true, true, true, true, true)
//
//// todo elem 0 is purgelist, elem 1 is afterpurge, rest are victim slots
//
//    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
//
//    fun resetStore(context: Context) {
//        scope.launch {
//            val ds = context.dataStore
//
//            ds.edit { settings -> settings[NAME_0] = "default" }
//            ds.edit { settings -> settings[NAME_1] = "default" }
//            ds.edit { settings -> settings[NAME_2] = "default" }
//            ds.edit { settings -> settings[NAME_3] = "default" }
//            ds.edit { settings -> settings[NAME_4] = "default" }
//            ds.edit { settings -> settings[NAME_5] = "default" }
//
//            ds.edit { settings -> settings[URI_0] = "default" }
//            ds.edit { settings -> settings[URI_1] = "default" }
//            ds.edit { settings -> settings[URI_2] = "default" }
//            ds.edit { settings -> settings[URI_3] = "default" }
//            ds.edit { settings -> settings[URI_4] = "default" }
//            ds.edit { settings -> settings[URI_5] = "default" }
//
//            ds.edit { settings -> settings[EMPTY_0] = true }
//            ds.edit { settings -> settings[EMPTY_1] = true }
//            ds.edit { settings -> settings[EMPTY_2] = true }
//            ds.edit { settings -> settings[EMPTY_3] = true }
//            ds.edit { settings -> settings[EMPTY_4] = true }
//            ds.edit { settings -> settings[EMPTY_5] = true }
//
//            toasty(context, "reset dataStore")
//        }
//    }
//
//    fun setSavedPurges(context: Context) {
//        scope.launch {
//            val ds = context.dataStore
//
//            ds.edit { settings -> settings[NAME_0] = namelist[0] }
//            ds.edit { settings -> settings[NAME_1] = namelist[1] }
//            ds.edit { settings -> settings[NAME_2] = namelist[2] }
//            ds.edit { settings -> settings[NAME_3] = namelist[3] }
//            ds.edit { settings -> settings[NAME_4] = namelist[4] }
//            ds.edit { settings -> settings[NAME_5] = namelist[5] }
//
//            ds.edit { settings -> settings[URI_0] = urilist[0] }
//            ds.edit { settings -> settings[URI_1] = urilist[1] }
//            ds.edit { settings -> settings[URI_2] = urilist[2] }
//            ds.edit { settings -> settings[URI_3] = urilist[3] }
//            ds.edit { settings -> settings[URI_4] = urilist[4] }
//            ds.edit { settings -> settings[URI_5] = urilist[5] }
//
//            ds.edit { settings -> settings[EMPTY_0] = isEmptylist[0] }
//            ds.edit { settings -> settings[EMPTY_1] = isEmptylist[1] }
//            ds.edit { settings -> settings[EMPTY_2] = isEmptylist[2] }
//            ds.edit { settings -> settings[EMPTY_3] = isEmptylist[3] }
//            ds.edit { settings -> settings[EMPTY_4] = isEmptylist[4] }
//            ds.edit { settings -> settings[EMPTY_5] = isEmptylist[5] }
//
//            toasty(context, "saved playlists")
//        }
//    }
//
//    fun getSavedPurges(context: Context) {
//        val ds = context.dataStore
//
//        namelist[0] = runBlocking { (ds.data.first()) }[NAME_0]!!;
//        namelist[1] = runBlocking { (ds.data.first()) }[NAME_1]!!;
//        namelist[2] = runBlocking { (ds.data.first()) }[NAME_2]!!;
//        namelist[3] = runBlocking { (ds.data.first()) }[NAME_3]!!;
//        namelist[4] = runBlocking { (ds.data.first()) }[NAME_4]!!;
//        namelist[5] = runBlocking { (ds.data.first()) }[NAME_5]!!;
//
//        urilist[0] = runBlocking { (ds.data.first()) }[URI_0]!!;
//        urilist[1] = runBlocking { (ds.data.first()) }[URI_1]!!;
//        urilist[2] = runBlocking { (ds.data.first()) }[URI_2]!!;
//        urilist[3] = runBlocking { (ds.data.first()) }[URI_3]!!;
//        urilist[4] = runBlocking { (ds.data.first()) }[URI_4]!!;
//        urilist[5] = runBlocking { (ds.data.first()) }[URI_5]!!;
//
//        isEmptylist[0] = runBlocking { (ds.data.first()) }[EMPTY_0]!!;
//        isEmptylist[1] = runBlocking { (ds.data.first()) }[EMPTY_1]!!;
//        isEmptylist[2] = runBlocking { (ds.data.first()) }[EMPTY_2]!!;
//        isEmptylist[3] = runBlocking { (ds.data.first()) }[EMPTY_3]!!;
//        isEmptylist[4] = runBlocking { (ds.data.first()) }[EMPTY_4]!!;
//        isEmptylist[5] = runBlocking { (ds.data.first()) }[EMPTY_5]!!;
//
//    }
//
//    //    private val NAME_PURGE = stringPreferencesKey("name_purge")
//    private val NAME_0 = stringPreferencesKey("name_0")
//    private val NAME_1 = stringPreferencesKey("name_1")
//    private val NAME_2 = stringPreferencesKey("name_2")
//    private val NAME_3 = stringPreferencesKey("name_3")
//    private val NAME_4 = stringPreferencesKey("name_4")
//    private val NAME_5 = stringPreferencesKey("name_5")
//
//    private val URI_0 = stringPreferencesKey("uri_0")
//    private val URI_1 = stringPreferencesKey("uri_1")
//    private val URI_2 = stringPreferencesKey("uri_2")
//    private val URI_3 = stringPreferencesKey("uri_3")
//    private val URI_4 = stringPreferencesKey("uri_4")
//    private val URI_5 = stringPreferencesKey("uri_5")
//
//    private val EMPTY_0 = booleanPreferencesKey("empty_0")
//    private val EMPTY_1 = booleanPreferencesKey("empty_1")
//    private val EMPTY_2 = booleanPreferencesKey("empty_2")
//    private val EMPTY_3 = booleanPreferencesKey("empty_3")
//    private val EMPTY_4 = booleanPreferencesKey("empty_4")
//    private val EMPTY_5 = booleanPreferencesKey("empty_5")
//
//}


