package com.lightningtow.gridline.data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.lightningtow.gridline.data.PurgeData.namelist
import com.lightningtow.gridline.data.PurgeData.purgename
import com.lightningtow.gridline.data.PurgeData.purgeuri
import com.lightningtow.gridline.data.PurgeData.urilist
import com.lightningtow.gridline.ui.home.dataStore
import com.lightningtow.gridline.utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object PurgeData {

    var currentSlot: Int = 1;
    var choosingPurgelist: Boolean = false

    var purgename: MutableState<String> = mutableStateOf<String>("default")
    var purgeuri: MutableState<String>  = mutableStateOf<String>("default")

    var namelist = mutableStateListOf<String>("default", "default", "default", "default", "default")
    var urilist  = mutableStateListOf<String>("default", "default", "default", "default", "default")




    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun resetStore(context: Context) {
        scope.launch {
            val ds = context.dataStore

            ds.edit { settings -> settings[NAME_PURGE] = "default" }
            ds.edit { settings -> settings[NAME_0] = "default" }
            ds.edit { settings -> settings[NAME_1] = "default" }
            ds.edit { settings -> settings[NAME_2] = "default" }
            ds.edit { settings -> settings[NAME_3] = "default" }
            ds.edit { settings -> settings[NAME_4] = "default" }

            ds.edit { settings -> settings[URI_PURGE] = "default" }
            ds.edit { settings -> settings[URI_0] = "default" }
            ds.edit { settings -> settings[URI_1] = "default" }
            ds.edit { settings -> settings[URI_2] = "default" }
            ds.edit { settings -> settings[URI_3] = "default" }
            ds.edit { settings -> settings[URI_4] = "default" }

            toast(context,"reset dataStore")
        }
    }
    fun setSavedPurges(context: Context) {
        scope.launch {
            val ds = context.dataStore

            if (purgename.value != "default") ds.edit { settings -> settings[NAME_PURGE] = purgename.value }
            if (namelist[0] != "default") ds.edit { settings -> settings[NAME_0] = namelist[0] }
            if (namelist[1] != "default") ds.edit { settings -> settings[NAME_1] = namelist[1] }
            if (namelist[2] != "default") ds.edit { settings -> settings[NAME_2] = namelist[2] }
            if (namelist[3] != "default") ds.edit { settings -> settings[NAME_3] = namelist[3] }
            if (namelist[4] != "default") ds.edit { settings -> settings[NAME_4] = namelist[4] }

            if (purgeuri.value != "default") ds.edit { settings -> settings[URI_PURGE] = purgeuri.value }
            if (urilist[0] != "default") ds.edit { settings -> settings[URI_0] = urilist[0] }
            if (urilist[1] != "default") ds.edit { settings -> settings[URI_1] = urilist[1] }
            if (urilist[2] != "default") ds.edit { settings -> settings[URI_2] = urilist[2] }
            if (urilist[3] != "default") ds.edit { settings -> settings[URI_3] = urilist[3] }
            if (urilist[4] != "default") ds.edit { settings -> settings[URI_4] = urilist[4] }

            toast(context,"saved playlists")
        }
    }
    fun getSavedPurges(context: Context) {
        val ds = context.dataStore

    var test = runBlocking { (ds.data.first()) }[NAME_PURGE]!!; if (test != "default") purgename.value = test
        test = runBlocking { (ds.data.first()) }[NAME_0]!!; if (test != "default") namelist[0] = test
        test = runBlocking { (ds.data.first()) }[NAME_1]!!; if (test != "default") namelist[1] = test
        test = runBlocking { (ds.data.first()) }[NAME_2]!!; if (test != "default") namelist[2] = test
        test = runBlocking { (ds.data.first()) }[NAME_3]!!; if (test != "default") namelist[3] = test
        test = runBlocking { (ds.data.first()) }[NAME_4]!!; if (test != "default") namelist[4] = test

        test = runBlocking { (ds.data.first()) }[URI_PURGE]!!; if (test != "default") purgeuri.value = test
        test = runBlocking { (ds.data.first()) }[URI_0]!!; if (test != "default") urilist[0] = test
        test = runBlocking { (ds.data.first()) }[URI_1]!!; if (test != "default") urilist[1] = test
        test = runBlocking { (ds.data.first()) }[URI_2]!!; if (test != "default") urilist[2] = test
        test = runBlocking { (ds.data.first()) }[URI_3]!!; if (test != "default") urilist[3] = test
        test = runBlocking { (ds.data.first()) }[URI_4]!!; if (test != "default") urilist[4] = test
    }
    private val NAME_PURGE = stringPreferencesKey("name_purge")
    private val NAME_0 = stringPreferencesKey("name_0")
    private val NAME_1 = stringPreferencesKey("name_1")
    private val NAME_2 = stringPreferencesKey("name_2")
    private val NAME_3 = stringPreferencesKey("name_3")
    private val NAME_4 = stringPreferencesKey("name_4")

    private val URI_PURGE = stringPreferencesKey("uri_purge")
    private val URI_0  = stringPreferencesKey("uri_0")
    private val URI_1  = stringPreferencesKey("uri_1")
    private val URI_2  = stringPreferencesKey("uri_2")
    private val URI_3  = stringPreferencesKey("uri_3")
    private val URI_4  = stringPreferencesKey("uri_4")
}


