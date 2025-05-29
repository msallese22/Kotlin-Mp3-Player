package com.example.mycoolmusicplayer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun HomeScreen(
    songs: List<Song>,
    onSongClick: (Song) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(songs) { song ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSongClick(song) }
                    .padding(vertical = 8.dp)
            ){
                Text(text = song.title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                Text(text = song.artist, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                Text(text = "Duration: ${song.duration} seconds", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
            HorizontalDivider()
        }
    }
}
