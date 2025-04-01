package com.example.compose_prepare_mediaplayer_app_class.player

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExoPlayerModule {
    @Provides
    @Singleton
    fun provideExoPlayer(context: Context): ExoPlayer {
        return ExoPlayer.Builder(context).build()
    }
}