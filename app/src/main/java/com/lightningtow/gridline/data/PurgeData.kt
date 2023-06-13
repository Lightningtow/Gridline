package com.lightningtow.gridline.data

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.lightningtow.gridline.ui.home.dataStore
import com.lightningtow.gridline.utils.toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.io.OutputStream



//object SettingsSerializer : Serializer<NewName> {
//    override val defaultValue: Settings = Settings.getDefaultInstance()
//
//    override suspend fun readFrom(input: InputStream): Settings {
//        try {
//            return Settings.parseFrom(input)
//        } catch (exception: InvalidProtocolBufferException) {
//            throw CorruptionException("Cannot read proto.", exception)
//        }
//    }
//
//    override suspend fun writeTo(
//        t: Settings,
//        output: OutputStream) = t.writeTo(output)
//}
//
//val Context.settingsDataStore: DataStore<Settings> by dataStore(
//    fileName = "settings.pb",
//    serializer = SettingsSerializer
//)
object PurgeData {

    var currentSlot: Int = 1;
//    var choosingPurgelist: Boolean = false

//    var purgename: MutableState<String> = mutableStateOf<String>("default")
//    var purgeuri: MutableState<String>  = mutableStateOf<String>("default")
//    var purgecover: MutableState<String>  = mutableStateOf<String>("default")
//    var purgeEmpty: MutableState<Boolean> = mutableStateOf<Boolean>(true)

