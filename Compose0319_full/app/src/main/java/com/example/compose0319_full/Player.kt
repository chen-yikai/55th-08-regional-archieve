package com.example.compose0319_full

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class Player : ViewModel() {
    var data = MutableStateFlow<List<Music>>(emptyList())
    var apiData: StateFlow<List<Music>> = data

    lateinit var player: ExoPlayer

    private var _isPlaying = MutableStateFlow(false)
    var isPlaying: StateFlow<Boolean> = _isPlaying

    private var _currentIndex = MutableStateFlow(0)
    var currentIndex: StateFlow<Int> = _currentIndex

    private var _mediaItem = MutableStateFlow(MediaMetadata.Builder().build())
    var mediaItem: StateFlow<MediaMetadata> = _mediaItem

    @OptIn(UnstableApi::class)
    fun initMediaApi(context: Context) {
        player = ExoPlayer.Builder(context).build()
        viewModelScope.launch {
            val client = OkHttpClient()
            val gson = Gson()

            withContext(Dispatchers.IO) {
                val request = Request.Builder().get().url("$host/sounds").build()
                val response = client.newCall(request).execute().use {
                    it.body?.string()
                }
                data.value = gson.fromJson(response, object : TypeToken<List<Music>>() {}.type)
            }
        }
        var mediaItemList: List<MediaItem> = data.value.map {
            MediaItem.Builder().setMediaId(it.id.toString()).setUri("$host${it.audio}")
                .setMediaMetadata(
                    MediaMetadata.Builder().setTitle(it.name).setDescription(it.cover)
                        .build()
                ).build()
        }

        player.addListener(
            object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _isPlaying.value = isPlaying
                }

                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    _currentIndex.value = player.currentMediaItemIndex
                    _mediaItem.value = mediaMetadata
                }
            }
        )
        player.setMediaItems(mediaItemList)
        player.prepare()
        player.play()
    }

    fun toggle() {
        if (_isPlaying.value) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun next() {
        player.seekToNextMediaItem()
    }

    fun prev() {
        player.seekToPrevious()
    }
}