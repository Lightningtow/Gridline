package com.lightningtow.gridline.data

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.lightningtow.gridline.PlaylistShortcutStore
import com.lightningtow.gridline.TrackShortcutStore

//import com.lightningtow.gridline.ShortcutList
//import com.lightningtow.gridline.ShortcutStruct
//import com.lightningtow.gridline.p_PlaylistShortcut
//import com.lightningtow.gridline.p_TrackList
//import com.lightningtow.gridline.p_TrackShortcut
import com.lightningtow.gridline.ui.components.SHORTCUT_TYPE
import java.io.InputStream
import java.io.OutputStream

object TrackShortcutStoreSerializer : Serializer<TrackShortcutStore> {
    override val defaultValue: TrackShortcutStore = TrackShortcutStore.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): TrackShortcutStore {
        try {
            return TrackShortcutStore.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: TrackShortcutStore,
        output: OutputStream
    ) = t.writeTo(output)
}

val Context.TrackShortcutStoreDataStore: DataStore<TrackShortcutStore> by dataStore(
    fileName = "TrackShortcutStore.pb",
    serializer = TrackShortcutStoreSerializer
)
object PlaylistShortcutStoreSerializer : Serializer<PlaylistShortcutStore> {
    override val defaultValue: PlaylistShortcutStore = PlaylistShortcutStore.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): PlaylistShortcutStore {
        try {
            return PlaylistShortcutStore.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: PlaylistShortcutStore,
        output: OutputStream
    ) = t.writeTo(output)
}

val Context.PlaylistShortcutStoreDataStore: DataStore<PlaylistShortcutStore> by dataStore(
    fileName = "PlaylistShortcutStore.pb",
    serializer = PlaylistShortcutStoreSerializer
)
//object ShortcutListSerializer : Serializer<ShortcutList> {
//    override val defaultValue: ShortcutList = ShortcutList.getDefaultInstance()
//    override suspend fun readFrom(input: InputStream): ShortcutList {
//        try {
//            return ShortcutList.parseFrom(input)
//        } catch (exception: InvalidProtocolBufferException) {
//            throw CorruptionException("Cannot read proto.", exception)
//        }
//    }
//
//    override suspend fun writeTo(
//        t: ShortcutList,
//        output: OutputStream
//    ) = t.writeTo(output)
//}
//
//val Context.shortcutListDataStore: DataStore<ShortcutList> by dataStore(
//    fileName = "ShortcutList.pb",
//    serializer = ShortcutListSerializer
//)

//fun kotlinToProto(item: KotlinShortcut): p_TrackList.p_TrackShortcut {
//    val new = p_TrackList.p_TrackShortcut.newBuilder()
//    new.accessUri = item.accessUri
//    new.coverUri = item.coverUri
////    new.type =
////        if (item.type == SHORTCUT_TYPE.PLAYLIST) ShortcutStruct.ShortcutType.PLAYLIST
////        else if (item.type == SHORTCUT_TYPE.TRACK) ShortcutStruct.ShortcutType.TRACK
////        else if (item.type == SHORTCUT_TYPE.ARTIST) ShortcutStruct.ShortcutType.ARTIST
////        else ShortcutStruct.ShortcutType.ALBUM
//    new.displayname = item.displayname
//    return new.build()
//}
//
//fun protoToKotlin(item: p_TrackShortcut): KotlinShortcut {
//    val new = KotlinShortcut(
//        accessUri = item.accessUri,
//        coverUri = item.coverUri,
////        type =
////        if (item.type == ShortcutStruct.ShortcutType.PLAYLIST) SHORTCUT_TYPE.PLAYLIST
////        else if (item.type == ShortcutStruct.ShortcutType.TRACK) SHORTCUT_TYPE.TRACK
////        else if (item.type == ShortcutStruct.ShortcutType.ARTIST) SHORTCUT_TYPE.ARTIST
////        else SHORTCUT_TYPE.ALBUM,
//        displayname = item.displayname,
//
//        )
//    return new
//}