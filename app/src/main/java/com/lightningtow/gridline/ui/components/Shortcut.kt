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

public enum class SHORTCUT_TYPE { PLAYLIST, TRACK, ARTIST, ALBUM }

object ShortcutStructSerializer : Serializer<ShortcutStruct> {
    override val defaultValue: ShortcutStruct = ShortcutStruct.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): ShortcutStruct {
        try { return ShortcutStruct.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception) } }
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
        try { return ShortcutList.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception) } }
    override suspend fun writeTo(
        t: ShortcutList,
        output: OutputStream
    ) = t.writeTo(output)
}

val Context.shortcutListDataStore: DataStore<ShortcutList> by dataStore(
    fileName = "ShortcutList.pb",
    serializer = ShortcutListSerializer
)

//var coverUsed: MutableState<String?> = mutableStateOf("default")
//var realCover: String = "default"
var realList: List<KotlinShortcut> = listOf()
@Composable
public fun ShortcutIcon(item: KotlinShortcut) {
    ShortcutIcon(type = item.type, displayname = "default", accessUri = item.accessUri, coverUri = item.coverUri)
}

@Composable
public fun ShortcutIcon(
    type: SHORTCUT_TYPE,
    displayname: String,
    accessUri: String,
    coverUri: String
//    cover: String? = null,
) {

//    ) {
    val context = LocalContext.current
//    val coveraaa by remember { mutableStateOf(realCover) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable() {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(accessUri))
            browserIntent.putExtra(
                Intent.EXTRA_REFERRER,
                Uri.parse("android-app://" + context.packageName)
            )

            //  Uri.parse(track.externalUrls.first { it.name == "spotify" }.url) // what does this do
            Log.e("deeplink", "deeplinking to $accessUri")
            ContextCompat.startActivity(context, browserIntent, null)
        }
    ) {


//        val cover: MutableState<Any?> = mutableStateOf(null)
//        val test = getStuff()
//        Log.e("ctrlfme", "displaying $coveraaa")
        GlideImage(
//            imageModel = getCover(uri, type) ?: Constants.DEFAULT_MISSING,
            imageModel = coverUri ?: Constants.DEFAULT_MISSING,
            modifier = Modifier
                .size(150.dp)
        )
        Text(displayname)
    }
}

fun downloadShortcutData() {
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    var ffs: String? = null
//    var data: ShortcutStruct? = null
    val listThing: MutableList<KotlinShortcut> = mutableListOf()
    var listObj: ShortcutList

    scope.launch {// TODO ITS A COROUTINE DUH, RACE CONDITIONS
        listObj = context.shortcutListDataStore.data.first()

        Log.e("scoped", "scoped $listObj")
//        list = runBlocking { context.shortcutListDataStore.data.first()

        for (item in listObj.entriesList) {

            listThing += protoToKotlin(item)
        }
        realList = listThing
        toasty(context, "downloaded shortcut data")
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

    val newnew = new.build()
    return newnew
}
fun protoToKotlin(item: ShortcutStruct): KotlinShortcut {
    val new = KotlinShortcut(
    accessUri = item.accessUri,
    coverUri = item.coverUri,
    type =
        if (item.type == ShortcutStruct.ShortcutType.PLAYLIST) SHORTCUT_TYPE.PLAYLIST
        else if (item.type == ShortcutStruct.ShortcutType.TRACK) SHORTCUT_TYPE.TRACK
        else if (item.type == ShortcutStruct.ShortcutType.ARTIST) SHORTCUT_TYPE.ARTIST
        else SHORTCUT_TYPE.ALBUM
    )

    return new
}

fun uploadShortcutData(
//list: List<KotlinShortcut>
) {
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    scope.launch {
        val listThing = mutableListOf<ShortcutStruct>()
        for (item in datas) {



            listThing += kotlinToProto(item)
        }

        context.shortcutListDataStore.updateData { loginResponse ->


            loginResponse.toBuilder()
                .addAllEntries(listThing)
    //                .setAllEntries(0, newnew)
                .build()
    //                .addUsersData(userData).build()
        }
        toasty(context, "done updating")
    }
}
//suspend fun idk() {
//    val ffs = ShortcutStructSerializer.defaultValue
val datas = listOf(
    KotlinShortcut(accessUri = Constants.ROADKILL,
        coverUri = "https://i.scdn.co/image/ab67706c0000bebbfee4892fcb2d95910502c238",
        type = SHORTCUT_TYPE.PLAYLIST
    ),

    KotlinShortcut(
        accessUri = Constants.OMNI,
        coverUri = "https://i.scdn.co/image/ab67706c0000bebbfee4892fcb2d95910502c238",
        type = SHORTCUT_TYPE.PLAYLIST
    )
)
//}
val test: ShortcutStruct = ShortcutStruct.getDefaultInstance()
val test2: ShortcutStruct = ShortcutStructSerializer.defaultValue

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
    val type: SHORTCUT_TYPE
) {


}



