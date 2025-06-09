package com.example.mycoolmusicplayer

import HomeScreen
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mycoolmusicplayer.ui.theme.MyCoolMusicPlayerTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.media.MediaMetadataRetriever



class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCoolMusicPlayerTheme {
                //setting the material surface so it knows to apply the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    //about to tell it we need the navController and PlayerViewModel up in here. these give it purpose and functionality! I wish real life was this simple.
                    val playerViewModel: PlayerViewModel = viewModel()
                    val navController = rememberNavController()

                    //without getSongs, there are no songs to get!
                    //then we need to set it after we get it.
                    val songs = getSongsWithMetadata()
                    playerViewModel.setSongs(songs, this)


                    //so I can navigate between the home screen and the player screen.
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(
                                songs = getSongsWithMetadata(),
                                onSongClick = { song ->
                                    playerViewModel.playSong(song)
                                    navController.navigate("player")
                                },
                                playerViewModel = playerViewModel,
                                onNowPlayingClick = {
                                    if (playerViewModel.currentSong.value.title.isNotEmpty()) {
                                        navController.navigate("player")
                                    }
                                }
                            )
                        }
                        //do the thing look like PlayerScreen, please.
                        composable("player") {
                            PlayerScreen(
                                viewModel = playerViewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }

                    //do you have permission to read this file?
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO),
                            1
                        )
                    }
                }
            }
        }
    }

    //retrieving the songs from the device's assets folder! if there isn't anything in it, then it says "oh, okay" and doesn't do anything.
//and along with it getting the metadata for each song.
    fun getSongsWithMetadata(): List<Song> {
        val songs = mutableListOf<Song>()
        val assetManager = assets
        val assetFiles = assetManager.list("") ?: emptyArray()

        for (fileName in assetFiles) {
            if (fileName.endsWith(".mp3") || fileName.endsWith(".wav")) {
                val retriever = MediaMetadataRetriever()
                val assetFileDescriptor = assetManager.openFd(fileName)
                retriever.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)

                val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: fileName.removeSuffix(".mp3")
                val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: "Unknown Artist"
                val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L

                songs.add(Song(fileName, title, artist, duration))
                retriever.release()
            }
        }
        return songs
    }
    //return what is found.

}

