package com.example.mycoolmusicplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.mycoolmusicplayer.ui.theme.JukeboxNeonBlue
import com.example.mycoolmusicplayer.ui.theme.JukeboxRed
import com.example.mycoolmusicplayer.ui.theme.JukeboxTeal
import com.example.mycoolmusicplayer.ui.theme.JukeboxYellow


//adding in those custom colors, rounding out the rectangles, I wanted it to have a vintage jukebox vibe.
//by making it a Composable in its own file, it keeps my code neat and organized.


//I made them Cards so they could be shaped more easily and have a shadow efftect. then I added an onclick to them separately,
@Composable
fun JukeboxSongCard(song: Song,  playerViewModel: PlayerViewModel,  onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .shadow(8.dp, RoundedCornerShape(24.dp))
            .background(
                brush = Brush.horizontalGradient(
                    listOf(JukeboxTeal, JukeboxNeonBlue)
                )
            ),
        shape = RoundedCornerShape(24.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val formattedDuration = playerViewModel.formatDurationUs(song.duration * 1000)
            Text(song.title, style = MaterialTheme.typography.titleLarge, color = JukeboxRed)
            Text(song.artist, style = MaterialTheme.typography.bodyMedium, color = JukeboxTeal)
            Text(text = formattedDuration)

        }
    }
}