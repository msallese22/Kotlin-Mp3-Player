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

    private val _currentSong = MutableStateFlow(Song(Uri.EMPTY, "", "", 0L)) // so it knows it'll change
    val currentSong: StateFlow<Song> = _currentSong

    private val _songs = MutableStateFlow<List<Song>>(emptyList())//it's a mutable state flow of a LIST, so it can be changed or observed. so it knows if it has stuff or no stuff.

    private var currentIndex = -1 //song we are currently playing, if it's -1, then nothing is playing atm.


    init {
        //flipping the boolean from not playing to is playing, daddy-o
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }
            override fun onPlaybackStateChanged(state: Int)  //getting the playback state of the player
            {
                _duration.value = exoPlayer.duration.coerceAtLeast(0L)
            }
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) //exoplayer is a little punk and needs to be TOLD to change the name output of what it's playing.
            {
                val index = exoPlayer.currentMediaItemIndex
                val song = _songs.value.getOrNull(index)
                if (song != null) {
                    _currentSong.value = song
                    currentIndex = index
                }
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
        val index = _songs.value.indexOfFirst { it.uri == song.uri } //URI -- Uniformed Resource Identifier -- tells me hey here is the reference to the file. Need this to make it go.
        if (index != -1) {
            //we don't want to prepare it twice, or else it'll never ever stop playing.
            currentIndex = index
            exoPlayer.seekTo(index, 0)
            exoPlayer.play()// Shania Twain voice "let's go, girls"

            _currentSong.value = song
        }
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

    //setting up the songs to be in the player, then we can play them and have a party.
    fun setSongs(songs: List<Song>) {
        _songs.value = songs
        val mediaItems = songs.map { MediaItem.fromUri(it.uri) }
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
        currentIndex = -1
    }

    //gets the index of the current song, adds 1 to it to play the nxt
    fun playNext() {
        val songsList = _songs.value
        if (songsList.isNotEmpty()) {
            val nextIndex = (currentIndex + 1).coerceAtMost(songsList.lastIndex)
            if (nextIndex != currentIndex) {
                playSong(songsList[nextIndex])
            }
        }
    }

    //gets index of current song, subtracts 1 from it to play the previous
    fun playPrevious() {
        val songsList = _songs.value
        if (songsList.isNotEmpty()) {
            val prevIndex = (currentIndex - 1).coerceAtLeast(0)
            if (prevIndex != currentIndex) {
                playSong(songsList[prevIndex])
            }
        }
    }

    fun formatDurationUs(durationUs: Long): String {
        val totalSeconds = durationUs / 1_000_000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%d:%02d".format(minutes, seconds)
    }
}

enum class LoopMode { NONE, ONE, ALL }
//things I gotta enumerate through for the loopmode. sh
// now our user what loopmode they're currently using.