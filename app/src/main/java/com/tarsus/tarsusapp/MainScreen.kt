package com.tarsus.tarsusapp

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.Text

@Composable
fun MainScreen(onFilePickerRequested: () -> Unit) {
    var showVideo by remember { mutableStateOf(false) }
    val videoUri = Uri.parse("android.resource://${LocalContext.current.packageName}/${R.raw.ornek}")

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (showVideo) {
            VideoPlayer(videoUri)
        } else {
            Button(onClick = { showVideo = true }) {
                Text(text = "Play Video")
            }
        }

        Button(
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
            onClick = onFilePickerRequested
        ) {
            Text(text = "Open File Picker")
        }
    }
}
