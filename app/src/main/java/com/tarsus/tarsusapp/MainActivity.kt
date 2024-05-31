package com.tarsus.tarsusapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.tarsus.tarsusapp.ui.theme.TarsusAppTheme
import android.widget.VideoView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TarsusAppTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    VideoPlayer()
                }
            }
        }
    }

    @Composable
    fun VideoPlayer() {
        val context = LocalContext.current
        val videoView = AndroidView(factory = {
            VideoView(context)
        }, update = {
            it.apply {
                setVideoURI(Uri.parse("android.resource://${context.packageName}/${R.raw.ornek}"))
                start()
            }
        })

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            videoView
        }
    }
}
