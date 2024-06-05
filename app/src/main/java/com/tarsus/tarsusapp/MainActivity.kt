package com.tarsus.tarsusapp

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
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
import androidx.compose.runtime.remember

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
        val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.ornek}")
        val context = LocalContext.current

        val videoView = remember {
            VideoView(context).apply {
                setVideoURI(videoUri)

                setOnCompletionListener {
                    seekTo(0) // Video tamamlandığında başa sar
                }
                start() // Videoyu başlat
            }
        }

        AndroidView(
            factory = {
                videoView.apply {
                    setOnKeyListener { v, keyCode, event ->
                        if (event.action == KeyEvent.ACTION_DOWN) {
                            when (keyCode) {
                                KeyEvent.KEYCODE_DPAD_CENTER -> {
                                    if (isPlaying) {
                                        pause()
                                    } else {
                                        start()
                                    }
                                    true
                                }
                                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                                    seekTo(currentPosition + 10000) // 10 saniye ileri sar
                                    true
                                }
                                KeyEvent.KEYCODE_DPAD_LEFT -> {
                                    seekTo(currentPosition - 10000) // 10 saniye geri sar
                                    true
                                }
                                else -> false
                            }
                        } else {
                            false
                        }
                    }
                    isFocusable = true
                    isFocusableInTouchMode = true
                    requestFocus()
                }
            },
            update = { view ->
                view.setOnKeyListener { v, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_DOWN) {
                        when (keyCode) {
                            KeyEvent.KEYCODE_DPAD_CENTER -> {
                                if (view.isPlaying) {
                                    view.pause()
                                } else {
                                    view.start()
                                }
                                true
                            }
                            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                                view.seekTo(view.currentPosition + 10000) // 10 saniye ileri sar
                                true
                            }
                            KeyEvent.KEYCODE_DPAD_LEFT -> {
                                view.seekTo(view.currentPosition - 10000) // 10 saniye geri sar
                                true
                            }
                            else -> false
                        }
                    } else {
                        false
                    }
                }
            }
        )
    }
}