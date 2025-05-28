// app/src/main/java/com/example/mycoolmusicplayer/PlayerScreen.kt
package com.example.mycoolmusicplayer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
    song: Song
) {
    val isPlaying by viewModel.isPlaying.collectAsState()
    val position by viewModel.position.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val loopMode by viewModel.loopMode.collectAsState()
    var volume by remember { mutableStateOf(1f) }

    LaunchedEffect(song) {
        viewModel.playSong(song)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = song.title)
        Text(text = song.artist)
        // Seekbar
        Slider(
            value = position.coerceAtMost(duration).toFloat(),
            onValueChange = { viewModel.seekTo(it.toLong()) },
            valueRange = 0f..duration.toFloat()
        )
        Row {
            Button(onClick = { if (isPlaying) viewModel.pause() else viewModel.resume() }) {
                Text(if (isPlaying) "Pause" else "Play")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                val next = when (loopMode) {
                    LoopMode.NONE -> LoopMode.ONE
                    LoopMode.ONE -> LoopMode.ALL
                    LoopMode.ALL -> LoopMode.NONE
                }
                viewModel.setLoopMode(next)
            }) {
                Text("Loop: $loopMode")
            }
        }
        // Volume control
        Text("Volume")
        Slider(
            value = volume,
            onValueChange = {
                volume = it
                viewModel.setVolume(it)
            },
            valueRange = 0f..1f
        )
    }
}