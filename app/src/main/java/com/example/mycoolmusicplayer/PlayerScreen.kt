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
    onBack: () -> Unit
) {
    val currentSong by viewModel.currentSong.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val position by viewModel.position.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val loopMode by viewModel.loopMode.collectAsState()
    var volume by remember { mutableStateOf(1f) }


    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = onBack) {
            Text("Back")
        }
        Text(text = currentSong.title)
        Text(text = currentSong.artist)
        // finds the time during the song, goes there.
        Slider(
            value = position.coerceAtMost(duration).toFloat(),
            onValueChange = { viewModel.seekTo(it.toLong()) },
            valueRange = 0f..duration.toFloat()
        )
            Text(
                text = "${viewModel.formatDurationUs(position * 1000)} / ${viewModel.formatDurationUs(duration * 1000)}",
                modifier = Modifier.padding(vertical = 8.dp)
            )
        Row {
            Button(onClick = { viewModel.playPrevious() }) {
                Text("Previous")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = { if (isPlaying) viewModel.pause() else viewModel.resume() }) {
                Text(if (isPlaying) "Pause" else "Play")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = { viewModel.playNext() }) {
                Text("Next")
            }
            Spacer(Modifier.width(8.dp))
            Row{
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