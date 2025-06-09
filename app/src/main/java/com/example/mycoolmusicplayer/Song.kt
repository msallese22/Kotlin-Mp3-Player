package com.example.mycoolmusicplayer

import android.net.Uri

//it's a data class, it holds data about the songs. this way, any time I call "Song" it remembers what that means and is and has.
data class Song(
    val assetFileName: String,
    val title: String,
    val artist: String,
    val duration: Long
)