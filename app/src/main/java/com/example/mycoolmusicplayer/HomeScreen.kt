package com.example.mycoolmusicplayer

import android.os.Build
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onSongClick: (Song) -> Unit = {}
){
    val context = LocalContext.current
    val permissionState = rememeberPermissionState(
        permission = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            Manifest.permission.READ_MEDIA_AUDIO
        } else{
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )

    LaunchedEffect(Unit){
        if (!permissionState.status.isGranted){
            permissionState.launchPermissionRequest()
        }

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My Cool Music Player") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ){ paddingValues ->
        if (permissionState.status.isGranted){
            SongsContent(
                viewModel = viewModel,
                onSongClick = onSongClick,
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            PermissionRequired(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            )
        }

    }
}