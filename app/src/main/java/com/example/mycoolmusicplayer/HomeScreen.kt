import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    onSongClick: (Song) -> Unit, playerViewModel: PlayerViewModel
) {
    val currentlyPlayingSong by playerViewModel.currentSong.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        Row(

        )
        {
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
                        color = JukeboxRed
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
        Spacer(modifier = Modifier.height(8.dp))
        Row(

        )
        {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                items(songs) { song ->
                    JukeboxSongCard(song = song) { onSongClick(song) }
                    HorizontalDivider()
                }
            }
        }


    }
}

