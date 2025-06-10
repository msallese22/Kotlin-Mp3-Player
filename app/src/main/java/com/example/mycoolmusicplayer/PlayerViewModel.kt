// app/src/main/java/com/example/mycoolmusicplayer/PlayerViewModel.kt
package com.example.mycoolmusicplayer

import android.app.Application
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.AssetDataSource
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DataSpec
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
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

    private val _currentSong = MutableStateFlow(Song("", "", "", 0L)) // so it knows it'll change
    val currentSong: StateFlow<Song> = _currentSong //because where the timer is in the current song will change.

    private val _songs =
        MutableStateFlow<List<Song>>(emptyList())//it's a mutable state flow of a LIST, so it can be changed or observed. so it knows if it has stuff or no stuff.

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

            override fun onMediaItemTransition(
                mediaItem: MediaItem?,
                reason: Int
            ) //exoplayer is a little punk and needs to be TOLD to change the name output of what it's playing.
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


    //where we're getting the song from (assets) and then playing it.
    @OptIn(UnstableApi::class)
    fun playSong(song: Song) {
        _currentSong.value = song
        currentIndex = _songs.value.indexOf(song) //find the index of the song in the list of songs
        exoPlayer.seekTo(currentIndex, 0L) //seek to the index of the song
        exoPlayer.play()
    }

    fun pause() = exoPlayer.pause() //duh
    fun resume() = exoPlayer.play() //obvi
    fun seekTo(position: Long) = exoPlayer.seekTo(position) //position is a Long, so we can seek it.
    fun setLoopMode(mode: LoopMode) { //mode--- setting loop to the specific kind we want.
        _loopMode.value = mode
        //exoPlayer understands what repeatMode is, here I am telling it "hey, set up the repeat modes to the loop modes I need."
        exoPlayer.repeatMode = when (mode) {
            LoopMode.NONE -> ExoPlayer.REPEAT_MODE_OFF
            LoopMode.ONE -> ExoPlayer.REPEAT_MODE_ONE
            LoopMode.ALL -> ExoPlayer.REPEAT_MODE_ALL
        } //you get 3 choices -- none, all, or once.
    }

    fun setVolume(volume: Float) {
        exoPlayer.volume =
            volume //exoplayer has a volume property already in it. love that, love that.
    }

    //we already have the current song, so we're returning it and then the next index of the item after that.
    fun getNextSong(): Song? {
        val songs = _songs.value //the songsssss
        val currentIdx = exoPlayer.currentMediaItemIndex //the current index of the beautiful music
        return when (_loopMode.value) {
            LoopMode.ALL -> { //all them loop modes
                if (songs.isEmpty()) null // no songs = no life
                else songs.getOrNull((currentIdx + 1) % songs.size) //then we get the index +1 % the size of the list of songs! wowza!
            }
            LoopMode.ONE -> songs.getOrNull(currentIdx)
            LoopMode.NONE -> songs.getOrNull(currentIdx + 1)
        }//and it is also respecting what loopMode is doing for us and
    }

    override fun onCleared() {
        exoPlayer.release() //release the exoPlayer when we're done with it, so we don't leak memory.
        super.onCleared() //and then clear that viewModel like a toilet flush
    }

    //setting up the songs to be in the player, then we can play them and have a party.
    @OptIn(UnstableApi::class)
    fun setSongs(songs: List<Song>, context: android.content.Context) {
        exoPlayer.clearMediaItems()
        songs.forEach { song ->
            val dataSpec = DataSpec(Uri.parse("asset:///${song.assetFileName}"))

            //it needs a Factory to create the data source for song! we learned about Factories in software development class.
            val factory = DataSource.Factory {
                AssetDataSource(context)
            }

            //needs to be a MediaItem because that is what ExoPlayer likes.
            val mediaItem = MediaItem.fromUri(dataSpec.uri)

            //and then we're creating a media source from the media item, so ExoPlayer can play it.
            val mediaSource = ProgressiveMediaSource.Factory(factory)
                .createMediaSource(mediaItem)

            //and now that all the ingredients are together, we can finally cook this pot roast.
            exoPlayer.addMediaSource(mediaSource)
        }

        exoPlayer.prepare()
        _songs.value = songs //set the songs to the viewModel so it can be observed.
    }

    //checking if exoPlayer has a "next" media item, and then going to the next one, if it even exists.
    fun playNext() {
        if (exoPlayer.hasNextMediaItem())
        {
            exoPlayer.seekToNext()
            _currentSong.value = _songs.value.getOrNull(exoPlayer.currentMediaItemIndex)!!
        }
    }


//checking the next media item, then going to the one before it instead.
    fun playPrevious() {
        if (exoPlayer.hasNextMediaItem())
        {
            exoPlayer.seekToPrevious()
            _currentSong.value = _songs.value.getOrNull(exoPlayer.currentMediaItemIndex)!!
        }
    }

    //formatting the duration so it doesn't look like hot garbage.
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