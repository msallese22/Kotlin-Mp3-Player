// app/src/main/java/com/example/mycoolmusicplayer/PlayerViewModel.kt
package com.example.mycoolmusicplayer

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


//exoPlayer is the super neat cool powerful media player library for Android. Here we are, setting it up to work with My Cool Music Player.
class PlayerViewModel(application: Application) : AndroidViewModel(application) {
    private val exoPlayer = ExoPlayer.Builder(application).build() //cook it up, baby!
    private val _isPlaying = MutableStateFlow(false) //change if is on or off!
    val isPlaying: StateFlow<Boolean> = _isPlaying //hello, is this thing on?

    private val _position = MutableStateFlow(0L) //where we currently are in the song
    val position: StateFlow<Long> = _position //StateFlow I'm observin' that flow, yo

    private val _duration = MutableStateFlow(0L) //how long is our song
    val duration: StateFlow<Long> = _duration //need to observe it. so I can update the UI with it.

    private val _loopMode = MutableStateFlow(LoopMode.NONE) //like my ipod's stuck on replay
    val loopMode: StateFlow<LoopMode> = _loopMode //tell me the state of the loop mode.

    init {
        //flipping the boolean from not playing to is playing, daddy-o
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }
            override fun onPlaybackStateChanged(state: Int) {
                _duration.value = exoPlayer.duration.coerceAtLeast(0L)
            }
        })
        //launch this puppy
        viewModelScope.launch {
            while (true) {
                _position.value = exoPlayer.currentPosition
                kotlinx.coroutines.delay(500) //update the position of the viewModel every half a second
            }
        }
    }



    fun playSong(song: Song) {
        exoPlayer.setMediaItem(MediaItem.fromUri(song.uri)) //URI -- Uniformed Resource Identifier -- tells me hey here is the reference to the file. Need this to make it go.
        exoPlayer.prepare() //get it ready, boys!
        exoPlayer.play()// Shania Twain voice "let's go, girls"
    }

    fun pause() = exoPlayer.pause() //duh
    fun resume() = exoPlayer.play() //obvi
    fun seekTo(position: Long) = exoPlayer.seekTo(position) //position is a Long, so we can seek it.
    fun setLoopMode(mode: LoopMode) { //mode--- setting loop to the specific kind we want.
        _loopMode.value = mode
        exoPlayer.repeatMode = when (mode) {
            LoopMode.NONE -> ExoPlayer.REPEAT_MODE_OFF
            LoopMode.ONE -> ExoPlayer.REPEAT_MODE_ONE
            LoopMode.ALL -> ExoPlayer.REPEAT_MODE_ALL
        } //you get 3 choices -- none, all, or once.
    }
    fun setVolume(volume: Float) {
        exoPlayer.volume = volume //exoplayer has a volume property already in it. love that, love that.
    }
    override fun onCleared() {
        exoPlayer.release() //release the exoPlayer when we're done with it, so we don't leak memory.
        super.onCleared() //and then clear that viewModel like a toilet flush
    }
    val currentSong: Song
        get() = exoPlayer.currentMediaItem?.let { mediaItem ->
            mediaItem.mediaMetadata.durationMs?.let {
                Song(
                    uri = mediaItem.localConfiguration?.uri ?: Uri.EMPTY,
                    title = mediaItem.mediaMetadata.title.toString(),
                    artist = mediaItem.mediaMetadata.artist.toString(),
                    duration = it //convert microseconds to milliseconds
                )
            }
        } ?: Song(Uri.EMPTY, "Unknown", "Unknown", 0L) //if no song is playing, return a default song.
    fun formatDurationUs(durationUs: Long): String {
        val totalSeconds = durationUs / 1_000_000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%d:%02d".format(minutes, seconds)
    }
}

enum class LoopMode { NONE, ONE, ALL }
//things I gotta enumerate through for the loopmode. show our user what loopmode they're currently using.