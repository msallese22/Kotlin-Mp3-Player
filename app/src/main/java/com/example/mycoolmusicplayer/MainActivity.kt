package com.example.mycoolmusicplayer

import HomeScreen
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mycoolmusicplayer.ui.theme.MyCoolMusicPlayerTheme
import android.provider.MediaStore
import android.net.Uri
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


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
                    val songs = getSongs()
                    playerViewModel.setSongs(songs)


                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(
                                songs = getSongs(),
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
                        composable("player") {
                            PlayerScreen(
                                viewModel = playerViewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }

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

    //retrieving the songs from the device's media storage! if there isn't anything in it, then it says "oh, okay" and doesn't do anything.
    fun getSongs(): List<Song> {
        val songs = mutableListOf<Song>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )

        //cursor iterates through the results, and if something is there, it adds it up to the list!
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            //going through the cursor and finding each song's details. moving 'it' to the next item.
            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val title = it.getString(titleColumn)
                val artist = it.getString(artistColumn)
                val duration = it.getLong(durationColumn)
                val uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id.toString())
                songs.add(Song(uri, title, artist, duration))
            }
        }
        return songs
    }
    //return what is found.

}

