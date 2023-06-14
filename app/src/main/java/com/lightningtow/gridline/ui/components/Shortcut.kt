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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.adamratzman.spotify.models.Playable
import com.google.protobuf.InvalidProtocolBufferException
import com.lightningtow.gridline.GridlineApplication.Companion.context
import com.lightningtow.gridline.ShortcutList
import com.lightningtow.gridline.ShortcutStruct
import com.lightningtow.gridline.utils.Constants
import com.lightningtow.gridline.utils.toasty
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import androidx.compose.runtime.getValue

import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue

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
}

val Context.shortcutStructDataStore: DataStore<ShortcutStruct> by dataStore(
    fileName = "ShortcutStruct.pb",
    serializer = ShortcutStructSerializer
)

object ShortcutListSerializer : Serializer<ShortcutList> {
    override val defaultValue: ShortcutList = ShortcutList.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): ShortcutList {
        try {
            return ShortcutList.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: ShortcutList,
        output: OutputStream
    ) = t.writeTo(output)
}

val Context.shortcutListDataStore: DataStore<ShortcutList> by dataStore(
    fileName = "ShortcutList.pb",
    serializer = ShortcutListSerializer
)

//var masterListOfShortcuts: MutableList<KotlinShortcut> = mutableStateListOf()
var realList: List<KotlinShortcut> = listOf()

var masterListOfShortcuts by mutableStateOf(listOf<KotlinShortcut>())

fun downloadShortcutData() {
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val tempList: MutableList<KotlinShortcut> = mutableListOf()
    var listObj: ShortcutList

    scope.launch {// TODO ITS A COROUTINE DUH, RACE CONDITIONS
        listObj = context.shortcutListDataStore.data.first()

        Log.e("scoped", "scoped $listObj")
//        list = runBlocking { context.shortcutListDataStore.data.first()

        for (item in listObj.entriesList) {

            tempList += protoToKotlin(item)
        }
//        realList = listThing
        masterListOfShortcuts = tempList
        toasty(context, "downloaded shortcut data")
    }

}

fun uploadShortcutData() {
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    scope.launch {
        val tempList = mutableListOf<ShortcutStruct>()

        for (item in masterListOfShortcuts) {
//      for (item in datas) {

                tempList += kotlinToProto(item)
        }

        context.shortcutListDataStore.updateData { loginResponse ->
            loginResponse.toBuilder()
                .clearEntries()
                .addAllEntries(tempList)
                .build()
        }
        toasty(context, "uploaded shortcut data") // todo i dont think this fake context works
    }
}

fun kotlinToProto(item: KotlinShortcut): ShortcutStruct {
    val new = ShortcutStruct.newBuilder()
    new.accessUri = item.accessUri
    new.coverUri = item.coverUri
    new.type =
        if (item.type == SHORTCUT_TYPE.PLAYLIST) ShortcutStruct.ShortcutType.PLAYLIST
        else if (item.type == SHORTCUT_TYPE.TRACK) ShortcutStruct.ShortcutType.TRACK
        else if (item.type == SHORTCUT_TYPE.ARTIST) ShortcutStruct.ShortcutType.ARTIST
        else ShortcutStruct.ShortcutType.ALBUM
    new.displayname = item.displayname
    return new.build()
}

fun protoToKotlin(item: ShortcutStruct): KotlinShortcut {
    val new = KotlinShortcut(
        accessUri = item.accessUri,
        coverUri = item.coverUri,
        type =
        if (item.type == ShortcutStruct.ShortcutType.PLAYLIST) SHORTCUT_TYPE.PLAYLIST
        else if (item.type == ShortcutStruct.ShortcutType.TRACK) SHORTCUT_TYPE.TRACK
        else if (item.type == ShortcutStruct.ShortcutType.ARTIST) SHORTCUT_TYPE.ARTIST
        else SHORTCUT_TYPE.ALBUM,
        displayname = item.displayname,

        )
    return new
}


val datas = listOf(
    KotlinShortcut(
        accessUri = Constants.ROADKILL,
        coverUri = Constants.RK_COVER,
        type = SHORTCUT_TYPE.PLAYLIST,
        displayname = "Roadkill"

    ),

    KotlinShortcut(
        accessUri = Constants.OMNI,
        coverUri = Constants.OMNI_COVER,
        type = SHORTCUT_TYPE.PLAYLIST,
        displayname = "Omniscience"
    )
)


// https://stackoverflow.com/questions/64430872/how-to-save-a-list-of-objects-with-proto-datastore
//suspend fun uploadStuff() {
//fun uploadStuff() {
//    val cover: String = "default2"
//    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
//    scope.launch {
////        val thing: ShortcutStruct (
////            accessUri = Constants.ROADKILL,
////            coverUri = "https://i.scdn.co/image/ab67706c0000bebbfee4892fcb2d95910502c238",
////            type = SHORTCUT_TYPE.PLAYLIST
////        )
//        updateShortcutData(datas)
////        context.shortcutListDataStore.updateData { thing ->
////            thing.toBuilder()
////                .clearEntries()
////                .setEntries(datas)
////                .build()
////        }
//
//    }
//}
// todo uncomment funcs
/*
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
}*/

data class KotlinShortcut(
    val accessUri: String,
    val coverUri: String,
    val type: SHORTCUT_TYPE,
    val displayname: String
) {


}



