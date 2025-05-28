package com.example.mycoolmusicplayer

import android.net.Uri
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.material3.*

@Composable
fun FilePickerScreen(viewModel: FilePickerViewModel) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data
        viewModel.handleFileResult(uri)
    }

    val selectedFileUri by viewModel.selectedFileUri

    Column {
        Button(onClick = { viewModel.pickFile(launcher) }) {
            Text("Select MP3 File")
        }
        selectedFileUri?.let {
            Text("Selected File: $it")
        }
    }
}