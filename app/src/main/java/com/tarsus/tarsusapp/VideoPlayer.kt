package com.tarsus.tarsusapp

import android.net.Uri
import android.view.KeyEvent
import android.widget.VideoView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun VideoPlayer(videoUri: Uri) {
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
