import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mycoolmusicplayer.JukeboxSongCard
import com.example.mycoolmusicplayer.PlayerViewModel
import com.example.mycoolmusicplayer.Song
import com.example.mycoolmusicplayer.ui.theme.JukeboxRed
import com.example.mycoolmusicplayer.ui.theme.JukeboxNeonBlue


// the homescreen. this loads up a list of the songs in the music file and displays them really cute.
// It also displays a currently playing song at the top if on is selected. If not, it just says "My Cool Music Player"
//And we're keeping that jukebox aesthetic throughout with the colors and shapes.
// I worked at a 50s style diner in high school, so I truly am a fan of this aesthetic.
@Composable
fun HomeScreen(
    songs: List<Song>,
    onSongClick: (Song) -> Unit,
    playerViewModel: PlayerViewModel,
    onNowPlayingClick: () -> Unit
) {
    val currentlyPlayingSong by playerViewModel.currentSong.collectAsState()
    val isPlaying by playerViewModel.isPlaying.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        Row {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(JukeboxNeonBlue)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (currentlyPlayingSong.title.isNotEmpty()) {
                    Text(
                        text = "Now Playing: ${currentlyPlayingSong.title} - ${currentlyPlayingSong.artist}",
                        style = MaterialTheme.typography.titleLarge,
                        color = JukeboxRed,
                        modifier = Modifier.clickable { onNowPlayingClick() }
                    )
                } else {
                    Text(
                        text = "My Cool Music Player",
                        style = MaterialTheme.typography.titleLarge,
                        color = JukeboxRed
                    )
                }
            }
        }
        // Row with the playback controls buttons!
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { playerViewModel.playPrevious() }) {
                Text("Previous")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                if (isPlaying) playerViewModel.pause() else playerViewModel.resume()
            }) {
                Text(if (isPlaying) "Pause" else "Play") //nifty condensed else statement, recognizes if the song is playing or not.
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = { playerViewModel.playNext() }) {
                Text("Next")
            }
        }
        Spacer(modifier = Modifier.height(8.dp)) //giving my buttons some room to breathe.
        Row {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                //lambda function, iterates through te list of songs and creates JukeBoxSogCards for each one. Then, onSongClick gets called when it's clicked and does the thing (plays the song, takes you to the PlayerScreen).
                items(songs) { song ->
                    JukeboxSongCard(song = song, playerViewModel = playerViewModel) { onSongClick(song) }
                    HorizontalDivider()
                }
            }
        } //now all the songs can live in harmony in a list. click it to go to it and play it.
    }
}