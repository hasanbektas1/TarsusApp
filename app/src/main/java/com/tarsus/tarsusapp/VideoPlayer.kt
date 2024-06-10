package com.tarsus.tarsusapp

import android.net.Uri
import android.view.KeyEvent
import android.widget.VideoView
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView


// Video oynatma
@Composable
fun VideoPlayer(videoUri: Uri, startPosition: Int, onPositionChanged: (Int) -> Unit) {
    val context = LocalContext.current
    AndroidView(factory = {
        VideoView(context).apply {
            setVideoURI(videoUri)
            setOnCompletionListener {
                seekTo(0)
            }
            setOnPreparedListener {
                seekTo(startPosition)
                start()
            }

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
                            seekTo(currentPosition + 10000) // 10 seconds forward
                            true
                        }
                        KeyEvent.KEYCODE_DPAD_LEFT -> {
                            seekTo(currentPosition - 10000) // 10 seconds backward
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    onPositionChanged(currentPosition)
                }
            }

            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
        }
    })
}
