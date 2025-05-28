package com.example.mycoolmusicplayer

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

//this whole thing is how it handles picking files that are stored locally.
class FilePickerViewModel : ViewModel() {
    val selectedFileUri = mutableStateOf<Uri?>(null)//oh yea, I remember mutable states. It means it can change over time. so in this case, it might change over time, or be null at a time.

    fun pickFile(launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "audio/mpeg" //had to do a double take at THAT. but we're specifying the type of file we're letting exist here.
            addCategory(Intent.CATEGORY_OPENABLE) //can I open this content?
        }
        launcher.launch(intent) //go for it. handles the rest of the launching of the intent.
    }

    fun handleFileResult(uri: Uri?) {
        selectedFileUri.value = uri //during run time, the uri is set to whatever the user picks. and value is holding that and updating it.
    } //is it good? or not good?
}