    var namelist  = mutableStateListOf<String>("default", "default", "default", "default", "default", "default")
    var urilist   = mutableStateListOf<String>("default", "default", "default", "default", "default", "default")
    var coverlist = mutableStateListOf<String>("default", "default", "default", "default", "default", "default")
    var isEmptylist  = mutableStateListOf<Boolean>(true, true, true, true, true, true)

// todo elem 0 is purgelist, elem 1 is afterpurge, rest are victim slots

    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun resetStore(context: Context) {
        scope.launch {
            val ds = context.dataStore

            ds.edit { settings -> settings[NAME_0] = "default" }
            ds.edit { settings -> settings[NAME_1] = "default" }
            ds.edit { settings -> settings[NAME_2] = "default" }
            ds.edit { settings -> settings[NAME_3] = "default" }
            ds.edit { settings -> settings[NAME_4] = "default" }
            ds.edit { settings -> settings[NAME_5] = "default" }

            ds.edit { settings -> settings[URI_0] = "default" }
            ds.edit { settings -> settings[URI_1] = "default" }
            ds.edit { settings -> settings[URI_2] = "default" }
            ds.edit { settings -> settings[URI_3] = "default" }
            ds.edit { settings -> settings[URI_4] = "default" }
            ds.edit { settings -> settings[URI_5] = "default" }

            ds.edit { settings -> settings[EMPTY_0] = true }
            ds.edit { settings -> settings[EMPTY_1] = true }
            ds.edit { settings -> settings[EMPTY_2] = true }
            ds.edit { settings -> settings[EMPTY_3] = true }
            ds.edit { settings -> settings[EMPTY_4] = true }
            ds.edit { settings -> settings[EMPTY_5] = true }

//            ds.edit { settings -> settings[COVER_PURGE] = "default" }
//            ds.edit { settings -> settings[COVER_0] = "default" }
//            ds.edit { settings -> settings[COVER_1] = "default" }
//            ds.edit { settings -> settings[COVER_2] = "default" }
//            ds.edit { settings -> settings[COVER_3] = "default" }
//            ds.edit { settings -> settings[COVER_4] = "default" }
            toasty(context,"reset dataStore")
        }
    }
    fun setSavedPurges(context: Context) {
        scope.launch {
            val ds = context.dataStore

            ds.edit { settings -> settings[NAME_0] = namelist[0] }
            ds.edit { settings -> settings[NAME_1] = namelist[1] }
            ds.edit { settings -> settings[NAME_2] = namelist[2] }
            ds.edit { settings -> settings[NAME_3] = namelist[3] }
            ds.edit { settings -> settings[NAME_4] = namelist[4] }
            ds.edit { settings -> settings[NAME_5] = namelist[5] }

            ds.edit { settings -> settings[URI_0] = urilist[0] }
            ds.edit { settings -> settings[URI_1] = urilist[1] }
            ds.edit { settings -> settings[URI_2] = urilist[2] }
            ds.edit { settings -> settings[URI_3] = urilist[3] }
            ds.edit { settings -> settings[URI_4] = urilist[4] }
            ds.edit { settings -> settings[URI_5] = urilist[5] }

            ds.edit { settings -> settings[EMPTY_0] = isEmptylist[0] }
            ds.edit { settings -> settings[EMPTY_1] = isEmptylist[1] }
            ds.edit { settings -> settings[EMPTY_2] = isEmptylist[2] }
            ds.edit { settings -> settings[EMPTY_3] = isEmptylist[3] }
            ds.edit { settings -> settings[EMPTY_4] = isEmptylist[4] }
            ds.edit { settings -> settings[EMPTY_5] = isEmptylist[5] }
            
//            if (purgecover.value != "default") ds.edit { settings -> settings[COVER_PURGE] = purgecover.value }
//            if (coverlist[0] != "default") ds.edit { settings -> settings[COVER_0] = coverlist[0] }
//            if (coverlist[1] != "default") ds.edit { settings -> settings[COVER_1] = coverlist[1] }
//            if (coverlist[2] != "default") ds.edit { settings -> settings[COVER_2] = coverlist[2] }
//            if (coverlist[3] != "default") ds.edit { settings -> settings[COVER_3] = coverlist[3] }
//            if (coverlist[4] != "default") ds.edit { settings -> settings[COVER_4] = coverlist[4] }
            toasty(context,"saved playlists")
        }
    }
    fun getSavedPurges(context: Context) {
        val ds = context.dataStore

        namelist[0] = runBlocking { (ds.data.first()) }[NAME_0]!!;
        namelist[1] = runBlocking { (ds.data.first()) }[NAME_1]!!;
        namelist[2] = runBlocking { (ds.data.first()) }[NAME_2]!!;
        namelist[3] = runBlocking { (ds.data.first()) }[NAME_3]!!;
        namelist[4] = runBlocking { (ds.data.first()) }[NAME_4]!!;
        namelist[5] = runBlocking { (ds.data.first()) }[NAME_5]!!;

        urilist[0] = runBlocking { (ds.data.first()) }[URI_0]!!;
        urilist[1] = runBlocking { (ds.data.first()) }[URI_1]!!;
        urilist[2] = runBlocking { (ds.data.first()) }[URI_2]!!;
        urilist[3] = runBlocking { (ds.data.first()) }[URI_3]!!;
        urilist[4] = runBlocking { (ds.data.first()) }[URI_4]!!;
        urilist[5] = runBlocking { (ds.data.first()) }[URI_5]!!;

        isEmptylist[0] = runBlocking { (ds.data.first()) }[EMPTY_0]!!;
        isEmptylist[1] = runBlocking { (ds.data.first()) }[EMPTY_1]!!;
        isEmptylist[2] = runBlocking { (ds.data.first()) }[EMPTY_2]!!;
        isEmptylist[3] = runBlocking { (ds.data.first()) }[EMPTY_3]!!;
        isEmptylist[4] = runBlocking { (ds.data.first()) }[EMPTY_4]!!;
        isEmptylist[5] = runBlocking { (ds.data.first()) }[EMPTY_5]!!;

////        var test = runBlocking { (ds.data.first()) }[NAME_PURGE]!!; if (test != "default") purgename.value = test
//        var test = runBlocking { (ds.data.first()) }[NAME_0]!!;     if (test != "default") namelist[0] = test
//        test = runBlocking { (ds.data.first()) }[NAME_1]!!;     if (test != "default") namelist[1] = test
//        test = runBlocking { (ds.data.first()) }[NAME_2]!!;     if (test != "default") namelist[2] = test
//        test = runBlocking { (ds.data.first()) }[NAME_3]!!;     if (test != "default") namelist[3] = test
//        test = runBlocking { (ds.data.first()) }[NAME_4]!!;     if (test != "default") namelist[4] = test
//
////        test = runBlocking { (ds.data.first()) }[URI_PURGE]!!; if (test != "default") purgeuri.value = test
//        test = runBlocking { (ds.data.first()) }[URI_0]!!;     if (test != "default") urilist[0] = test
//        test = runBlocking { (ds.data.first()) }[URI_1]!!;     if (test != "default") urilist[1] = test
//        test = runBlocking { (ds.data.first()) }[URI_2]!!;     if (test != "default") urilist[2] = test
//        test = runBlocking { (ds.data.first()) }[URI_3]!!;     if (test != "default") urilist[3] = test
//        test = runBlocking { (ds.data.first()) }[URI_4]!!;     if (test != "default") urilist[4] = test

//        test = runBlocking { (ds.data.first()) }[COVER_PURGE]!!; if (test != "default") purgecover.value = test
//        test = runBlocking { (ds.data.first()) }[COVER_0]!!;     if (test != "default") coverlist[0] = test
//        test = runBlocking { (ds.data.first()) }[COVER_1]!!;     if (test != "default") coverlist[1] = test
//        test = runBlocking { (ds.data.first()) }[COVER_2]!!;     if (test != "default") coverlist[2] = test
//        test = runBlocking { (ds.data.first()) }[COVER_3]!!;     if (test != "default") coverlist[3] = test
//        test = runBlocking { (ds.data.first()) }[COVER_4]!!;     if (test != "default") coverlist[4] = test
    }
//    private val NAME_PURGE = stringPreferencesKey("name_purge")
    private val NAME_0 = stringPreferencesKey("name_0")
    private val NAME_1 = stringPreferencesKey("name_1")
    private val NAME_2 = stringPreferencesKey("name_2")
    private val NAME_3 = stringPreferencesKey("name_3")
    private val NAME_4 = stringPreferencesKey("name_4")
    private val NAME_5 = stringPreferencesKey("name_5")

    private val URI_0  = stringPreferencesKey("uri_0")
    private val URI_1  = stringPreferencesKey("uri_1")
    private val URI_2  = stringPreferencesKey("uri_2")
    private val URI_3  = stringPreferencesKey("uri_3")
    private val URI_4  = stringPreferencesKey("uri_4")
    private val URI_5  = stringPreferencesKey("uri_5")

    private val EMPTY_0  = booleanPreferencesKey("empty_0")
    private val EMPTY_1  = booleanPreferencesKey("empty_1")
    private val EMPTY_2  = booleanPreferencesKey("empty_2")
    private val EMPTY_3  = booleanPreferencesKey("empty_3")
    private val EMPTY_4  = booleanPreferencesKey("empty_4")
    private val EMPTY_5  = booleanPreferencesKey("empty_5")
    
//    private val COVER_0  = stringPreferencesKey("cover_0")
//    private val COVER_1  = stringPreferencesKey("cover_1")
//    private val COVER_2  = stringPreferencesKey("cover_2")
//    private val COVER_3  = stringPreferencesKey("cover_3")
//    private val COVER_4  = stringPreferencesKey("cover_4")
}


